package com.application.service;

import cn.hutool.core.lang.Assert;
import com.application.mapper.ProblemMapper;
import com.application.mapper.ReviewMapper;
import com.application.mapper.TieMapper;
import com.application.mapper.UserMapper;
import com.application.model.DTO.ReviewDTO;
import com.application.model.DTO.TieDTO;
import com.application.model.entity.Review;
import com.application.model.entity.ReviewExample;
import com.application.model.entity.Tie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewService {
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private TieMapper tieMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProblemMapper problemMapper;
    public Review create(Review revice) {
        Assert.notNull(revice.getContent(),"内容为空");
        Assert.notNull(revice.getUserId(),"用户为空");
        Assert.notNull(revice.getProblemId(),"评论为空");
        revice.setLikes(0L);
        reviewMapper.insert(revice);
        return revice;
    }

    public TieDTO selectById(Long titleId) {
        Assert.notNull(titleId,"id为空");
        ReviewExample reviewExample=new ReviewExample();
        reviewExample.createCriteria().andProblemIdEqualTo(titleId);
        List<Review> reviews = reviewMapper.selectByExample(reviewExample);
        List<ReviewDTO> collect = reviews.stream()
                .map(item -> new ReviewDTO(item, userMapper.selectByPrimaryKey(item.getUserId())))
                .collect(Collectors.toList());
        Tie tie = tieMapper.selectByPrimaryKey(titleId);

        TieDTO tieDTO = new TieDTO(tie, userMapper.selectByPrimaryKey(tie.getUserId()), problemMapper.selectByPrimaryKey(tie.getProblemId()));
        tieDTO.setRecent(collect);
        return tieDTO;
    }
    @Transactional
    public void delReview(List<Long> list) {
        ReviewExample reviewExample=new ReviewExample();
        reviewExample.createCriteria().andIdIn(list);
        reviewMapper.deleteByExample(reviewExample);
    }
}
