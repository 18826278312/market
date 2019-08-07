package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.example.entity.Log;
import tk.mybatis.mapper.common.Mapper;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface LogMapper extends Mapper<Log> {
	List<Log> selectLog(@Param("name")String name);
}