package com.baizhi.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.util.Set;

@Aspect  //生命这个类是切面类
@Configuration  //这个一个交给Spring工厂管理的配置类
public class CacheAspectHash {

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    //判断缓存添加缓存的方法
    @Around("@annotation(com.baizhi.annotation.AddCache)")
    public Object addCaChe(ProceedingJoinPoint proceedingJoinPoint){

        System.out.println("= 1 == 进入环绕通知 ===");

        /*解决key的乱码*/
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        //Redis K,V
        // String,String
        //Hash  KEY(String),Hash(key(String),value(String))
        //KEY:类的全限定名
            //key:方法名+实参   value数据
        
        //Key=类的全限定名+方法名+实参  Value=数据

        //创建字符串拼接对象
        StringBuilder sb = new StringBuilder();

        //获取类的全限定名  com.baizhi.serviceImpl.CategoryServiceImpl
        String className = proceedingJoinPoint.getTarget().getClass().getName();

        //获取方法名  queryOneCatePage
        String methodName = proceedingJoinPoint.getSignature().getName();
        sb.append(methodName);
        sb.append("-");

        //获取切到方法的实参  18
        Object[] args = proceedingJoinPoint.getArgs();
        //便利实参
        for (Object arg : args) {
            sb.append(arg);//拼接实参
        }

        //获取拼接的字符串  queryOneCatePage-18
        String key = sb.toString();

        //获取Hash类型的操作
        HashOperations hashOperations = redisTemplate.opsForHash();

        //根据key从redis数据库中获取缓存
        Boolean aBoolean = hashOperations.hasKey(className, key);

        Object result = null;

        //判断缓存
        if(aBoolean){
            //有缓存  取出缓存 返回结果
            result = hashOperations.get(className, key);
        }else{
            //没有 缓存查询数据库  加入缓存
            try {
                //放行  执行方法查询数据库
                result = proceedingJoinPoint.proceed();
                //加入缓存
                hashOperations.put(className,key,result);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        System.out.println("= 3 == 回到环绕通知 ===");
        return result;
    }

    //返回通知  只有目标方法执行成功不抛出异常才会进入该切面
    @AfterReturning("@annotation(com.baizhi.annotation.DelCache)")
    public void delCache(JoinPoint joinPoint){

        System.out.println("====后置通知 清除缓存====");

        //com.baizhi.serviceImpl.CategoryServiceImpl queryOneCatePage 18
        //曾删改 数据发生改变时 清除缓存

        //获取类的全限定名  com.baizhi.serviceImpl.CategoryServiceImpl
        String className = joinPoint.getTarget().getClass().getName();

        //删除数据
        redisTemplate.delete(className);
    }

}
