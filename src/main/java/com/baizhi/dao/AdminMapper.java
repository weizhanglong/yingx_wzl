package com.baizhi.dao;

import com.baizhi.entity.Admin;
import com.baizhi.entity.AdminExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface AdminMapper extends Mapper<Admin> {


    Admin queryByUsername(String username);


}