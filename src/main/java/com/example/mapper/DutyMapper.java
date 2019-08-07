package com.example.mapper;

import org.springframework.stereotype.Repository;

import com.example.entity.Duty;
import tk.mybatis.mapper.common.Mapper;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface DutyMapper extends Mapper<Duty> {
}