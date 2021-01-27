package com.baizhi.app;

import com.baizhi.common.CommonResult;
import com.baizhi.po.VideoPO;
import com.baizhi.service.VideoService;
import com.baizhi.util.CreateValidateCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("app")
public class AppController {
    
    
    @Resource
    VideoService vs;

    @RequestMapping("getPhoneCode")
    public HashMap<String, Object> getPhoneCode(String phone){

        HashMap<String, Object> map = new HashMap<>();
        //根据用户输入的手机号给该用户发送手机验证码
        String phoneCode = null;
        CreateValidateCode validateUtil = new CreateValidateCode();
        try {
            phoneCode = validateUtil.getCode();
            map.put("status","100");
            map.put("message","发送成功");
            map.put("data",phone);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status","104");
            map.put("message","发送失败");
            map.put("data",null);
        }

        System.out.println("  手机验证码："+phoneCode);

        return map;
    }

    @RequestMapping("queryByReleaseTimes")
    public Object queryByReleaseTimes(){
        HashMap<String, Object> map = new HashMap<>();
        try {
            List<VideoPO> videoPOS =vs.queryByReleaseTime();
            map.put("status","100");
            map.put("message","查询成功");
            map.put("data",videoPOS);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status","104");
            map.put("message","查询失败");
            map.put("data",null);
        }
        return map;
    }

    @RequestMapping("queryByReleaseTime")
    public Object queryByReleaseTime(){
        try {
            List<VideoPO> videoPOS =vs.queryByReleaseTime();
            return new CommonResult().success(videoPOS);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().faild();
        }
    }



}
