package com.baizhi.controller;

import com.baizhi.entity.Admin;
import com.baizhi.service.AdminService;
import com.baizhi.util.ImageCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

@RequestMapping("admin")
@Controller
public class AdminController {

    @Autowired
    AdminService adminService;

    @RequestMapping("getImageCode")
    public void getImageCode(HttpServletResponse response,HttpServletRequest request){
        //1.获取随机验证码
        String randomCode = ImageCodeUtil.getSecurityCode();
        System.out.println(" 登录验证码： "+randomCode);

        //存储验证码字符
        request.getSession().setAttribute("imageCode",randomCode);

        //3.根据随机验证码创建图片
        BufferedImage image = ImageCodeUtil.createImage(randomCode);

        try {
            //4.将验证码响应给前台
            ImageIO.write(image,"png",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping("login")
    public HashMap<String, Object> login(Admin admin,String enCode){
        return adminService.login(admin, enCode);
    }

    @RequestMapping("logout")
    public String logout(HttpServletRequest request){
        //清楚登录标记
        request.getSession().removeAttribute("admin");
        return "redirect:/login/login.jsp";
    }


    @ResponseBody
    @RequestMapping("queryAdminPage")
    public HashMap<String, Object> queryAdminPage(Integer page, Integer rows){
        return adminService.queryUserPage(page, rows);
    }

    @ResponseBody
    @RequestMapping("edit")
    public void edit(Admin admin, String oper){

        if(oper.equals("add")){
            adminService.add(admin);
        }
        if(oper.equals("edit")){
            adminService.edit(admin);
        }
        if(oper.equals("del")){
            adminService.del(admin);
        }
    }


}
