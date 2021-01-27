package com.baizhi.service;

import com.baizhi.entity.Admin;

import java.util.HashMap;

public interface AdminService {

    HashMap<String, Object> login(Admin admin, String enCode);

    HashMap<String,Object> queryUserPage(Integer page, Integer rows);

    void add(Admin admin);

    void edit(Admin admin);

    void del(Admin admin);
}
