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
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/checkuser")
    public ResultJson checkLogin(HttpServletRequest request) {
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
    public ResultJson update( UserDTO userDTO,HttpServletRequest request,@RequestParam(value = "file",required = false) MultipartFile multipartFile){
        String token = UserTokenUtils.checkUser(request.getCookies());
        User user = userDTO.toEntity();
        user.setWxId(token);
        String avatarUrl = GiteeUtil.upload(multipartFile);
        LogUtil.debug("传输用户数据：{}",user);
        LogUtil.debug("文件名称: {}",multipartFile.getOriginalFilename());
        user.setImageUrl(avatarUrl);
        userService.Update(user);
        ResultJson json = new ResultJson();
        json.ok("更新用户数据成功",new UserDTO(user));
        return json;
    }
    @DeleteMapping("/user/{id}")
    public ResultJson delUser(@PathVariable("id") Long id,HttpServletRequest request){
        String str = UserTokenUtils.checkUser(request.getCookies());
        Assert.isTrue(str.equals("Admin"),"管理员未登录");
        User user=userService.delete(id);
        return new ResultJson().ok("删除成功");
    }
    @PutMapping("/user")
    public ResultJson addUser(@RequestBody UserDTO userDTO,HttpServletRequest request){
        String token = UserTokenUtils.checkUser(request.getCookies());
        Assert.isTrue(token.equals("Admin"),"管理员未登录");
        User user = userService.create(userDTO.toEntity());
        return new ResultJson<>().ok("添加一个新用户成功",user);
    }
    @GetMapping("/getuser")
    public ResultJson getUserMessage(HttpServletRequest request){
        String token=UserTokenUtils.checkUser(request.getCookies());
        Assert.notNull(token,"未登录");
        User user=userService.getUser(token);
        return new ResultJson().ok("查询成功",new UserDTO(user));
    }


}
