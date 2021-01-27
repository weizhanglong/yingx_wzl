package com.baizhi.controller;


import com.baizhi.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {
    @Resource
    FeedbackService fs;

    @RequestMapping("queryByPage")
    @ResponseBody
    public HashMap<String, Object> queryByPage(Integer page, Integer rows){
        HashMap<String, Object> map = fs.queryByPage(page, rows);
        return map;
    }

    @RequestMapping("Exports")
    @ResponseBody
    public Object Exports(){
        try {
            fs.Exports();
            return "导出数据成功！";
        } catch (Exception e) {
            e.printStackTrace();
            return "导出数据失败！";
        }
    }

    //导入数据
    @ResponseBody
    @RequestMapping("Import")
    public String Import(){
        try {
            fs.Import();
            return "导入数据成功！";
        } catch (Exception e) {
            e.printStackTrace();
            return "导入数据失败！";
        }
    }

}
