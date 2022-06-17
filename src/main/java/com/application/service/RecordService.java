package com.application.service;

import cn.hutool.core.lang.Assert;
import com.application.mapper.ProblemMapper;
import com.application.mapper.RecordMapper;
import com.application.mapper.UserMapper;
import com.application.model.DTO.ProblemDTO;
import com.application.model.DTO.RecordDTO;
import com.application.model.DTO.UserDTO;
import com.application.model.entity.*;
import com.application.model.subentity.CodeC;
import com.application.utils.DTOUtil;
import com.application.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RecordService {
    @Value("${output}")
    private  String outPath;
    @Value("${input}")
    private String inPath;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private ProblemMapper problemMapper;
    private  String exeC(String file) throws IOException, InterruptedException {
        ProcessBuilder processBuilder=new ProcessBuilder();
        processBuilder.command("cmd",file,"-o",file.substring(0,file.lastIndexOf(File.separator)+1)+"1.exe");
        Process start = processBuilder.start();
        boolean bool=start.waitFor(30, TimeUnit.SECONDS);
        Assert.isTrue(bool,"编译失败");
        return outPath+"1.exe";
    }
    private File runProcess(String file,String in,Long time) throws IOException, InterruptedException {
        ProcessBuilder processBuilder=new ProcessBuilder();
        processBuilder.command(file);
        processBuilder.redirectInput(new File(in));
        File out=new File(file.substring(0,file.lastIndexOf(File.separator)+1)+"out");
        processBuilder.redirectOutput(out);
        Process start = processBuilder.start();
        boolean b = start.waitFor(time, TimeUnit.MILLISECONDS);
        Assert.isTrue(b,"超时运行");
        return out;
    }
    private Boolean judge(String file,String in,String ans,Long time) throws IOException, InterruptedException {
        File tempFile=runProcess(file, in, time);
        File ansFile=new File(ans);
        Assert.isTrue(tempFile.length()==ansFile.length(),"答案错误");
        try(FileInputStream tempStream=new FileInputStream(tempFile);
            FileInputStream ansStream=new FileInputStream(ans)){
            while (true){
                int i=tempStream.read();
                int j=ansStream.read();
                if(i==j&&i!=-1&&j!=-1) continue;
                if(i==j&&i==-1&&j==-1) return true;
                break;
            }
        }catch (IOException e){
            LogUtil.info("服务器异常",e);
        }
        return false;

    }
    @Transactional
    public RecordDTO addRecord(Long problemId, String userToken, CodeC code) throws IOException, InterruptedException {
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(userToken);
        List<User> users = userMapper.selectByExample(userExample);
        Assert.isTrue(users!=null&&users.size()==1,"登录异常");
        Problem problem = problemMapper.selectByPrimaryKey(problemId);
        Assert.notNull(problem,"没有这个问题");

        File file = code.toFile(inPath,problemId,users.get(0).getId());
        String absolutePath = file.getAbsolutePath();
        String path = exeC(absolutePath);
        User user=users.get(0);
        Record record = DTOUtil.getNewRecord(user, problem, path);
        recordMapper.insert(record);
        return new RecordDTO(record,
                new UserDTO(user),
                new ProblemDTO(userMapper.selectByPrimaryKey(problem.getUserId()),problem));
    }
    public RecordDTO runProcess(RecordDTO recordDTO){
        String path = recordDTO.getPath();
        String substring = path.substring(0, path.lastIndexOf(File.separator) + 1);
        try {
            Boolean judge = judge(path, substring + "1.in", substring + "1.out", 5000L);
            recordDTO.setType(1);
            if(judge){
                recordDTO.setScore(100L);
            }
            recordMapper.updateByPrimaryKey(recordDTO.toEntity());
            return recordDTO;

        } catch (IOException e) {
            LogUtil.error("文件异常",e.getMessage());
        } catch (InterruptedException e) {
            LogUtil.error("中断异常", e.getMessage());
        }
        return recordDTO;

    }

    public RecordDTO selectRecord(Long id){
        Record record = recordMapper.selectByPrimaryKey(id);
        Assert.notNull(record,"没有这个记录");
        User user = userMapper.selectByPrimaryKey(record.getUserId());
        Assert.notNull(user,"没有这个用户");
        Problem problem = problemMapper.selectByPrimaryKey(record.getProblemId());
        Assert.notNull(problem,"没有这个问题");
        return new RecordDTO(record,
                new UserDTO(user),
                new ProblemDTO(problem,userMapper.selectByPrimaryKey(problem.getUserId())));
    }
}
