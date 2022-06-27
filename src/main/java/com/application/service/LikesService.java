package com.application.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.application.mapper.CommentMapper;
import com.application.mapper.LikesMapper;
import com.application.mapper.ReviewMapper;
import com.application.model.entity.*;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikesService {
    @Autowired
    private LikesMapper likesMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ReviewMapper reviewMapper;

    public boolean addLikeComment(Long userId, Long commmentId) {
        LikesExample likesExample=new LikesExample();
        likesExample.createCriteria().andCommentIdEqualTo(commmentId)
                .andUserIdEqualTo(userId);
        List<Likes> likes = likesMapper.selectByExample(likesExample);
        if(likes.size()==1){
            Likes like = likes.get(0);
            likesMapper.deleteByPrimaryKey(like.getId());
            Comment comment = commentMapper.selectByPrimaryKey(like.getCommentId());
            comment.setLikess(comment.getLikess()-1);
            commentMapper.updateByPrimaryKey(comment);
            return false;
        }else{
            Likes like = new Likes(null,userId, commmentId,null);
            likesMapper.insert(like);
            Comment comment = commentMapper.selectByPrimaryKey(like.getCommentId());
            comment.setLikess(comment.getLikess()+1);
            commentMapper.updateByPrimaryKey(comment);
            return true;
        }
    }
    public boolean addLikeReview(Long userId, Long reviewId) {
        LikesExample likesExample=new LikesExample();
        likesExample.createCriteria().andEmailIdEqualTo(reviewId)
                .andUserIdEqualTo(userId);
        List<Likes> likes = likesMapper.selectByExample(likesExample);
        if(likes.size()==1){
            Likes like = likes.get(0);
            likesMapper.deleteByPrimaryKey(like.getUserId());
            Review review = reviewMapper.selectByPrimaryKey(like.getEmailId());
            review.setLikes(review.getLikes()-1);
            reviewMapper.updateByPrimaryKey(review);
//            FileUtil.
            return false;
        }else{
            Likes like = new Likes(null,userId, null,reviewId);
            likesMapper.insert(like);
            Review review = reviewMapper.selectByPrimaryKey(like.getEmailId());
//            comment.setLikess(comment.getLikess()+1);
            review.setLikes(review.getLikes()+1);
            reviewMapper.updateByPrimaryKey(review);
            return true;
        }
    }
}
