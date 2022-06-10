package com.application.controller;

import com.application.model.ResultJson;
import com.application.model.entity.Sign;
import com.application.service.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.SignStyle;

@RestController
public class OtherController {
    @Autowired
    private OtherService otherService;
    @GetMapping("/sign/{id}")
    public ResultJson sign(@PathVariable("id") Long id){
            Sign sign=new Sign();
            sign.setId(id);
            sign = otherService.signIn(sign);
            ResultJson<Sign> json=new ResultJson<>();
            json.ok("签到成功", sign);
            return json;
    }
}
