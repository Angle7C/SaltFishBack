package com.application.controller;

import com.application.model.DTO.YiDengUser;
import com.application.model.ResultJson;
import com.application.service.UserService;
import com.application.service.WxService;
import com.application.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/oauth")
public class OauthController {
    @Autowired
    private WxService wxService;
    @Autowired
    private UserService userService;
    @GetMapping("/wx")
    public ResultJson getYiDeng(){
        ResultJson<Object> result = new ResultJson();
        result.setData(wxService.getWXTempUser());
        return  result;
    }
    @PostMapping("/backcall")
    public Map<String,Object> getUserData(@RequestBody YiDengUser yiDengUser){
        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put("code",0);
        returnMap.put("msg","登录成功，默认密码是123456，为了您的账号安全请及时修改密码");
        try{
            String token = wxService.setWxUserData(yiDengUser);
            RedisUtils.addSet("tokenSet",token);
        }catch (RuntimeException e){
            returnMap.put("code",10001);
            returnMap.put("msg","登录失败或注册异常");
        }
        return  returnMap;
    }


}
