package com.application.model.entity;

import cn.hutool.crypto.digest.MD5;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class WxUser {
    private String openid;
    private String nickname;
    private String sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private List<String> privilege;
//    private String unionid;
    public static User toUser(User user,WxUser wxUser){
        user.setWxId(wxUser.getOpenid());
        user.setImageUrl(wxUser.getHeadimgurl());
        user.setToken(UUID.randomUUID().toString());
        user.setUserName(wxUser.getNickname());
        user.setDecription(null);
        user.setRanks(0L);
        user.setPassWord(MD5.create().digestHex("123456","UTF-8"));
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());;
        return user;
    }
}
