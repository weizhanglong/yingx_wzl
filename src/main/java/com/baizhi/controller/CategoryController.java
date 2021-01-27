package com.baizhi.controller;

import com.baizhi.entity.Category;
import com.baizhi.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;

@RequestMapping("/category")
@Controller
public class CategoryController {

    @Resource
    CategoryService cs;

    @RequestMapping("queryOneByPage")
    @ResponseBody
    public HashMap<String, Object> queryOneByPage(Integer page, Integer rows){
        HashMap<String, Object> map = cs.queryOneByPage(page, rows);
        return map;
    }

    @RequestMapping("queryTwoByPage")
    @ResponseBody
    public HashMap<String, Object> queryTwoByPage(String categoryId, Integer page, Integer rows){
        HashMap<String, Object> map = cs.queryTwoByPage(categoryId, page, rows);
        return map;
    }

    @RequestMapping("edit")
    @ResponseBody
    public String edit(String levelCate, String categoryId, Category category, String oper){

        System.out.println("  userController: "+category);

        String id=null;

        /*
         * 1.添加数据入库
         * 2.修改 添加的返回值 返回一个数据id   返回到页面上afterSubmit:function(data)
         * 3.执行文件上传   携带添加的id
         * 4.根据id修改头像路径
         * */
        if (oper.equals("add")){
            id = cs.add(levelCate,categoryId,category);
        }
        /*
         * 1.修改数据  判断是否修改文件  返回id
         * 2.删除旧文件，上传新文件
         * 3.修改头像路径
         * */
        if (oper.equals("edit")){
            cs.update(category);
        }
        if (oper.equals("del")){
            cs.delete(category);
        }
        return id;
    }
}
