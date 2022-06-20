package com.application.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import com.application.model.DTO.UserDTO;
import com.application.model.ResultJson;
import com.application.model.entity.User;
import com.application.service.WxService;
import com.application.utils.WxStateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@RestController
public class WeChatController {
    @Autowired
    private WxService wxService;
    @Value("${wxlogin}")
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
    public ResultJson getQrCode(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                String code,
                                String state){
       UserDTO userDTO=wxService.getWxAccessToken(code,state);
       WxStateUtil.changeSate(state,userDTO.getId(),TimeUnit.SECONDS.toMillis(300));
//       response.sendRedirect(); 重定向到一个登录成功的界面
        return new ResultJson().ok("微信登录成功",userDTO);
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

}
