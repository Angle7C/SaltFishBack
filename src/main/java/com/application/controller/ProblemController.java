package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.mapper.UserMapper;
import com.application.model.DTO.ProblemDTO;
import com.application.model.DTO.SearchDTO;
import com.application.model.DTO.UserDTO;
import com.application.model.ResultJson;
import com.application.model.entity.Problem;
import com.application.model.entity.User;
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

    @GetMapping("problem/{pageSize}/{pageIndex}")
    public ResultJson getProblemList(@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex){
        PageInfo<Problem> pageInfo = problemService.allList(pageSize,pageIndex);
        List<Problem> list = pageInfo.getList();
        List<ProblemDTO> collect = list.stream()
                .map(item -> new ProblemDTO(item,userMapper.selectByPrimaryKey(item.getUserId())))
                .collect(Collectors.toList());
        ResultJson json=new ResultJson();
        return  json.ok("查询成功",
                new Page(pageInfo.getTotal(),pageInfo.getPageSize(),pageInfo.getPageNum()),
                collect);
    }
    @GetMapping("/problem/{id}")
    public ResultJson getProblem(@PathVariable("id") Long id){
//        ProblemDTO problem=problemService.getProblem(id);
        List<Problem> problems = problemService.getProblem(id);
        Problem problem=problems.get(0);
        User user = userMapper.selectByPrimaryKey(problem.getUserId());
        return new ResultJson().ok("查询成功",new ProblemDTO(problem,user));
    }
    @PostMapping("/search/{pageSize}/{pageIndex}")
    public ResultJson search(@RequestBody SearchDTO searchDTO,@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex){
        PageInfo<ProblemDTO> pageInfo = problemService.searchProbelm(searchDTO.getName(), searchDTO.getTag(), searchDTO.getLevel(), pageSize, pageIndex);
        Page page = new Page(pageInfo.getTotal(), pageSize, pageIndex);
        return new ResultJson().ok("查询成功",page,pageInfo.getList());
    }
}
