package com.application.mapper;

import com.application.model.entity.Record;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface RecordExtMapper {
    public List<Record> selectDistinctProblem(Long id);
}
