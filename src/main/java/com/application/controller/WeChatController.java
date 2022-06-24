package com.application.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import com.application.model.DTO.UserDTO;
import com.application.model.ResultJson;
import com.application.model.entity.User;
import com.application.model.entity.WxUser;
import com.application.service.WxService;
import com.application.utils.ImageUtil;
import com.application.utils.UserTokenUtils;
import com.application.utils.WxStateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class WeChatController {
    @Autowired
    private WxService wxService;
    @Value("${wx" +
            "login}")
    private String url;
    @GetMapping("/wxAction")
    public String handleWxCheckSignature(HttpServletRequest request){
        String echostr=request.getParameter("echostr");
        return echostr;
    }

    @GetMapping("/getwx")
    public void checkLogin(HttpServletResponse response, HttpSession session){
        System.out.println(session.getId());
        String state= RandomUtil.randomString(20);
        wxService.getWxPng(response,state);
        session.setAttribute("state",state);
        WxStateUtil.addState(state, TimeUnit.SECONDS.toMillis(300),0L);
    }
    @GetMapping("/wxlogin")
    public void getQrCode(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                String code,
                                String state) throws IOException {
       UserDTO userDTO=wxService.getWxAccessToken(code,state);
       WxStateUtil.changeSate(state,userDTO.getId(),TimeUnit.SECONDS.toMillis(300));
        PrintWriter writer = response.getWriter();
        writer.write("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "    <img src=\"http://1.15.115.226/img/login_success.png\" style=\"width:100%\">\n" +
                "</body>\n" +
                "</html>");
        writer.close();

    }
    @GetMapping("/checkwxuser")
    public ResultJson handleWxEvent(HttpServletRequest request,HttpSession session,HttpServletResponse response){
        System.out.println(session.getId());
        String state = (String) session.getAttribute("state");
        Long value = WxStateUtil.checkState(state);
        Assert.notNull(value,"登录失败");
        if(value!=0) {
            User user = wxService.getUser(value);
            Cookie userToken = new Cookie("userToken", user.getToken());
            userToken.setMaxAge(60*60*24);
            response.addCookie(userToken);
            return new ResultJson().ok("微信登录成功", new UserDTO(user));
        }else
            return new ResultJson().error(1001L,"等待登录");
    }
    @PostMapping("wxloginpro")
    public ResultJson wxlogin(String openId, @RequestBody WxUser wxUser,HttpSession session,HttpServletResponse response){
        System.out.println(session.getId());
        User user = wxService.bindUser(openId, wxUser);
        response.addCookie(new Cookie("userToken",user.getToken()));
//        UserTokenUtils.addToken(user.getToken());
        return new ResultJson().ok("微信小程序登录成功",user);
    }

}
