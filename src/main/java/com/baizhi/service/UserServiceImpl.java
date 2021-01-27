package com.baizhi.service;

import com.baizhi.annotation.AddCache;
import com.baizhi.annotation.AddLog;
import com.baizhi.annotation.DelCache;
import com.baizhi.dao.UserMapper;
import com.baizhi.entity.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper um;

    @Resource
    HttpServletRequest request;


    @AddCache
    @Override
    public HashMap<String, Object> queryByPage(Integer page, Integer rows) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("page",page);
        System.out.println("当前页page:"+page);

        UserExample example = new UserExample();
        //总条数
        int records = um.selectCountByExample(example);
        map.put("records",records);

        //总页数=总条数/每页展示条数  除不尽+1页
        Integer total = records%rows==0?records/rows:records/rows+1;
        map.put("total",total);
        //数据
        int p = (page-1)*rows;
        int r = rows;
        RowBounds rowBounds = new RowBounds(p,r);
        List<User> users = um.selectByExampleAndRowBounds(example,rowBounds);
        map.put("rows",users);

        return map;
    }

    @DelCache
    @AddLog(description = "修改用户信息")
    @Override
    public void update(User user) {
        um.updateByPrimaryKeySelective(user);
    }

    @DelCache
    @AddLog(description = "添加用户信息")
    @Override
    public String add(User user) {
        String uuid = UUID.randomUUID().toString();
        user.setId(uuid);
        user.setWechat(user.getPhone());
        user.setCreateTime(new Date());
        user.setStatus("0");

        //添加
        um.insert(user);

        //返回当前添加数据的id
        return uuid;
    }

    @DelCache
    @AddLog(description = "删除用户信息")
    @Override
    public void delete(User user) {

        //1.删除文件
        //根据相对路径获取绝对路径
        String realPath = request.getSession().getServletContext().getRealPath("/bootstrap/img");
        //获取文件名
        User users = um.selectOne(user);

        //拼接头像的绝对路径
        String photoPath=realPath+"/"+users.getHeadImg();

        File file = new File(photoPath);

        //判断该file存在并且是一个文件(不是文件夹)
        if(file.exists() && file.isFile() ){
            //删除文件
            boolean isOk = file.delete();
            System.out.println(" 删除文件： "+isOk);
        }

        System.out.println(" delete service: "+user);
        //2.删除数据
        um.deleteByPrimaryKey(user);
    }

    @Override
    public void uploadUserHeadImg(MultipartFile headImg, String id) {
        //1.根据相对路径获取绝对路径
        String realPath = request.getSession().getServletContext().getRealPath("/bootstrap/img");

        File file = new File(realPath);

        //判断文件夹是否存在
        if(!file.exists()){
            file.mkdirs();  //创建文件夹
        }

        //2.获取文件名   10.jpg
        String filename = headImg.getOriginalFilename();
        //文件拼接时间戳  1611027120148-10.jpg
        String newName=new Date().getTime()+"-"+filename;

        //3.文件上传
        try {
            headImg.transferTo(new File(realPath,newName));
        } catch (IOException e) {
            e.printStackTrace();
        }


        User user = new User();
        user.setId(id);
        user.setHeadImg(newName);
        //4.执行修改
        um.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<City> queryUserCity(String sex) {
        return um.queryUserCity(sex);
    }
}

