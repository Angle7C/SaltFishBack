package com.application.utils;

import cn.hutool.core.lang.Assert;
import enums.UserToken;

import javax.servlet.http.Cookie;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserTokenUtils {
    private static Set<String> tokenSession=new HashSet<>();
    public  static String checkUser( Cookie[] cookies){
        Assert.notNull(cookies,"没有携带Cookie");
        List<Cookie> userToken = Arrays.stream(cookies)
                .filter(item -> item.getName().equals("userToken"))
                .collect(Collectors.toList());
        Assert.isTrue(userToken!=null||userToken.size()>=1,"携带userToken出错");
        tokenSession.contains(userToken.get(0).getValue());
        return userToken.get(0).getValue();
    }
    public static boolean checkUser(@NotNull(message = "没有携带userToken")String token){
        boolean contains = tokenSession.contains(token);
        return contains;
    }
    public static boolean addToken(String token){
        return tokenSession.add(token);
    }
    public static boolean removeToken(String token){
        return tokenSession.remove(token);
    }


    public static boolean checkAdmin(Cookie[] cookies) {
        Assert.notNull(cookies,"没有携带Cookie");
        List<Cookie> userToken = Arrays.stream(cookies)
                .filter(item -> item.getName().equals("userToken"))
                .collect(Collectors.toList());
        Assert.isTrue(userToken!=null||userToken.size()!=1,"携带userToken出错");
        return userToken.get(0).getValue().equals(UserToken.Admin_TOKEN.getMessage());

    }
}
