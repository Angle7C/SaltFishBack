package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.model.DTO.RecordDTO;
import com.application.model.ResultJson;
import com.application.model.subentity.RecordSub;
import com.application.service.ProblemService;
import com.application.service.RecordService;
import com.application.service.UserService;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
@RestController
@RequestMapping("/Admin")
public class AdminRecordController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private RecordService recordService;
    //查询到用户的所有做题记录
    @GetMapping("/record/{userId}")
    public ResultJson getRecordUser(@PathVariable("userId") Long userId,HttpServletRequest request){
//        Assert.isTrue(UserTokenUtils.checkAdmin(request.getCookies()),"管理员没有登陆");
        List<RecordDTO> list = recordService.selectUserId(userId);
        return new ResultJson().ok("查询到用户的所有做题记录",null,list);
    }
    //查询特定用户特定题目的记录
    @GetMapping("/record/{problenId}/{userId}")
    public ResultJson getRecordId(@PathVariable("userId") Long userId,@PathVariable("probelmId") Long probelmId,HttpServletRequest request){
//        Assert.isTrue(UserTokenUtils.checkAdmin(request.getCookies()),"管理员没有登陆");
        List<RecordDTO> list= recordService.selectRecord(userId,probelmId);
        return new ResultJson().ok("查询用户特定题目的记录",null,list);
    }
    //查询特定记录的代码
    @PostMapping("/recordcode/{recordId}")
    public ResultJson getRecordCode(HttpServletRequest request,@PathVariable("recordId") Long recordId) throws IOException {
//        boolean b = UserTokenUtils.checkAdmin(request.getCookies());
//        Assert.isTrue(b,"管理员没有登陆");
        RecordDTO recordDTO = recordService.selectRecord(recordId);
        recordDTO.getProblem().setDescription(null);
        String path = recordDTO.getPath();
        File file=new File(path.substring(0,path.lastIndexOf(File.separator)+1)+recordDTO.getUser().getId()+"_source.c");
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder stringBuilder=new StringBuilder();
        String str=null;
        while ((str=bufferedReader.readLine())!=null){
            stringBuilder.append(str);
        }
        return new ResultJson().ok("查询代码成功",new RecordSub(stringBuilder.toString(),recordDTO));
    }
}
