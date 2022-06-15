package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.model.ResultJson;
import com.application.model.entity.Sign;
import com.application.service.OtherService;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.format.SignStyle;

@RestController
public class OtherController {
    @Autowired
    private OtherService otherService;
    @GetMapping("/sign/{id}")
    public ResultJson sign(@PathVariable("id") Long id, HttpServletRequest request){
        String token = UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token,"未登录");

            Sign sign=new Sign();
            sign.setId(id);
            sign = otherService.signIn(sign);
            ResultJson<Sign> json=new ResultJson<>();
            json.ok("签到成功", sign);
            return json;
    }
}
