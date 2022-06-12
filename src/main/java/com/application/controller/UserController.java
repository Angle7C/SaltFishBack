package com.application.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.extra.mail.MailUtil;
import com.application.model.DTO.UserDTO;
import com.application.model.ResultJson;
import com.application.model.entity.User;
import com.application.service.UserService;
import com.application.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    //使用Cooike来检查登录状态
    @GetMapping("/checkuser")
    public ResultJson checkLogin(HttpServletRequest request, HttpServletResponse response) {
        ResultJson json = new ResultJson<>();
        String value = null;
        //携带了token
        Cookie[] cookies = request.getCookies();
        Assert.notNull(cookies,"没有携带Cookie");
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userToken")) {
                value = cookie.getValue();
                break;
            }
        }
        Assert.notNull(value, "没有携带userToken");
        User user=userService.checkUser(value);
        Assert.notNull(user,  "没有这个用户");
        json.ok("成功登录了",new UserDTO(user));
        return json;
    }
    @PostMapping("/logout")
    public ResultJson logout(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        request.getSession().invalidate();
        String value=null;
        Assert.notNull(cookies,"没有携带Cookie");
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userToken")) {
                value = cookie.getValue();
                break;
            }
        }
        userService.logOut(value);
        ResultJson json=new ResultJson();
        json.ok("登出成功");
        return json;
    }
    @PostMapping("/login")
    public ResultJson login(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDTO userDTO){
        User user = userDTO.toEntity();
        String token = userService.logIn(user);
        Cookie cookie=new Cookie("userToken",token);
        cookie.setMaxAge(60*60*24);
        cookie.setPath("/");
        response.addCookie(cookie);
        ResultJson json=new ResultJson();
        json.ok("登录成功");
        return  json;
    }
    @GetMapping("/getcode/{email}")
    public ResultJson generateCode(@PathVariable String email, HttpSession session){
        ResultJson json=new ResultJson();
        String code = EmailUtil.setEmailCode("2501741939@qq.com", "测试");
        session.setAttribute("code", MD5.create().digest(code,"UTF-8"));
        json.ok("发送验证码成功",code);
        return json;
    }
    @PostMapping("/register/{code}")
    public ResultJson register(@RequestBody UserDTO userDTO,@PathVariable String code,HttpSession session){
        String myCode = (String) session.getAttribute("code");
        Assert.isTrue(code.equals(myCode),"验证码错误");
        User user = userDTO.toEntity();
        user = userService.create(user);
        ResultJson json=new ResultJson();
        json.ok("注册成功",new UserDTO(user));
        return json;
    }

}
