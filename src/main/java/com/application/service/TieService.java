package com.application.service;

import com.application.mapper.ProblemMapper;
import com.application.mapper.TieMapper;
import com.application.mapper.UserMapper;
import com.application.model.DTO.TieDTO;
import com.application.model.entity.Problem;
import com.application.model.entity.Tie;
import com.application.model.entity.TieExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TieService {
    @Autowired
    private TieMapper tieMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProblemMapper problemMapper;

    public List<TieDTO> getAll(){

        TieExample tieExample=new TieExample();
        List<Tie> ties = tieMapper.selectByExample(tieExample);
        List<TieDTO> collect = ties.stream().map(item ->
                new TieDTO(item, userMapper.selectByPrimaryKey(item.getUserId()), problemMapper.selectByPrimaryKey(item.getProblemId()))
        ).collect(Collectors.toList());
        return collect;
    }

    public void insert(Tie tie) {
         tieMapper.insert(tie);
    }
}
