package com.baizhi.controller;

import com.baizhi.entity.City;
import com.baizhi.entity.UserVO;
import com.baizhi.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("echarts")
public class EchartsController {

    /*
    * {
          "month": ["1月","2月","3月","4月","5月","6月"],
          "boys": [5, 200, 36, 10, 10, 200],
          "girls": [200, 20, 360, 100, 10, 20]
       }
    sql：
    select CONCAT(MONTH(create_time),'月') month ,count(id) count
    from yx_user where sex='男' group by MONTH(create_time)

    * 函数
        month(create_time) ：取时间字段的月值
        concat（值，”拼接的字符“）
    * */
    @RequestMapping("getUserRegirestData")
    public HashMap<String, Object> getUserRegirestData(){

        //数据查询 sql怎么写 数据封装
        HashMap<String, Object> map = new HashMap<>();

        map.put("month", Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月"));
        map.put("boys",Arrays.asList(5, 200, 36, 10, 10, 200));
        map.put("girls",Arrays.asList(200, 20, 360, 100, 10, 20));

        /*
        * http  应用层协议   短连接
        * tcp/ip 网络层协议  长链接
        * */
        return map;
    }


    @Resource
    UserService userService;

    @RequestMapping("getUserRegirestChinaDatas")
    public ArrayList<UserVO> getUserRegirestChinaDatas(){

        List<City> boyCitys = userService.queryUserCity("男");
        List<City> girlsCitys = userService.queryUserCity("女");

        ArrayList<UserVO> userVOS = new ArrayList<>();
        userVOS.add(new UserVO("小男生",boyCitys));
        userVOS.add(new UserVO("小姑娘",girlsCitys));

        return userVOS;
    }

}
