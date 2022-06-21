package com.application.mapper;

import com.application.model.entity.Tie;
import com.application.model.entity.TieExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TieMapper {
    int countByExample(TieExample example);

    int deleteByExample(TieExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Tie record);

    int insertSelective(Tie record);

    List<Tie> selectByExample(TieExample example);

    Tie selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Tie record, @Param("example") TieExample example);

    int updateByExample(@Param("record") Tie record, @Param("example") TieExample example);

    int updateByPrimaryKeySelective(Tie record);

    int updateByPrimaryKey(Tie record);
}