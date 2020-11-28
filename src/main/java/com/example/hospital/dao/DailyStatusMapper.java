package com.example.hospital.dao;

import com.example.hospital.model.DailyStatus;
import com.example.hospital.model.DailyStatusExample;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DailyStatusMapper {
    int countByExample(DailyStatusExample example);

    int deleteByExample(DailyStatusExample example);

    int deleteByPrimaryKey(@Param("patientId") Integer patientId, @Param("date") Date date);

    int insert(DailyStatus record);

    int insertSelective(DailyStatus record);

    List<DailyStatus> selectByExample(DailyStatusExample example);

    DailyStatus selectByPrimaryKey(@Param("patientId") Integer patientId, @Param("date") Date date);

    int updateByExampleSelective(@Param("record") DailyStatus record, @Param("example") DailyStatusExample example);

    int updateByExample(@Param("record") DailyStatus record, @Param("example") DailyStatusExample example);

    int updateByPrimaryKeySelective(DailyStatus record);

    int updateByPrimaryKey(DailyStatus record);
}