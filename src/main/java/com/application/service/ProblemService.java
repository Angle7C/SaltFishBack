package com.application.service;

import cn.hutool.core.lang.Assert;
import com.application.mapper.ProblemMapper;
import com.application.mapper.RecordMapper;
import com.application.mapper.UserMapper;
import com.application.model.DTO.ProblemDTO;
import com.application.model.DTO.UserDTO;
import com.application.model.entity.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        problemMapper.updateByPrimaryKeyWithBLOBs(problem);
        return new ProblemDTO(problem,user);
    }
    public PageInfo allList(Integer pageSize,Integer pageIndex) {
        PageHelper.startPage(pageIndex,pageSize);
        ProblemExample problemExample=new ProblemExample();
        List<Problem> problems = problemMapper.selectByExample(problemExample);
        PageInfo<Problem> problemPageInfo = new PageInfo<>(problems);
        return problemPageInfo;
    }
    public List<ProblemDTO> allList() {
        ProblemExample problemExample=new ProblemExample();
        List<Problem> problems = problemMapper.selectByExampleWithBLOBs(problemExample);
        List<ProblemDTO> collect = problems.stream()
                .map(item -> new ProblemDTO(item, userMapper.selectByPrimaryKey(item.getUserId())))
                .collect(Collectors.toList());
        return collect;
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

    public List<Problem> getProblem(Long... id) {
        List<Long> collect = Arrays.stream(id).collect(Collectors.toList());
        ProblemExample problemExample=new ProblemExample();
        problemExample.createCriteria().andIdIn(collect);
        List<Problem> problems = problemMapper.selectByExampleWithBLOBs(problemExample);
//        User user = userMapper.selectByPrimaryKey(problem.getUserId());
        return problems;
    }

    public PageInfo<ProblemDTO> searchProbelm(String name, String[] tag, String level,Integer pageSize,Integer pageIndex) {
        ProblemExample problemExample=new ProblemExample();
        problemExample.createCriteria()
                .andTitleLike("%"+name+"%");
        Long aLong = (ProblemDTO.changTag(tag)==0?Long.MAX_VALUE:ProblemDTO.changTag(tag));

        //                .andTagLessThan(ProblemDTO.changTag(tag)+999);
        List<Problem> problems = problemMapper.selectByExample(problemExample);
        List<ProblemDTO> problemList = problems.stream()
                                                    .filter(item->( item.getTag() & aLong )>0)
                                                    .map(item -> new ProblemDTO(item, userMapper.selectByPrimaryKey(item.getUserId())))
                                                    .collect(Collectors.toList());
        PageHelper.startPage(pageIndex,pageSize);
        PageInfo<ProblemDTO> pageInfo = new PageInfo<>(problemList);
        return pageInfo;
    }

    public List<ProblemDTO> allListNone() {
        ProblemExample problemExample=new ProblemExample();
        List<Problem> problems = problemMapper.selectByExample(problemExample);
        List<ProblemDTO> collect = problems.stream()
                .map(item -> new ProblemDTO(item, userMapper.selectByPrimaryKey(item.getUserId())))
                .collect(Collectors.toList());
        return collect;
    }
}
