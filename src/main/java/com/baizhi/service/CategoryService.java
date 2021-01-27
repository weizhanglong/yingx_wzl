package com.baizhi.service;

import com.baizhi.entity.Category;

import java.util.HashMap;
import java.util.List;

public interface CategoryService {

    //一级类别分页
    HashMap<String, Object> queryOneByPage(Integer page, Integer rows);

    //二级类别分页
    HashMap<String, Object> queryTwoByPage(String categoryId, Integer page, Integer rows);

    //修改
    void update(Category category);

    //添加
    String add(String levelCate, String categoryId, Category category);

    //删除
    void delete(Category category);
}
