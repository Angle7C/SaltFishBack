package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.model.DTO.CommentDTO;
import com.application.model.ResultJson;
import com.application.model.entity.Comment;
import com.application.model.subentity.Page;
import com.application.service.CommentService;
import com.application.utils.UserTokenUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Validated
public class CommentController {
    @Autowired
    private CommentService commentService;

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
    @PostMapping("/comment/{id}")
    public ResultJson updateComment(HttpServletRequest request,  @RequestBody CommentDTO commentDTO){
        ResultJson json=new ResultJson();
        String token=UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token,"没有登录");
        Assert.notNull(commentDTO,"传输数据异常");
        Comment comment = commentDTO.toEntity();
        commentDTO = commentService.updateComment(comment, token);
        return json.ok("发表题解成功",commentDTO);
    }
    @DeleteMapping("/comment/{id}")
    public ResultJson deleteComment(HttpServletRequest request,@PathVariable("id") Long id) {
        ResultJson json = new ResultJson();
        String token = UserTokenUtils.checkUser(request.getCookies());
        Assert.isTrue(token != null && "Admin".equals(token), "没有登录");
        CommentDTO commentDTO = commentService.deleteComment(id);
        return json.ok("发表题解成功", commentDTO);
    }
    @GetMapping("/comment/{pageSize}/{pageIndex}")
    public ResultJson pageList(@PathVariable("id") Integer pageSize,@PathVariable("") Integer pageIndex){
        PageInfo<CommentDTO> pageInfo=commentService.allList(pageIndex,pageSize);
        Page page=new Page(pageInfo.getTotal(),pageSize,pageIndex);
        return new  ResultJson().ok("查询成功",page,pageInfo.getList());
    }
}
