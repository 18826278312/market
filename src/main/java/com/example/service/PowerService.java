package com.example.service;

import java.util.List;

import com.example.entity.Power;
import com.example.vo.PowerVo;

public interface PowerService {
	/**
	 * 获取用户对应的权限
	 * @param telephone
	 * @return
	 */
	Power getPower(String telephone);
	/**
	 * 获取所有权限记录
	 * @return
	 */
	List<PowerVo> listPower();
}
