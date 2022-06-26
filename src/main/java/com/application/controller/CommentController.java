package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.model.DTO.CommentDTO;
import com.application.model.ResultJson;
import com.application.model.entity.Comment;
import com.application.model.entity.User;
import com.application.model.subentity.Page;
import com.application.service.CommentService;
import com.application.service.UserService;
import com.application.utils.UserTokenUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Validated

public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    //发表题解
    @PutMapping("/comment")
    public ResultJson pushComment(HttpServletRequest request,  @RequestBody CommentDTO commentDTO){
           ResultJson json=new ResultJson();
           String token=UserTokenUtils.checkUser(request.getCookies());
           Assert.notNull(token,"没有登录");
           Assert.notNull(commentDTO,"传输数据异常");
           Comment comment = commentDTO.toEntity();
           comment.setType(0);
           commentDTO = commentService.pushComment(comment, token);
           return json.ok("发表题解成功",commentDTO);
    }
    //修改题解
    @PostMapping("/comment")
    public ResultJson updateComment(HttpServletRequest request,  @RequestBody CommentDTO commentDTO){
        ResultJson json=new ResultJson();
        String token=UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token,"没有登录");
        Assert.notNull(commentDTO,"传输数据异常");
        Comment comment = commentDTO.toEntity();
        commentDTO = commentService.updateComment(comment, token);
        return json.ok("修改题解成功",commentDTO);
    }
    //获取题解
    @GetMapping("/comment/{pageSize}/{pageIndex}/{id}")
    public ResultJson pageList(@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex,@PathVariable("id") Long id){
        PageInfo<CommentDTO> pageInfo=commentService.allList(pageIndex,pageSize,id);
        Page page=new Page(pageInfo.getTotal(),pageSize,pageIndex);
        return new  ResultJson().ok("查询成功",page,pageInfo.getList());
    }
    //获取题解
    @GetMapping("/comment/{id}")
    public ResultJson userForList(@PathVariable("id") Long id){
        List<CommentDTO> list=commentService.getListUser(id);
        return new ResultJson().ok("查询成功",null,list);
    }
    @GetMapping("/commentproblem/{problemId}")
    public ResultJson userProblem(@PathVariable("problemId") Long problemId,@CookieValue("userToken") String value){
        Assert.isTrue(UserTokenUtils.checkUser(value),"没有登录");
        User user = userService.getUser(value);
        Comment comment = commentService.getProblem(user.getId(), problemId);
        return new ResultJson().ok("查询成功",new CommentDTO(comment,user));
    }
    @GetMapping("/commentproblem")
    public ResultJson userProblem( String strId){
        Long id=Long.valueOf(strId);
        List<CommentDTO> listProblem = commentService.getListProblem(id);

        return new ResultJson().ok("问题下的题解",null,listProblem);
    }
}
