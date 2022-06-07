package com.application.controller;

import com.application.mapper.RecordMapper;
import com.application.mapper.UserMapper;
import com.application.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("hello")
    public Object hello(){
        Long i=1L;
        int i1=1/0;
        return userMapper.selectByPrimaryKey(i);
    }
}
