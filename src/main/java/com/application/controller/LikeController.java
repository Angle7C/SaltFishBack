package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.model.DTO.LikeDTO;
import com.application.model.ResultJson;
import com.application.model.entity.Likes;
import com.application.model.entity.User;
import com.application.service.LikesService;
import com.application.service.UserService;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LikeController {
    @Autowired
    private UserService userService;
    @Autowired
    private LikesService likesService;

    @PostMapping("/addlikecomment")
    public ResultJson addLike(@RequestBody LikeDTO likesDTO, HttpServletRequest request){
        String token = UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token,"用户未登录");
        boolean b =false;
        if(likesDTO.getId()==null){
            User user=userService.getUser(token);
            b=likesService.addLikeComment(user.getId(), likesDTO.getCommentId());
        }else{
            b=likesService.addLikeComment(likesDTO.getUserId(), likesDTO.getCommentId());
        }
        return new ResultJson().ok(b?"点赞成功":"取消点赞");
    }
    @PostMapping("/addlikereview")
    public ResultJson addLikes(@RequestBody LikeDTO likesDTO,HttpServletRequest request){
        String token = UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token,"用户未登录");
        boolean b =false;
        if(likesDTO.getId()==null){
            User user=userService.getUser(token);
            b=likesService.addLikeReview(user.getId(), likesDTO.getReviewId());
        }else{
            b=likesService.addLikeReview(likesDTO.getUserId(), likesDTO.getReviewId());
        }
        return new ResultJson().ok(b?"点赞成功":"取消点赞");
    }

}
