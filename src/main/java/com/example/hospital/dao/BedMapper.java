package com.example.hospital.dao;

import com.example.hospital.model.Bed;
import com.example.hospital.model.BedExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BedMapper {
    int countByExample(BedExample example);

    int deleteByExample(BedExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Bed record);

    int insertSelective(Bed record);

    List<Bed> selectByExample(BedExample example);

    Bed selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Bed record, @Param("example") BedExample example);

    int updateByExample(@Param("record") Bed record, @Param("example") BedExample example);

    int updateByPrimaryKeySelective(Bed record);

    int updateByPrimaryKey(Bed record);
}