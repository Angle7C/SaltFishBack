package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.mapper.UserMapper;
import com.application.model.DTO.ProblemDTO;
import com.application.model.ResultJson;
import com.application.model.entity.Problem;
import com.application.service.ProblemService;
import com.application.utils.UserTokenUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("Admin")
public class AdminProblemController {
    @Autowired
    private ProblemService problemService;
    @Autowired
    private UserMapper userMapper;
    @PutMapping("/problem")
    public ResultJson updateProblem(@RequestBody ProblemDTO problemDTO, HttpServletRequest request){
        Assert.isTrue(UserTokenUtils.checkAdmin(request.getCookies()),"管理员没有登陆");
        Problem problem = problemDTO.toEntity();
        problemService.create(problem);
        return  new ResultJson().ok("添加问题");
    }

    @DeleteMapping("/problem/{id}")
    public ResultJson delProblem(@PathVariable("id") Long id, HttpServletRequest request){
        Assert.isTrue(UserTokenUtils.checkAdmin(request.getCookies()),"管理员没有登陆");
        List<Problem> problem = problemService.getProblem(id);
        return new ResultJson().ok("特定问题查询成功");

    }
    @GetMapping("/problem")
    public ResultJson getAllProblem(HttpServletRequest request){
        Assert.isTrue(UserTokenUtils.checkAdmin(request.getCookies()),"管理员没有登陆");
        List<ProblemDTO>  list= problemService.allList();
        return new ResultJson().ok("查询所有问题",null,list);
    }
    @PostMapping("/problem")
    public ResultJson postProblem(@RequestBody ProblemDTO problemDTO,HttpServletRequest request){
        Assert.isTrue(UserTokenUtils.checkAdmin(request.getCookies()),"管理员没有登陆");
        problemService.update(problemDTO.toEntity());
        return new ResultJson().ok("修改成功");
    }
}
