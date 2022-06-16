package com.application.model.entity;

import cn.hutool.http.HttpUtil;

import com.application.inter.Oauth;
import com.application.utils.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class GiteeOauth implements Oauth {
    private static final String tokenUrl ="https://gitee.com/oauth/token?grant_type=authorization_code&code=%s&client_id=%s&redirect_uri=%s&client_secret=%s";
    private static final String userUrl="https://gitee.com/api/v5/user?access_token=%s";
    private static final String clientId="533270564bc0c9d9e10c23c6f802f05e2534513612651c01d5692139536492e7";
    private static final String redirectUrl="http://localhost:8081/backCall/Gitee";
    private static final String clientSecret="e13186384d6ff6c7b92c44fa610180cb215fca61ed50920431235355a5f00458";
    private static final Map<String,Object> map=new HashMap<>();
    static {
        map.put("client_id",clientId);
        map.put("redirect_uri",redirectUrl);
        map.put("client_secret",clientSecret);
    }
    @Getter
    private String code=null;
    @Setter
    @Getter
    private String token=null;
    public  void setCode(String code){
        this.code=code;
    }
    public String getToken(String str){
        return JsonUtil.getAttribute(str,"access_token");
    }
    @Override
    public String getTokenUrl() {
        if(code==null) throw new IllegalStateException("没有获得code");
        return getTokenUrl(code,clientId,redirectUrl,clientSecret);
    }

    @Override
    public String getAvatar(String str) {
        return JsonUtil.getAttribute(str,"avatar_url");
    }

    @Override
    public String getLogin(String str) {
        return JsonUtil.getAttribute(str,"login");
    }

    @Override
    public String getName(String str) {
        return JsonUtil.getAttribute(str,"name");
    }

    @Override
    public String getId(String str) {
        return JsonUtil.getAttribute(str,"id");
    }

    @Override
    public String getType() {
        return "Gitee";
    }

    @Override
    public String getUserUrl() {
        return String.format(userUrl,token);
    }

    @Override
    public String sendTokenUrl() {
        return HttpUtil.post(getTokenUrl(),getBody());
    }

    @Override
    public String sendUserUrl() {
        return HttpUtil.get(getUserUrl());
    }

    @Override
    public Map<String, Object> getBody() {
        return map;
    }

    @Override
    public User changToUser(String userInfo) {

        User user=new User();

        return user;

    }
    public String getTokenUrl(String code, String clientId, String redirect, String clientSecret) {
        return String.format(tokenUrl,code,clientId,redirect,clientSecret);
    }
}
