package com.application.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import com.application.mapper.UserMapper;
import com.application.model.entity.User;
import com.application.model.entity.UserExample;
import com.application.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Transactional
    public User create(User user){
        UserExample userExample=new UserExample();
        userExample.createCriteria().andUserNameEqualTo(user.getUserName());
        List<User> users = userMapper.selectByExample(userExample);
        System.out.println(users.size());
        Assert.isTrue(users.size()==0,"账号重复");
        userMapper.insert(user);
        return user;
    }
    @Transactional
    public int Update(User user){
        Assert.isTrue(user.getId()!=null,"没有传输ID");
        int i= userMapper.updateByPrimaryKey(user);
        Assert.isTrue(i==1,"没有这个用户");
        return i;
    };
    public User checkUser(String token) {
        UserExample userExample=new UserExample();
        userExample.createCriteria().andWxIdEqualTo(token);
        List<User> users = userMapper.selectByExample(userExample);
        Assert.isTrue(users.size()==1,"携带token错误");
        return users.get(0);
    }
    public void logOut(String token){
        UserExample userExample=new UserExample();
        userExample.createCriteria().andWxIdEqualTo(token);
        List<User> users = userMapper.selectByExample(userExample);
        Assert.isTrue(users.size()==1,"账号或密码错误");
        User user = users.get(0);
        String s = UUID.randomUUID().toString();
        user.setWxId(null);
        userMapper.updateByPrimaryKey(user);
    }

    public String logIn(User user) {
        UserExample userExample=new UserExample();
        userExample.createCriteria().andPassWordEqualTo(user.getPassWord())
                        .andUserNameEqualTo(user.getUserName());
        List<User> users = userMapper.selectByExample(userExample);
        Assert.isTrue(users.size()==1,"账号或密码错误");
        user = users.get(0);
        String s = UUID.randomUUID().toString();
        user.setWxId(s);
        userMapper.updateByPrimaryKey(user);
        return s;
    }
}
