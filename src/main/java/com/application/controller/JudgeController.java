package com.application.controller;

import cn.hutool.core.lang.Assert;

import com.application.mapper.RecordMapper;
import com.application.model.DTO.RecordDTO;
import com.application.model.ResultJson;
import com.application.model.entity.Record;
import com.application.model.subentity.CodeC;
import com.application.service.RecordService;
import com.application.utils.LogUtil;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

//import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;
@RestController
public class JudgeController {

    @Autowired
    private RecordService recordService;


    @PostMapping("/submit/{id}")
    public ResultJson subCompiler(@PathVariable("id") Long id, @RequestBody CodeC code,HttpServletRequest request) {
        String token = UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token,"用户未登录");
        try {
            RecordDTO recordDTO = recordService.addRecord(id, token, code);

            return new ResultJson().ok("编译完成，等待运行",recordDTO);
        } catch (IOException e) {
            LogUtil.info("创建文件时失败:{}",e.getMessage());

        } catch (InterruptedException e) {
            LogUtil.info("编译失败:{}",e.getMessage());
        }
        return new ResultJson().error(2003L,"服务器异常，请重试",null);
    }
    @PostMapping("getans/{id}")
    public ResultJson getAns(@PathVariable("id") Long id){
        RecordDTO recordDTO = recordService.selectRecord(id);
        return new ResultJson().ok("查询成功",recordDTO);
    }
}
