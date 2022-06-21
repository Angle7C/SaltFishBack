package com.application.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.log.Log;
import com.application.mapper.ProblemMapper;
import com.application.mapper.RecordExtMapper;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RecordService {
    @Value("${output}")
    private String outPath;
    @Value("${input}")
    private String inPath;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private RecordExtMapper recordExtMapper;
    private String exeC(String file, Long problemId, CodeC code) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (code.getType().equals("MSVC")) {
            processBuilder.command("cmd","/c","cl.exe", "/std:c++17",
                    "/O2",
                    "/Fe:",
                    outPath + File.separator + problemId.toString() + File.separator + "1.exe",
                    file);
        } else {
            processBuilder.command("cmd", "/c", "gcc", file, "-o", outPath + File.separator + problemId.toString() + File.separator + "1.exe");
        }
//        Runtime.getRuntime().exec("cmd /c g++"+fil)
        System.out.println(outPath);
        File errorLog = new File(outPath + "\\log");
        processBuilder.redirectError(errorLog);
        Process start = processBuilder.start();
        int bool = start.waitFor();
        System.out.println(bool);
        Assert.isTrue(bool == 0, "编译失败");
        return outPath + File.separator + problemId.toString() + File.separator + "1.exe";
    }

    private File runProcess(String file, String in, Long time) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(file);

        processBuilder.redirectInput(new File(in));
        File out = new File(file.substring(0, file.lastIndexOf(File.separator) + 1) + "out");

        processBuilder.redirectOutput(out);
        Process start = processBuilder.start();
        boolean b = start.waitFor(time, TimeUnit.MILLISECONDS);
        Assert.isTrue(b, "超时运行");
        return out;
    }

    private Boolean judge(String file, String in, String ans, Long time) throws IOException, InterruptedException {
        File tempFile = runProcess(file, in, time);
        File ansFile = new File(ans);
        Assert.isTrue(tempFile.length() == ansFile.length(), "答案错误");
        try (FileInputStream tempStream = new FileInputStream(tempFile);
             FileInputStream ansStream = new FileInputStream(ans)) {
            while (true) {
                int i = tempStream.read();
                int j = ansStream.read();
                if (i == j && i != -1 && j != -1) continue;
                if (i == j && i == -1 && j == -1) return true;
                break;
            }
        } catch (IOException e) {
            LogUtil.info("服务器异常", e);
        }
        return false;

    }

    @Transactional
    public RecordDTO addRecord(Long problemId, String userToken, CodeC code) throws IOException, InterruptedException {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andTokenEqualTo(userToken);
        List<User> users = userMapper.selectByExample(userExample);
        Assert.isTrue(users != null && users.size() == 1, "登录异常");
        Problem problem = problemMapper.selectByPrimaryKey(problemId);
        Assert.notNull(problem, "没有这个问题");

        File file = code.toFile(inPath, problemId, users.get(0).getId());
        String absolutePath = file.getAbsolutePath();
        String path = exeC(absolutePath, problemId, code);
        User user = users.get(0);
        Record record = DTOUtil.getNewRecord(user, problem, path);

        recordMapper.insert(record);
        return new RecordDTO(record,
                new UserDTO(user),
                new ProblemDTO(userMapper.selectByPrimaryKey(problem.getUserId()), problem));
    }

//    @Async
public RecordDTO runProcess(RecordDTO recordDTO) {
    LogUtil.info("准备运行程序");
    String path = recordDTO.getPath();
    Integer num = recordDTO.getProblem().getNum();
    String substring = path.substring(0, path.lastIndexOf(File.separator) + 1);
    recordDTO.setScore(0L);
    for(int i=0;i<num;i++){
        try {
            Boolean judge = judge(path, substring + i+".in", substring + i+".out", 5000L);
            recordDTO.setType(1);
            recordDTO.addSocre(judge);
        } catch (Exception e) {
            LogUtil.error("读入输入输出文件异常", e.getMessage());
            recordDTO.addSocre(false);
            recordDTO.setType(2);
        }finally {
            recordMapper.updateByPrimaryKey(recordDTO.toEntity());
        }
    }
    LogUtil.info("完成运行程序");
    return recordDTO;

}

    public RecordDTO selectRecord(Long id) {
        Record record = recordMapper.selectByPrimaryKey(id);
        Assert.notNull(record, "没有这个记录");
        User user = userMapper.selectByPrimaryKey(record.getUserId());
        Assert.notNull(user, "没有这个用户");
        Problem problem = problemMapper.selectByPrimaryKey(record.getProblemId());
        Assert.notNull(problem, "没有这个问题");
        return new RecordDTO(record,
                new UserDTO(user),
                new ProblemDTO(problem, userMapper.selectByPrimaryKey(problem.getUserId())));
    }
    public  List<RecordDTO> selectRecord(String userToken) {
//        Record record = recordMapper.selectByPrimaryKey(id);
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(userToken);
        List<User> users = userMapper.selectByExample(userExample);
        Assert.isTrue(users.size()==1, "没有这个用户");
        List<Record> records = recordExtMapper.selectDistinctProblem(users.get(0).getId());
        List<RecordDTO> collect = records.stream()
                .map(item -> new RecordDTO(item).HiddePath())
                .collect(Collectors.toList());
        return collect;
    }
    public List<RecordDTO> selectRecord(Long problemId,String token) {
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(token);
        List<User> users = userMapper.selectByExample(userExample);
        Assert.isTrue(users.size()==1, "没有这个用户");
        Problem problem = problemMapper.selectByPrimaryKey(problemId);
        Assert.notNull(problem, "没有这个问题");
        User user=users.get(0);
        RecordExample recordExample=new RecordExample();
        recordExample.createCriteria()
                .andProblemIdEqualTo(problemId)
                .andUserIdEqualTo(user.getId());
        List<Record> records = recordMapper.selectByExample(recordExample);
        List<RecordDTO> collect = records
                .stream()
                .map(item -> new RecordDTO(item).HiddePath())
                .collect(Collectors.toList());
        return collect;
    }
    public List<RecordDTO> selectRecord(Long problemId,Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        Assert.notNull(user, "没有这个用户");
        Problem problem = problemMapper.selectByPrimaryKey(problemId);
        Assert.notNull(problem, "没有这个问题");
        RecordExample recordExample=new RecordExample();
        recordExample.createCriteria()
                .andProblemIdEqualTo(problemId)
                .andUserIdEqualTo(user.getId());
        List<Record> records = recordMapper.selectByExample(recordExample);
        List<RecordDTO> collect = records
                .stream()
                .map(item -> new RecordDTO(item,problem))
                .collect(Collectors.toList());
        return collect;
    }

    public List<RecordDTO> selectUserId(Long id) {
        RecordExample recordExample=new RecordExample();
        recordExample.createCriteria().andUserIdEqualTo(id);
        List<Record> records = recordMapper.selectByExample(recordExample);
        List<RecordDTO> collect = records.stream()
                .map(item -> new RecordDTO(item,
                        problemMapper.selectByPrimaryKey(item.getProblemId())))
                .collect(Collectors.toList());
        return collect;
    }
//    public List<>
}
