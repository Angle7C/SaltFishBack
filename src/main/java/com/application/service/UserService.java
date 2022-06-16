package com.application.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import com.application.mapper.RecordMapper;
import com.application.mapper.UserMapper;
import com.application.model.entity.RecordExample;
import com.application.model.entity.User;
import com.application.model.entity.UserExample;
import com.application.utils.LogUtil;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecordMapper recordMapper;
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
        Assert.notNull(user.getToken());
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(user.getToken());
        int i=userMapper.updateByExampleSelective(user, userExample);
        Assert.isTrue(i==1,"没有这个用户");
        return i;
    };
    @Transactional
    public int UpdatePassWord(User user,String wxId){
        Assert.notNull(user.getToken());
        UserExample userExample=new UserExample();
        userExample.createCriteria().andUserNameEqualTo(user.getUserName())
                .andPassWordEqualTo(user.getPassWord());
        user.setWxId(wxId);
        int i=userMapper.updateByExampleSelective(user, userExample);
        Assert.isTrue(i==1,"没有这个用户");
        return i;
    }
    public User checkUser(String token) {
        Assert.isTrue(UserTokenUtils.checkUser(token),"没有登录");
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(token);
        List<User> users = userMapper.selectByExample(userExample);
        return users.get(0);
    }
    @Transactional
    public void logOut(String token){
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(token);
        List<User> users = userMapper.selectByExample(userExample);
        Assert.isTrue(users.size()==1,"账号或密码错误");
        User user = users.get(0);
        UserTokenUtils.removeToken(user.getToken());
        user.setToken(null);
        userMapper.updateByPrimaryKey(user);
    }
    @Transactional
    public String logIn(User user) {
        UserExample userExample=new UserExample();
        userExample.createCriteria().andPassWordEqualTo(user.getPassWord())
                        .andUserNameEqualTo(user.getUserName());
        List<User> users = userMapper.selectByExample(userExample);
        Assert.isTrue(users.size()==1,"账号或密码错误");
        user = users.get(0);
        String s = UUID.randomUUID().toString();
        user.setToken(s);
        userMapper.updateByPrimaryKey(user);
        UserTokenUtils.addToken(user.getToken());
        return s;
    }
    //删除这个用户，并删除这个用户做题记录，
    public User delete(Long id){
        User user = userMapper.selectByPrimaryKey(id);
        Assert.notNull(user,"没有这个用户");
        RecordExample recordExample=new RecordExample();
        recordExample.createCriteria().andUserIdEqualTo(id);
        int i=recordMapper.deleteByExample(recordExample);
        LogUtil.info("删除了用户:{} 影响了:{}条record记录",user,i);
        userMapper.deleteByPrimaryKey(id);
        return user;
    }

    public User getUser(String token) {
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(token);
        List<User> users = userMapper.selectByExample(userExample);
        Assert.isTrue(users!=null&&users.size()==1,"没有这个用户");
        return users.get(0);
    }

    public User checkUserWx(String id) {
        UserExample userExample=new UserExample();
        userExample.createCriteria().andWxIdEqualTo(id);
        List<User> users = userMapper.selectByExample(userExample);
        Assert.isTrue(users!=null&&users.size()==1,"没有绑定这个微信");
        return users.get(0);
    }
}
