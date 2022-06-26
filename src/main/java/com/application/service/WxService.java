package com.application.service;
import cn.hutool.core.lang.Assert;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.application.mapper.UserMapper;
import com.application.model.DTO.UserDTO;
import com.application.model.entity.User;
import com.application.model.entity.UserExample;
import com.application.model.entity.WxUser;
import com.application.utils.ImageUtil;
import com.application.utils.LogUtil;
import com.application.utils.UserTokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class WxService  {
    @Value("${appID}")
    private String appid;
    @Value("${sercet}")
    private String secret;
    @Value("${nat}")
    private String nat;
    @Value("${imagePath}")
    private String image;
    @Autowired
    private UserMapper userMapper;
    private static final String accessTokenUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    private static final String wxUserInfoUrl="https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
    public UserDTO getWxAccessToken(String code, String state) {

        String postUrl=String.format(accessTokenUrl,appid,secret,code);
        String s = HttpUtil.get(postUrl);
        JSONObject json= JSONUtil.parseObj(s);
        String accessToken = json.get("access_token",String.class);
        String openId = json.get("openid",String.class);

        User wxUserInfo = getWxUserInfo(accessToken, openId);
        if(wxUserInfo.getId()==null){
            userMapper.insert(wxUserInfo);
        }
        return new UserDTO(wxUserInfo);
    }
    public User getWxUserInfo(String accessToken,String openId){
       String postUrl=String.format(wxUserInfoUrl,accessToken,openId);
       String checkUrl=String.format("https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s",accessToken,openId);
       String checkAns=HttpUtil.get(checkUrl);
       JSONObject jsonObject = JSONUtil.parseObj(checkAns);
       Integer errcode = jsonObject.get("errcode",Integer.class);
       Assert.isTrue(errcode==0,jsonObject.get("errmsg",String.class));
       String userInfo = HttpUtil.get(postUrl);
       WxUser wxUser = JSONUtil.toBean(userInfo,WxUser.class);
       openId=wxUser.getOpenid();
       return bindUser(openId,wxUser);
    }
    @Transactional
    public User bindUser(String openId,WxUser wxUser){
        UserExample userExample=new UserExample();
        userExample.createCriteria().andWxIdEqualTo(openId);
        List<User> users = userMapper.selectByExample(userExample);
        if(users!=null&&users.size()==0){
            LogUtil.info("微信没有绑定");
            User user = new User();
            userMapper.insert(user);
            WxUser.toUser(user,wxUser,image+ File.separator+user.getId()+File.separator+ UUID.randomUUID().toString()+".png");
            UserTokenUtils.addToken(openId);
            user.setWxId(openId);
            user.setToken(openId);
            ImageUtil.urlToImage(wxUser.getHeadimgurl(),user.getImageUrl());
            user.setImageUrl(user.getImageUrl());
            userMapper.updateByPrimaryKey(user);
            return user;
        }else{
            LogUtil.info("微信已经绑定");
            UserTokenUtils.addToken(openId);
            User user=users.get(0);
            user.setToken(openId);
            userMapper.updateByPrimaryKey(user);
            return users.get(0);
        }
    }
    public void getWxPng(HttpServletResponse response,String state) {
        ImageUtil.createQRCode2Stream(
                "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect",
                response,appid,nat,state);
    }
    public User getUser(Long id){
        User user = userMapper.selectByPrimaryKey(id);
        Assert.notNull("没有这个用户");
        return user;
    }
}
