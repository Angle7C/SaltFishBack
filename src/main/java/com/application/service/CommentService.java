package com.application.service;

import cn.hutool.core.lang.Assert;
import com.application.mapper.CommentMapper;
import com.application.mapper.ProblemMapper;
import com.application.mapper.UserMapper;
import com.application.model.DTO.CommentDTO;
import com.application.model.entity.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private UserMapper userMapper;
    @Transactional
    public CommentDTO pushComment(Comment comment,String token) {
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        Assert.notNull(problemMapper.selectByPrimaryKey(comment.getProblemId()),"没有这个问题");
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(token) ;
        List<User> userList= userMapper.selectByExample(userExample);
        Assert.isTrue(userList!=null&&userList.size()==1,"没有上传者");
        User user = userList.get(0);
        comment.setUserId(user.getId());
        commentMapper.insert(comment);
        return new CommentDTO(comment,user);
    }
    @Transactional
    public CommentDTO updateComment(Comment comment, String token) {
        comment.setGmtModified(System.currentTimeMillis());
        UserExample userExample=new UserExample();
        userExample.createCriteria().andTokenEqualTo(token) ;
        List<User> userList= userMapper.selectByExample(userExample);
        Assert.isTrue(userList!=null&&userList.size()==1,"没有上传者");
        User user = userList.get(0);
        Comment commentTemp = commentMapper.selectByPrimaryKey(comment.getId());
        comment.setGmtCreate(commentTemp.getGmtCreate());
        commentMapper.updateByPrimaryKey(comment);
        return new CommentDTO(comment,user);
    }
    @Transactional
    public CommentDTO deleteComment(Long id) {
        Comment comment = commentMapper.selectByPrimaryKey(id);
        Assert.notNull(comment,"没有这个题解");
        User user =userMapper.selectByPrimaryKey(comment.getUserId());
        Assert.notNull(user,"题解没有对应的用户");
        commentMapper.deleteByPrimaryKey(id);
        return new CommentDTO(comment,user);
    }

    public PageInfo<CommentDTO> allList(Integer pageIndex, Integer pageSize,Long id){
        PageHelper.startPage(pageIndex,pageSize);
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria().andProblemIdEqualTo(id).andTypeEqualTo(1);
        List<Comment> comments = commentMapper.selectByExampleWithBLOBs(commentExample);
        List<CommentDTO> collect = comments.stream()
                .map(item -> new CommentDTO(item, userMapper.selectByPrimaryKey(item.getUserId())))
                .collect(Collectors.toList());
        PageInfo<CommentDTO> pageInfo = new PageInfo<>(collect);
        return pageInfo;
    }

    public List<CommentDTO> getListProblem(Long id) {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria()
                .andProblemIdEqualTo(id);
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        List<CommentDTO> collect = comments.stream().map(
                item -> new CommentDTO(item, userMapper.selectByPrimaryKey(item.getUserId()))
        ).map(item->{item.setContent(problemMapper.selectByPrimaryKey(item.getProblemId()).getTitle());
            return item;
        }).collect(Collectors.toList());
        return collect;

    }    public List<CommentDTO> getListUser(Long id) {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria()
                .andUserIdEqualTo(id);
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        List<CommentDTO> collect = comments.stream().map(
                item -> new CommentDTO(item, userMapper.selectByPrimaryKey(item.getUserId()))
        ).map(item->{item.setContent(problemMapper.selectByPrimaryKey(item.getProblemId()).getTitle());
            return item;
        }).collect(Collectors.toList());
        return collect;
    }

    public Comment getProblem(Long userId, Long problemId) {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria().andTypeEqualTo(1)
                .andProblemIdEqualTo(problemId)
                .andUserIdEqualTo(userId);
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        Assert.isTrue(comments.size()>0,"没有这个题解");
        return comments.get(0);
    }
}
