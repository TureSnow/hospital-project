package com.example.hospital.dao;

import com.example.hospital.model.NaSheet;
import com.example.hospital.model.NaSheetExample;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NaSheetMapper {
    int countByExample(NaSheetExample example);

    int deleteByExample(NaSheetExample example);

    int deleteByPrimaryKey(@Param("patientId") Integer patientId, @Param("date") Date date);

    int insert(NaSheet record);

    int insertSelective(NaSheet record);

    List<NaSheet> selectByExample(NaSheetExample example);

    NaSheet selectByPrimaryKey(@Param("patientId") Integer patientId, @Param("date") Date date);

    int updateByExampleSelective(@Param("record") NaSheet record, @Param("example") NaSheetExample example);

    int updateByExample(@Param("record") NaSheet record, @Param("example") NaSheetExample example);

    int updateByPrimaryKeySelective(NaSheet record);

    int updateByPrimaryKey(NaSheet record);
}