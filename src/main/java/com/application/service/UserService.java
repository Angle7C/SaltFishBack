package com.application.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import com.application.mapper.RecordMapper;
import com.application.mapper.UserMapper;
import com.application.model.DTO.UserDTO;
import com.application.model.entity.Record;
import com.application.model.entity.RecordExample;
import com.application.model.entity.User;
import com.application.model.entity.UserExample;
import com.application.utils.LogUtil;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Value("${output}")
    private String compilePath;
    @Transactional
    public User create(User user){
        UserExample userExample=new UserExample();
        userExample.createCriteria()
                .andUserNameEqualTo(user.getUserName());
        List<User> users = userMapper.selectByExample(userExample);
        Assert.isTrue(users.size()==0,"账号重复");
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        userMapper.insert(user);
        return user;
    }
    //不更新密码rank
    @Transactional
    public int Update(User user){
        Assert.notNull(user.getToken());
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(user.getToken());
        user.setGmtModified(System.currentTimeMillis());
        User userTemp = userMapper.selectByPrimaryKey(user.getId());
        user.setPassWord(userTemp.getPassWord());
        user.setRanks(userTemp.getRanks());
        int i=userMapper.updateByExampleSelective(user, userExample);
        Assert.isTrue(i==1,"没有这个用户");
        return i;
    };
    public void UpdateUserID(User user){
        user.setGmtModified(System.currentTimeMillis());
        User userTemp = userMapper.selectByPrimaryKey(user.getId());
        userTemp.setDecription(user.getDecription());
        userMapper.updateByPrimaryKey(userTemp);
    }
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
        List<Record> records = recordMapper.selectByExample(recordExample);
        //删除代码文件
        records.stream().forEach(item->{
            String path=item.getPath().substring(0,item.getPath().indexOf(File.separator)+1);
            File file=new File(path+path+item.getUserId()+"_source.c");
            file.delete();
            recordMapper.deleteByPrimaryKey(item.getId());
        });
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


    public User getUserID(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        Assert.notNull(user,"没有这个用户");
        return user;
    }

    public List<UserDTO> selectName(String name) {
        UserExample userExample=new UserExample();
        userExample.createCriteria().andUserNameLike(name);
        List<User> users = userMapper.selectByExample(userExample);
        List<UserDTO> collect =
                users.stream()
                        .map(item -> new UserDTO(item))
                        .collect(Collectors.toList());
        return collect;
    }
}
