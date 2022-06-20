package com.application.service;

import cn.hutool.core.lang.Assert;
import com.application.mapper.CommentMapper;
import com.application.mapper.LikesMapper;
import com.application.model.entity.Comment;
import com.application.model.entity.Likes;
import com.application.model.entity.LikesExample;
import com.application.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikesService {
    @Autowired
    private LikesMapper likesMapper;
    @Autowired
    private CommentMapper commentMapper;
    public boolean addLike(Long userId, Long commmentId) {
        LikesExample likesExample=new LikesExample();
        likesExample.createCriteria().andCommentIdEqualTo(commmentId)
                .andUserIdEqualTo(userId);
        List<Likes> likes = likesMapper.selectByExample(likesExample);
        if(likes.size()==1){
            Likes like = likes.get(0);
            likesMapper.deleteByPrimaryKey(like.getUserId());
            Comment comment = commentMapper.selectByPrimaryKey(like.getCommentId());
            comment.setLikess(comment.getLikess()-1);
            commentMapper.updateByPrimaryKey(comment);
            return false;
        }else{
            Likes like = new Likes(userId, commmentId, null);
            likesMapper.insert(like);
            Comment comment = commentMapper.selectByPrimaryKey(like.getCommentId());
            comment.setLikess(comment.getLikess()+1);
            return true;
        }
    }
}
