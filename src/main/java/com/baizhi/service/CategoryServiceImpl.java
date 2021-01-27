package com.baizhi.service;

import com.baizhi.annotation.AddCache;
import com.baizhi.annotation.AddLog;
import com.baizhi.annotation.DelCache;
import com.baizhi.dao.CategoryMapper;
import com.baizhi.entity.Category;
import com.baizhi.entity.CategoryExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryMapper cm;


    @AddCache
    @Override//一级类别分页
    public HashMap<String, Object> queryOneByPage(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page",page);
        System.out.println("当前页page:"+page);

        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo("1");
        //总条数
        int records = cm.selectCountByExample(example);
        map.put("records",records);

        //总页数=总条数/每页展示条数  除不尽+1页
        Integer total = records%rows==0?records/rows:records/rows+1;
        map.put("total",total);
        //数据
        int p = (page-1)*rows;
        int r = rows;
        RowBounds rowBounds = new RowBounds(p,r);
        List<Category> categorys = cm.selectByExampleAndRowBounds(example,rowBounds);
        map.put("rows",categorys);

        return map;
    }

    @AddCache
    @Override//二级类别分页
    public HashMap<String, Object> queryTwoByPage(String categoryId, Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page",page);
        System.out.println("当前页page:"+page);

        CategoryExample example = new CategoryExample();
        //example.createCriteria().andLevelsEqualTo("2");
        example.createCriteria().andParentIdEqualTo(categoryId);
        //总条数
        int records = cm.selectCountByExample(example);
        map.put("records",records);

        //总页数=总条数/每页展示条数  除不尽+1页
        Integer total = records%rows==0?records/rows:records/rows+1;
        map.put("total",total);
        //数据
        int p = (page-1)*rows;
        int r = rows;
        RowBounds rowBounds = new RowBounds(p,r);
        List<Category> categorys = cm.selectByExampleAndRowBounds(example,rowBounds);
        map.put("rows",categorys);

        return map;
    }

    @DelCache
    @AddLog(description = "修改类别")
    @Override//修改
    public void update(Category category) {
        cm.updateByPrimaryKeySelective(category);
    }

    @DelCache
    @AddLog(description = "添加类别")
    @Override//添加
    public String add(String levelCate, String categoryId, Category category) {
        String uuid = UUID.randomUUID().toString();
        category.setId(uuid);
        category.setLevels(levelCate);
        category.setParentId(categoryId);

        //添加
        cm.insertSelective(category);

        //返回当前添加数据的id
        return uuid;
    }

    @DelCache
    @AddLog(description = "删除类别")
    @Override//删除
    public void delete(Category category) {
        Category categorys = cm.selectOne(category);
        CategoryExample example = new CategoryExample();
        example.createCriteria().andIdEqualTo(categorys.getId());

        cm.deleteByExample(example);
    }
}
