package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.model.DTO.RecordDTO;
import com.application.model.ResultJson;
import com.application.service.RecordService;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RecordController {

    @Autowired
    private RecordService recordService;
    //
    @GetMapping("/record/{problemId}")
    public ResultJson getUserRecord(@PathVariable("problemId") Long problemId, HttpServletRequest request){
        String token = UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token,"没有登录");
        List<RecordDTO>  list= recordService.selectRecord(problemId, token);
        return new ResultJson().ok("查询到这个题解记录",null,list);
    }
    @GetMapping("/record")
    public ResultJson getUserRecords( HttpServletRequest request){
        String token = UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token,"未登录");
        List<RecordDTO> records = recordService.selectRecord(token);
        return new ResultJson().ok("查询到做过的题解",null,records);
    }

}
