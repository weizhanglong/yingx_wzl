package com.baizhi.controller;


import com.baizhi.entity.User;
import com.baizhi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService us;

    @RequestMapping("queryByPage")
    @ResponseBody
    public HashMap<String, Object> queryByPage(Integer page, Integer rows){
        HashMap<String, Object> map = us.queryByPage(page, rows);
        return map;
    }




    @RequestMapping("edit")
    @ResponseBody
    public String edit(User user, String oper){

        System.out.println("  userController: "+user);

        String id=null;

        /*
         * 1.添加数据入库
         * 2.修改 添加的返回值 返回一个数据id   返回到页面上afterSubmit:function(data)
         * 3.执行文件上传   携带添加的id
         * 4.根据id修改头像路径
         * */
        if (oper.equals("add")){
            id = us.add(user);
        }
        /*
         * 1.修改数据  判断是否修改文件  返回id
         * 2.删除旧文件，上传新文件
         * 3.修改头像路径
         * */
        if (oper.equals("edit")){
            us.update(user);
        }
        if (oper.equals("del")){
            us.delete(user);
        }
        return id;
    }
    @RequestMapping("uploadUserHeadImg")
    @ResponseBody
    public void uploadUserHeadImg(MultipartFile headImg, String id){
        us.uploadUserHeadImg(headImg,id);
    }

}
