package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.example.entity.Region;
import tk.mybatis.mapper.common.Mapper;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface RegionMapper extends Mapper<Region> {
	List<Region> selectRegion(@Param("companyName")String companyName,@Param("businessName")String businessName,@Param("dutyName")String dutyName);
}