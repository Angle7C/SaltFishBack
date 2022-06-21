package com.application.service;

import com.application.mapper.NoticeMapper;
import com.application.mapper.ReviewMapper;
import com.application.mapper.UserMapper;
import com.application.model.entity.Notice;
import com.application.model.entity.Review;
import com.application.model.entity.User;
import com.application.model.entity.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ReviewMapper reviewMapper;
    @Transactional
    public void sendMessage(Review revice, Matcher matcher) {
        Long userId = revice.getUserId();
        List<String> names=new LinkedList<>();
        for(int i=0;i<matcher.groupCount();i++){
            names.add(matcher.group(i));
        }
        UserExample userExample=new UserExample();
        userExample.createCriteria().andUserNameIn(names);

        List<User> users = userMapper.selectByExample(userExample);
        List<Notice> notices=new LinkedList<>();
        users.stream().parallel().forEach(item->{
            notices.add(new Notice(null,item.getId(),revice.getUserId(),0));
        });
        insert(notices);
        reviewMapper.insert(revice);
    }
    private void insert(List<Notice> list){
        list.forEach(item->noticeMapper.insert(item));
    }
}
