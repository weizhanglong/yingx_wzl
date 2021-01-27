package com.baizhi.dao;

import com.baizhi.entity.City;
import com.baizhi.entity.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    List<City> queryUserCity(String sex);
}