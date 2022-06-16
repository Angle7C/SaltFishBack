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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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
    private ProblemMapper problemMapper
    private  String exeC(String file) throws IOException, InterruptedException {
        Process exec = Runtime.getRuntime()
                .exec("cmd /c gcc " + file + " -o " + file.substring(0,file.lastIndexOf(File.separator)+1)+ "1.exe");
        int i = exec.waitFor();
        Assert.isTrue(i==0,"编译失败");

        return outPath+"1.exe";
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
}
