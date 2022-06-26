package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.model.DTO.CommentDTO;
import com.application.model.ResultJson;
import com.application.service.CommentService;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Admin")
public class AdminCommentController {
    @Autowired
    private CommentService commentService;
    //删除题解
    @DeleteMapping("/comment/{id}")
    public ResultJson deleteComment(HttpServletRequest request, @PathVariable("id") Long id) {
        ResultJson json = new ResultJson();
        Boolean b = UserTokenUtils.checkAdmin(request.getCookies());
        Assert.isTrue( b, "没有登录");
        CommentDTO commentDTO = commentService.deleteComment(id);
        return json.ok("删除题解成功", commentDTO);
    }
}
