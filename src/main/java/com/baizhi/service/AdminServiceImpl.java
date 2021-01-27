package com.baizhi.service;

import com.baizhi.annotation.AddCache;
import com.baizhi.annotation.AddLog;
import com.baizhi.dao.AdminMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.AdminExample;
import com.baizhi.util.Md5Utils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Resource
    HttpSession session;

    @Resource
    AdminMapper am;

    @Override
    public HashMap<String, Object> login(Admin admin, String enCode) {

        HashMap<String, Object> map = new HashMap<>();

        //1.判断验证码是否正确
        //1-1 取出验证码
        String imageCode = (String) session.getAttribute("imageCode");
        //1-2 对比验证码
        if(imageCode.equals(enCode)){
            //2.管理员是否存在

            AdminExample example = new AdminExample();
            example.createCriteria().andUsernameEqualTo(admin.getUsername());

            //2-1 根据管理员用户名查询管理员数据
            Admin admins = am.selectOneByExample(example);
            //2-2 判断管理员是否存在
            if(admins!=null){
                //3.判断管理员状态
                if(admins.getStatus().equals("0")){
                    //4.管理员密码是否正确
                    if(Md5Utils.getMd5Code((admins.getSalt()+admin.getPassword()+admins.getSalt())).equals(admins.getPassword())){
                        //存储管理员标记
                        session.setAttribute("admin",admins);

                        map.put("status","100");
                        map.put("message","登录成功");
                    }else{
                        map.put("status","101");
                        map.put("message","用户密码不正确");
                    }
                }else{
                    map.put("status","101");
                    map.put("message","该用户被限制登录");
                }
            }else{
                map.put("status","101");
                map.put("message","该用户不存在");
            }
        }else{
            map.put("status","101");
            map.put("message","验证码不正确");
        }
        return map;
    }

    @AddCache
    @Override
    public HashMap<String, Object> queryUserPage(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();
        //设置当前页
        map.put("page",page);
        //创建条件对象
        AdminExample example = new AdminExample();
        //创建分页对象   参数：从第几条开始，展示几条
        RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
        //查询数据
        List<Admin> admins = am.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows",admins);

        //查询总条数
        int records = am.selectCountByExample(example);
        map.put("records",records);

        //计算总页数
        Integer tolal=records%rows==0?records/rows:records/rows+1;
        map.put("total",tolal);

        return map;
    }


    @AddLog(description = "添加管理员")
    @Override
    public void add(Admin admin) {
        admin.setId(UUID.randomUUID().toString());
        admin.setStatus("0");

        //生成随机盐
        String salt = Md5Utils.getSalt(4);
        //拼接随机盐加密
        String md5Code = Md5Utils.getMd5Code(salt + admin.getPassword() + salt);

        admin.setSalt(salt);
        admin.setPassword(md5Code);

        am.insertSelective(admin);
    }


    @AddLog(description = "修改管理员信息")
    @Override
    public void edit(Admin admin) {
        am.updateByPrimaryKeySelective(admin);
    }


    @AddLog(description = "删除管理员")
    @Override
    public void del(Admin admin) {
        am.deleteByPrimaryKey(admin);
    }
}
