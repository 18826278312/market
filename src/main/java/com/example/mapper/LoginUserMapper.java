package com.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.example.entity.LoginUser;
import tk.mybatis.mapper.common.Mapper;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface LoginUserMapper extends Mapper<LoginUser> {
	LoginUser selectByPhone(@Param("phone")String phone);
	void updateByPhone(@Param("phone")String phone);
}