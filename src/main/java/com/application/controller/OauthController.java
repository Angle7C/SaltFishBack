package com.application.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import com.application.aspect.UtilLog;
import com.application.inter.Oauth;
import com.application.model.DTO.UserDTO;
import com.application.model.ResultJson;
import com.application.model.entity.GiteeOauth;
import com.application.model.entity.User;
import com.application.service.UserService;
import com.application.utils.LogUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@RestController
public class OauthController {
   @Autowired
    private UserService userService;
   @GetMapping("/checkuser/{wxId}")
   public ResultJson checkUser(@PathVariable("wxId") String wxId){
        User  user=userService.checkUserWx(wxId);
        return new ResultJson().ok("存在这个用户",new UserDTO(user));
   }
   @PostMapping("/bindWx/{wxId}")
   public ResultJson bindWx(@PathVariable("wxId") String wxId,@RequestBody UserDTO userDTO){
       User user = userDTO.toEntity();
       user.setWxId(wxId);
       userService.UpdatePassWord(user,wxId);
       return new ResultJson().ok("绑定用户成功", new UserDTO(user));
   }

}
