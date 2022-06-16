package com.application.inter;

import com.application.model.entity.User;
import lombok.AllArgsConstructor;

import java.util.Map;

public interface Oauth {
    void   setCode(String code);
    void   setToken(String token);
    String getToken(String reponse);
    String getToken();
    String getTokenUrl();
    String getAvatar(String str);
    String getLogin(String str);
    String getName(String str);
    String getId(String Id);
    String getType();
    String getUserUrl();
    String sendTokenUrl();
    String sendUserUrl();
    Map<String,Object> getBody();
    User changToUser(String userInfo);
}
