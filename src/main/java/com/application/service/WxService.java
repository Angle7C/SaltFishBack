package com.application.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.application.mapper.UserMapper;
import com.application.model.DTO.YiDengUser;
import com.application.model.entity.User;
import com.application.model.entity.UserExample;
import com.application.utils.RedisUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WxService {
    @Autowired
    private UserMapper userMapper;

    public YiDeng getWXTempUser() {
        String beanStr = HttpUtil.get("https://yd.jylt.cc/api/wxLogin/tempUserId?secret=2f040616");
        YiDeng yiDeng = JSONUtil.toBean(beanStr, YiDeng.class);
        Assert.isTrue(0==yiDeng.getCode());
        //将登录token存储到数据库中。来注册用户
//        setWxUser(yiDeng.getData().getTempUserId());
        //将登录token存储
        RedisUtils.addSet("TempIdSet", yiDeng.getData().getTempUserId());
        return yiDeng;
    }
    //创建一个新的用户并登陆或者登录到老用户
    public String setWxUserData(YiDengUser yiDengUser) {
        String openId = yiDengUser.getWxMaUserInfo().getOpenId();
        String tempId = yiDengUser.getTempUserId();
        Assert.isTrue(RedisUtils.existsSetInValue("TempIdSet", tempId));
        RedisUtils.removeSetInValue("tempIdSet",tempId);
        String securityOpenId=MD5.create().digestHex(openId,"UTF-8");
        UserExample userExample=new UserExample();
        userExample.createCriteria().andWxIdEqualTo(securityOpenId);
        List<User> users = userMapper.selectByExample(userExample);
        Integer size=users.size();
        //已经存在用户
        User user = new User();
        if(size==1){
            user=users.get(0);
        }else if(size==0){
            user.setWxId(securityOpenId);
            user.setUserName(yiDengUser.getWxMaUserInfo().getNickName());
            user.setPassWord(MD5.create().digestHex("123456","UTF-8"));
            user.setImageUrl(yiDengUser.getWxMaUserInfo().getAvatarUrl());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setRanks(0L);
            userMapper.insert(user);
        }
        return securityOpenId;

    }
}

@Data
class YiDeng {
    private Integer code;
    private String msg;
    private Data data;

    @lombok.Data
    public class Data {
        private String qrUrl;
        private String tempUserId;
    }
}

