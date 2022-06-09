package com.application.model.DTO;


import lombok.Data;
@Data
public class YiDengUser {

    private String tempUserId;
    private WxMaUserInfo wxMaUserInfo;
    @Data
    public class WxMaUserInfo{
        private String openId;
        private String nickName;
        private Integer gender;
        private String avatarUrl;
    }
}

