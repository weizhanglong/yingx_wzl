package com.baizhi.aspect;

import com.baizhi.annotation.AddLog;
import com.baizhi.dao.LogMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

@Aspect  //生命这个类是切面类
@Configuration  //这个一个交给Spring工厂管理的配置类
public class LogAspect {

    @Resource
    HttpSession session;

    @Resource
    LogMapper lm;

    //自定义注解   java元注解（修饰注解的注解）
    //配置切面  切增删改方法
    @Around("@annotation(com.baizhi.annotation.AddLog)")
    public Object addLog(ProceedingJoinPoint proceedingJoinPoint){

        //谁  时间   操作   是否成功
        Admin admin = (Admin) session.getAttribute("admin");

        //获取切面切到方法的名字
        String methodName = proceedingJoinPoint.getSignature().getName();

        //获取切面切到的方法
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        //获取方法上的注解
        AddLog addLog = method.getAnnotation(AddLog.class);
        //获取注解上的属性值
        String description = addLog.description();
        //拼接操作方法
        String optionMethod=methodName+" ( "+description+" ) ";

        String message="";
        Object result =null;
        try {
            result = proceedingJoinPoint.proceed();
            message="success";
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            message="error";
        }

        Log log = new Log(UUID.randomUUID().toString(),admin.getUsername(),new Date(),optionMethod,message);

        System.out.println("数据入库"+log);

        //数据入库
        lm.insertSelective(log);

        return result;
    }

}
