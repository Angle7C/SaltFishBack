package com.application.service;

import cn.hutool.core.lang.Assert;
import com.application.mapper.ProblemMapper;
import com.application.mapper.RecordMapper;
import com.application.mapper.UserMapper;
import com.application.model.DTO.ProblemDTO;
import com.application.model.entity.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProblemService {
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecordMapper recordMapper;

    @Transactional
    public Problem create(Problem problem) {
        problem.setGmtCreate(System.currentTimeMillis());
        problem.setGmtModified(problem.getGmtCreate());
        int insert = problemMapper.insert(problem);
        Assert.isTrue(insert==1,"问题找不到");
        return problem;
    }
    @Transactional
    public ProblemDTO update(Problem problem) {
        problem.setGmtModified(System.currentTimeMillis());
        Problem problemTemp = problemMapper.selectByPrimaryKey(problem.getId());
        Assert.notNull(problemTemp,"没有这个问题");
        problem.setGmtCreate(problemTemp.getGmtCreate());
        User user = userMapper.selectByPrimaryKey(problem.getUserId());
        Assert.notNull(user,"没有这个上传者");
        problemMapper.updateByPrimaryKey(problem);
        return new ProblemDTO(problem,user);
    }

    public PageInfo allList(Integer pageSize,Integer pageIndex) {
        PageHelper.startPage(pageIndex,pageSize);
        ProblemExample problemExample=new ProblemExample();

        List<Problem> problems = problemMapper.selectByExample(problemExample);
        PageInfo<Problem> problemPageInfo = new PageInfo<>(problems);
        return problemPageInfo;
    }
    @Transactional
    public ProblemDTO delete(Long id) {
        //删除做题记录
        RecordExample recordExample=new RecordExample();
        recordExample.createCriteria().andProblemIdEqualTo(id);
        recordMapper.deleteByExample(recordExample);
        //删除问题本身
        Problem problem = problemMapper.selectByPrimaryKey(id);
        Assert.notNull(problem,"没有这个问题");
        User user=userMapper.selectByPrimaryKey(problem.getUserId());
        Assert.notNull(user,"没有上传者");
        problemMapper.deleteByPrimaryKey(id);
        return new ProblemDTO(problem,user);
    }
}