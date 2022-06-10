package com.application.mapper;

import com.application.model.entity.Sign;
import com.application.model.entity.SignExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface SignMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sign
     *
     * @mbggenerated Thu Jun 09 11:26:28 CST 2022
     */
    int countByExample(SignExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sign
     *
     * @mbggenerated Thu Jun 09 11:26:28 CST 2022
     */
    int deleteByExample(SignExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sign
     *
     * @mbggenerated Thu Jun 09 11:26:28 CST 2022
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sign
     *
     * @mbggenerated Thu Jun 09 11:26:28 CST 2022
     */
    int insert(Sign record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sign
     *
     * @mbggenerated Thu Jun 09 11:26:28 CST 2022
     */
    int insertSelective(Sign record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sign
     *
     * @mbggenerated Thu Jun 09 11:26:28 CST 2022
     */
    List<Sign> selectByExample(SignExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sign
     *
     * @mbggenerated Thu Jun 09 11:26:28 CST 2022
     */
    Sign selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sign
     *
     * @mbggenerated Thu Jun 09 11:26:28 CST 2022
     */
    int updateByExampleSelective(@Param("record") Sign record, @Param("example") SignExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sign
     *
     * @mbggenerated Thu Jun 09 11:26:28 CST 2022
     */
    int updateByExample(@Param("record") Sign record, @Param("example") SignExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sign
     *
     * @mbggenerated Thu Jun 09 11:26:28 CST 2022
     */
    int updateByPrimaryKeySelective(Sign record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sign
     *
     * @mbggenerated Thu Jun 09 11:26:28 CST 2022
     */
    int updateByPrimaryKey(Sign record);
}