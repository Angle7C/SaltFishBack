package com.application.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.digest.MD5;
import enums.UserToken;
import com.application.model.DTO.UserDTO;
import com.application.model.ResultJson;
import com.application.model.entity.User;
import com.application.service.ProblemService;
import com.application.service.RecordService;
import com.application.service.UserService;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/Admin")
public class AdminUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private RecordService recordService;
    @PostMapping("/login")
    public ResultJson loginAdmin(@RequestBody  UserDTO userDTO, HttpServletResponse response){
        String name = userDTO.getName();
        String passWord = userDTO.getPassWord();
        Assert.isTrue("Admin".equals(name),"密码或账号错误");
        Assert.notNull(passWord,"密码或账号错误");
        passWord = MD5.create().digestHex(passWord, "UTF-8");
        Assert.isTrue(UserToken.Admin_TOKEN.getMessage().equals(passWord),"密码或账号错误");
        Cookie cookie = new Cookie("userToken",UserToken.Admin_TOKEN.getMessage());
        cookie.setMaxAge(60*60*24);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new ResultJson().ok("管理员登陆成功");
    }
    @PostMapping("/user")
    public ResultJson updateUser(HttpServletRequest request,@RequestBody  UserDTO userDTO){
        Assert.isTrue(UserTokenUtils.checkAdmin(request.getCookies()),"管理员没有登陆");
        User user = userDTO.toEntity();
        userService.UpdateAdmin(user);
        return new ResultJson().ok("修改用户信息成功");
    }
    @DeleteMapping("/user/{id}")
    public ResultJson delUser(@PathVariable("id") Long id,HttpServletRequest request){
        boolean b = UserTokenUtils.checkAdmin(request.getCookies());
        Assert.isTrue(b,"管理员未登录");
        User user=userService.delete(id);
        return new ResultJson().ok("删除成功");
    }
    @GetMapping("/user/{id}")
    public ResultJson getUser(@PathVariable("id") Long id,HttpServletRequest request){
        Assert.isTrue(UserTokenUtils.checkAdmin(request.getCookies()),"管理员没有登陆");
        User user=userService.getUserID(id);
        return new ResultJson().ok("获取用户信息成功",new UserDTO(user));
    }
    @PutMapping("/user")
    public ResultJson addUser(@RequestBody UserDTO userDTO,HttpServletRequest request){
        Assert.isTrue(UserTokenUtils.checkAdmin(request.getCookies()),"管理员没有登陆");
        User user = userService.create(userDTO.toEntity());
        return new ResultJson<>().ok("添加一个新用户成功",new UserDTO(user));
    }
    @GetMapping("/user/{name}")
    public ResultJson getUser(@PathVariable("name") String name,HttpServletRequest request){
        Assert.isTrue(UserTokenUtils.checkAdmin(request.getCookies()),"管理员没有登陆");
        List<UserDTO> list= userService.selectName(name);
        return new ResultJson().ok("获取用户信息成功",null,list);
    }
    @GetMapping("/user")
    public ResultJson getUser(HttpServletRequest request){
        Assert.isTrue(UserTokenUtils.checkAdmin(request.getCookies()),"管理员没有登陆");
        List<UserDTO> list= userService.findAll();
        return new ResultJson().ok("获取用户信息成功",null,list);
    }
    @GetMapping("/checkuser")
    public ResultJson checkAdmin(HttpServletRequest request){
        boolean b = UserTokenUtils.checkAdmin(request.getCookies());
        Assert.isTrue(b,"管理员没有登录");
        return new ResultJson().ok("已经登录");
    }


}
