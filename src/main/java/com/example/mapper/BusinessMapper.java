package com.example.mapper;

import org.springframework.stereotype.Repository;

import com.example.entity.Business;
import tk.mybatis.mapper.common.Mapper;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface BusinessMapper extends Mapper<Business> {
}