package com.baizhi.service;

import com.baizhi.entity.City;
import com.baizhi.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface UserService {


    HashMap<String, Object> queryByPage(Integer page, Integer rows);

    void update(User user);

    String add(User user);

    void delete(User user);

    void uploadUserHeadImg(MultipartFile headImg, String id);

    public List<City> queryUserCity(String sex);
}
