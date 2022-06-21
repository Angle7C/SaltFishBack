package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.mapper.UserMapper;
import com.application.model.DTO.ProblemDTO;
import com.application.model.ResultJson;
import com.application.model.entity.Problem;
import com.application.service.ProblemService;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("Admin")
public class AdminProblemController {
    @Autowired
    private ProblemService problemService;
    @Autowired
    private UserMapper userMapper;
//    @PutMapping("/problem")
//    public ResultJson updateProblem(@RequestBody ProblemDTO problemDTO, HttpServletRequest request){
//
//    }
//    @DeleteMapping("/problem/{id}")
//    public ResultJson delProblem(@PathVariable("id") Long id, HttpServletRequest request){
//
//    }
}
