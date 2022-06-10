package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.model.DTO.CommentDTO;
import com.application.model.ResultJson;
import com.application.model.entity.Comment;
import com.application.service.CommentService;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Validated
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PutMapping("/comment/{id}")
    public ResultJson pushComment(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody CommentDTO commentDTO){
           ResultJson json=new ResultJson();
           Assert.isTrue(UserTokenUtils.checkUser(request.getCookies()),"没有登录");
           Assert.notNull(commentDTO,"传输数据异常");
           Comment comment = commentDTO.toEntity();
           commentService.pushComment(comment);
           json.ok("发表题解成功");
           return json;
    }

}
