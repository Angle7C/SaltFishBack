package com.application.controller;
import cn.hutool.core.lang.Assert;
import com.application.model.DTO.RecordDTO;
import com.application.model.ResultJson;
import com.application.model.subentity.CodeC;
import com.application.service.RecordService;
import com.application.utils.LogUtil;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
@RestController
public class JudgeController {

    @Autowired
    private RecordService recordService;


    @PostMapping("/submit/{id}")
    public ResultJson subCompiler(@PathVariable("id") Long id, @RequestBody CodeC code, HttpServletRequest request) {
        String token = UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token, "用户未登录");
        try {
            RecordDTO recordDTO = recordService.addRecord(id, token, code);
            recordDTO = recordService.runProcess(recordDTO);
            return new ResultJson().ok("编译完成,运行成功", recordDTO);
        } catch (IOException e) {
            LogUtil.info("创建文件时失败:{}", e.getMessage());

        } catch (InterruptedException e) {
            LogUtil.info("编译失败:{}", e.getMessage());
        }
        return new ResultJson().error(2003L, "服务器异常，请重试", null);
    }

    @PostMapping("getans/{id}")
    public ResultJson getAns(@PathVariable("id") Long id) {
        RecordDTO recordDTO = recordService.selectRecord(id);
        return new ResultJson().ok("查询成功", recordDTO);
    }
}
