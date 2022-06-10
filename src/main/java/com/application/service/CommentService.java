package com.application.service;

import cn.hutool.core.lang.Assert;
import com.application.mapper.CommentMapper;
import com.application.mapper.ProblemMapper;
import com.application.model.entity.Comment;
import com.application.model.entity.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ProblemMapper problemMapper;
    public int pushComment(Comment comment) {
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        Assert.notNull(problemMapper.selectByPrimaryKey(comment.getProblemId()),"没有这个问题");
        return  commentMapper.insert(comment);

    }
}
