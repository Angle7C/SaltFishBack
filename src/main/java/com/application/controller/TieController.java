package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.model.DTO.TieDTO;
import com.application.model.ResultJson;
import com.application.model.entity.Review;
import com.application.service.NoticeService;
import com.application.service.ReviewService;
import com.application.service.TieService;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TieController {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private TieService tieService;
    @PostMapping("/revice")
    @Transactional
    public ResultJson pushRevice(Review review, HttpServletRequest request){
        String s = UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(s,"没有登录");
        Pattern compile = Pattern.compile("(?<=@)[^ ]+");
        Matcher matcher = compile.matcher(review.getContent());
        noticeService.sendMessage(review,matcher);
        reviewService.create(review);
        return new ResultJson().ok("发送评论成功");
    }
    @GetMapping("/review/{tieId}")
    public ResultJson getRevice(@PathVariable("tieId") Long titleId){
        TieDTO tieDTO = reviewService.selectById(titleId);
        return new ResultJson().ok("查找帖子下的评论成功",tieDTO);
    }
    @GetMapping("tie")
    public ResultJson getTieAll(){
        List<TieDTO> all = tieService.getAll();
        return new ResultJson().ok("查到所有帖子",null,all);
    }
    @DeleteMapping("/review")
    public ResultJson delRevice(@RequestBody List<Long> list, HttpServletRequest request){
        Assert.isTrue(UserTokenUtils.checkAdmin(request.getCookies()),"管理员未登录");
        reviewService.delReview(list);
        return new ResultJson().ok("删除成功");
    }


}
