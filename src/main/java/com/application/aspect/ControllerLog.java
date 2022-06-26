package com.application.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
@Aspect
public class ControllerLog {
    private static  final  Logger logger=LoggerFactory.getLogger("web访问入口日志");


    @Pointcut("execution( public String com.application.constant.OtherController.*(..))")
    public void points(){}
    @Pointcut("execution( public * com.application.controller.*.*(..))")
    public void point(){}
    @Before("point()")
    public void entry(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        logger.info("访问方法：{} ：织入对象：{} 访问参数：{}", signature.getName(),joinPoint.getTarget().getClass().getName(), Arrays.toString(joinPoint.getArgs()));
    }
    @AfterReturning(value = "point()",returning ="returnObject")
    public void exit(JoinPoint joinPoint,Object returnObject){
        Signature signature = joinPoint.getSignature();
        logger.info("退出方法：{} 织入对象：{} 返回对象：{}", signature.getName(),joinPoint.getTarget().getClass().getName(),returnObject);

    }
    @Before("points()")
    public void entrysss(JoinPoint joinPoint){}

    @AfterReturning(value = "points()")
    public void exitsss(JoinPoint joinPoint){

    }
}
