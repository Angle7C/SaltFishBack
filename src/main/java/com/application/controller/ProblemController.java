package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.mapper.UserMapper;
import com.application.model.DTO.ProblemDTO;
import com.application.model.ResultJson;
import com.application.model.entity.Problem;
import com.application.model.subentity.Page;
import com.application.service.ProblemService;
import com.application.utils.UserTokenUtils;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProblemController {
    @Autowired
    private ProblemService problemService;
    @Autowired
    private UserMapper userMapper;
    @PutMapping("/problem")
    public ResultJson createProblem(@RequestBody ProblemDTO problemDTO, HttpServletRequest request){
        //用户验证
        String s = UserTokenUtils.checkUser(request.getCookies());
        Assert.isTrue(s.equals("Admin"), "管理员用户验证失败");
        Problem problem = problemDTO.toEntity();
        Assert.isTrue(problem.getUserId()!=null,"请设置上传者");
        problem = problemService.create(problem);
        ResultJson json=new ResultJson();
        json.ok("创建问题成功",problem);
        return json;
    }
    @PostMapping("/problem")
    public ResultJson updateProblem(@RequestBody ProblemDTO problemDTO,HttpServletRequest request){
        String s = UserTokenUtils.checkUser(request.getCookies());
        Assert.isTrue(s.equals("Admin"), "管理员用户验证失败");
        Problem problem = problemDTO.toEntity();
        Assert.isTrue(problem.getUserId()!=null,"请设置上传者");
        problemDTO = problemService.update(problem);
        ResultJson json=new ResultJson();
        json.ok("更新问题成功",problemDTO);
        return json;
    }
    @DeleteMapping("/problem/{id}")
    public ResultJson delProblem(@PathVariable("id") Long id,HttpServletRequest request){

        String s = UserTokenUtils.checkUser(request.getCookies());
        Assert.isTrue(s.equals("Admin"), "管理员用户验证失败");
        ProblemDTO delete = problemService.delete(id);
        return new ResultJson().ok("删除成功",delete);
    }
    @GetMapping("problem/{pageSize}/{pageIndex}")
    public ResultJson getProblemList(@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex){
        PageInfo<Problem> pageInfo = problemService.allList(pageSize,pageIndex);
        List<Problem> list = pageInfo.getList();
        List<ProblemDTO> collect = list.stream()
                .map(item -> new ProblemDTO(item, userMapper.selectByPrimaryKey(item.getUserId()) ))
                .collect(Collectors.toList());
        ResultJson json=new ResultJson();
        return  json.ok("查询成功",
                new Page(pageInfo.getTotal(),pageInfo.getPageSize(),pageInfo.getPageNum()),
                collect);
    }
}
