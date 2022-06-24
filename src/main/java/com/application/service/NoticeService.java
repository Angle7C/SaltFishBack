package com.application.service;

import com.application.mapper.NoticeMapper;
import com.application.mapper.ReviewMapper;
import com.application.mapper.UserMapper;
import com.application.model.DTO.NoticeDTO;
import com.application.model.DTO.UserDTO;
import com.application.model.entity.*;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Service
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
        boolean b = matcher.find();
        if(b==true){
            for(int i=0;i<=matcher.groupCount();i++){
                names.add(matcher.group(i));
            }
            UserExample userExample=new UserExample();
            userExample.createCriteria().andUserNameIn(names);

            List<User> users = userMapper.selectByExample(userExample);
            List<Notice> notices=new LinkedList<>();
            users.stream().parallel().forEach(item->{
                notices.add(new Notice(null,item.getId(),revice.getUserId(), revice.getId(), 0));
            });
            insert(notices);
        }
//        reviewMapper.insert(revice);
    }
    private void insert(List<Notice> list){
        list.forEach(item->noticeMapper.insert(item));
    }

    public List<NoticeDTO>  getNotices(Long id) {
        NoticeExample noticeExample = new NoticeExample();
        noticeExample.createCriteria().andReciveIdEqualTo(id);
        List<Notice> notices = noticeMapper.selectByExample(noticeExample);
        List<NoticeDTO> collect = notices.stream()
                .map(item -> new NoticeDTO(item))
                .collect(Collectors.toList());
        return collect;
    }
}
