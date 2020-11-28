package com.example.hospital.dao;

import com.example.hospital.model.Notify;
import com.example.hospital.model.NotifyExample;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NotifyMapper {
    int countByExample(NotifyExample example);

    int deleteByExample(NotifyExample example);

    int deleteByPrimaryKey(@Param("userId") Integer userId, @Param("date") Date date);

    int insert(Notify record);

    int insertSelective(Notify record);

    List<Notify> selectByExample(NotifyExample example);

    Notify selectByPrimaryKey(@Param("userId") Integer userId, @Param("date") Date date);

    int updateByExampleSelective(@Param("record") Notify record, @Param("example") NotifyExample example);

    int updateByExample(@Param("record") Notify record, @Param("example") NotifyExample example);

    int updateByPrimaryKeySelective(Notify record);

    int updateByPrimaryKey(Notify record);
}