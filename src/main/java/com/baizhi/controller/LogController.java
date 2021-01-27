package com.baizhi.controller;


import com.baizhi.entity.User;
import com.baizhi.service.LogService;
import com.baizhi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;

@Controller
@RequestMapping("/log")
public class LogController {
    @Resource
    LogService ls;

    @RequestMapping("queryByPage")
    @ResponseBody
    public HashMap<String, Object> queryByPage(Integer page, Integer rows){
        HashMap<String, Object> map = ls.queryByPage(page, rows);
        return map;
    }

}
