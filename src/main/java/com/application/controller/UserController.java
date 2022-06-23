package com.application.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.extra.mail.MailUtil;
import com.application.model.DTO.UserDTO;
import com.application.model.ResultJson;
import com.application.model.entity.User;
import com.application.service.UserService;
import com.application.utils.EmailUtil;
import com.application.utils.GiteeUtil;
import com.application.utils.LogUtil;
import com.application.utils.UserTokenUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${imagePath}")
    private String imagePath;
    @GetMapping("/checkuser")
    public ResultJson checkLogin(HttpServletRequest request) {
//        ResultJson json = new ResultJson<>();
//        String value = null;
//        //携带了token
//        Cookie[] cookies = request.getCookies();
//        Assert.notNull(cookies,"没有携带Cookie");
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("userToken")) {
//                value = cookie.getValue();
//                break;
//            }
//        }
//        Assert.notNull(value, "没有携带userToken");
//        User user=userService.checkUser(value);

        String s = UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(s,"没有登录");
        User user=userService.getUser(s);
        Assert.notNull(user,  "没有这个用户");
        return new ResultJson().ok("成功登录了",new UserDTO(user));

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
        if(userDTO.getName().equals("Admin")&&userDTO.getPassWord().equals("Admin")){
            Cookie cookie=new Cookie("userToken","Admin");
            cookie.setMaxAge(60*60);
            cookie.setPath("/");
            response.addCookie(cookie);
            return new ResultJson().ok("管理员登录成功");
        }
        String token = userService.logIn(user);
        Cookie cookie=new Cookie("userToken",token);
        cookie.setMaxAge(60*60*24);
        cookie.setPath("/");
        response.addCookie(cookie);
        ResultJson json=new ResultJson();
        json.ok("登录成功",new UserDTO(user));
        return  json;
    }
    @PostMapping("/getcode/{email}")
    public ResultJson generateCode(@PathVariable String email, HttpSession session){
        ResultJson json=new ResultJson();
        String code = EmailUtil.sendEmailCode(email, "LouGu Code");
        code =MD5.create().digestHex(code,"UTF-8");
        session.setAttribute("code", code);
        json.ok("发送验证码成功",code);
        return json;
    }
    @PostMapping("/register/{code}")
    public ResultJson register(@RequestBody UserDTO userDTO,@PathVariable String code,HttpSession session){
        String myCode = (String) session.getAttribute("code");
        System.out.println(myCode);
        code=MD5.create().digestHex(code,"UTF-8");
        System.out.println(code);
        Assert.isTrue(code.equals(myCode),"验证码错误");
        User user = userDTO.toEntity();
        user = userService.create(user);
        ResultJson json=new ResultJson();
        json.ok("注册成功",new UserDTO(user));
        return json;
    }
    @PostMapping("/user")
    public ResultJson update(@RequestParam("id") Long id,
                              HttpServletRequest request,
                             @RequestPart(value = "file")  MultipartFile multipartFile){

        String token = UserTokenUtils.checkUser(request.getCookies());
        String avatarUrl = GiteeUtil.upload(multipartFile,imagePath,id);
        Assert.notNull(avatarUrl,"文件保存失败");
        User user = userService.getUserID(id);
        user.setImageUrl(avatarUrl);
        userService.Update(user);
        ResultJson json = new ResultJson();
        json.ok("更新用户数据成功" , new UserDTO(user));
        return json;
    }
    @PostMapping("/updateuser")
    public ResultJson updateUser(@RequestBody UserDTO userDTO,HttpServletRequest request){
        String s = UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(s,"没有登录");
        User user = userDTO.toEntity();
        user.setToken(s);
        userService.Update(user);
        return new ResultJson().ok("修改成功");
    }

    @GetMapping("/getuser")
    public ResultJson getUserMessage(HttpServletRequest request){
        String token=UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token,"未登录");
        User user=userService.getUser(token);
        return new ResultJson().ok("查询成功",new UserDTO(user));
    }


}
