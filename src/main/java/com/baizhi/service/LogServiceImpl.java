package com.baizhi.service;

import com.baizhi.annotation.AddCache;
import com.baizhi.dao.LogMapper;
import com.baizhi.entity.Log;
import com.baizhi.entity.UserExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class LogServiceImpl implements LogService{
    @Resource
    LogMapper lm;

    @Override
    public HashMap<String, Object> queryByPage(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page",page);
        System.out.println("当前页page:"+page);

        UserExample example = new UserExample();
        //总条数
        int records = lm.selectCountByExample(example);
        map.put("records",records);

        //总页数=总条数/每页展示条数  除不尽+1页
        Integer total = records%rows==0?records/rows:records/rows+1;
        map.put("total",total);
        //数据
        int p = (page-1)*rows;
        int r = rows;
        RowBounds rowBounds = new RowBounds(p,r);
        List<Log> logs = lm.selectByExampleAndRowBounds(example,rowBounds);
        map.put("rows",logs);

        return map;
    }
}
