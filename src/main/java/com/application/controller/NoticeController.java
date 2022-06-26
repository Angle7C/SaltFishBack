package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.model.DTO.NoticeDTO;
import com.application.model.ResultJson;
import com.application.model.entity.User;
import com.application.service.NoticeService;
import com.application.service.UserService;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@RestController

public class NoticeController {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserService userService;
    @GetMapping("/notice")
    public ResultJson getNoticeList(HttpServletRequest request){
        String token = UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token,"没有登录");
        User user = userService.getUser(token);
        List<NoticeDTO> list=noticeService.getNotices(user.getId());
        return new ResultJson().ok("查询通知成功",null,list);
    }
    @GetMapping("/notice/{id}")
    public ResultJson getNoticeList(@PathVariable("id") Long id, HttpServletRequest request){
        String token = UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token,"没有登录");
        User user = userService.getUser(token);
        List<NoticeDTO> list=noticeService.getNotices(user.getId());
        return new ResultJson().ok("查询通知成功",null,list);
    }

}
