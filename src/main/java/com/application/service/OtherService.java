package com.application.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import com.application.mapper.SignMapper;
import com.application.mapper.UserMapper;
import com.application.model.entity.Sign;
import com.application.model.entity.User;
import com.application.utils.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OtherService {
    @Autowired
    private SignMapper signMapper;
    @Autowired
    private UserMapper userMapper;


    public Sign signIn(Sign sign){
        Sign signTemp= signMapper.selectByPrimaryKey(sign.getId());
        User user = userMapper.selectByPrimaryKey(sign.getId());
        Assert.notNull(user,"没有这个用户");
        Date now = new Date();
        Long i = RandomUtil.randomLong(3);
        if(signTemp!=null) {
            Date temp = signTemp.getTime();
            if ((now.getTime()-temp.getTime())/86400000 > 0) {
                sign= SignUtil.getSign(i,new Date());
                signMapper.updateByPrimaryKeySelective(sign);
            } else {
                sign=signTemp;
            }
        }else{
            sign= SignUtil.getSign(i,new Date());
            sign.setId(user.getId());
            signMapper.insert(sign);
        }
        return sign;

    };
}
