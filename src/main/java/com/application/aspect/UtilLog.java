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
import java.util.Arrays;

@Aspect
@Slf4j
public class UtilLog {
    private static final Logger logger= LoggerFactory.getLogger("工具；类访问日志");
    @Pointcut("execution(public * com.application.utils.*.*(..))")
    public void point(){}
    @Before("point()")
    public void entry(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        logger.info("访问工具方法：{} ：访问工具：{} 访问参数：{}", signature.getName(),joinPoint.getTarget(), Arrays.toString(joinPoint.getArgs()));
    }
    @AfterReturning(value = "point()",returning ="returnObject")
    public void exit(JoinPoint joinPoint,Object returnObject){
        Signature signature = joinPoint.getSignature();
        logger.info("退出工具方法：{} 访问工具：{} 返回对象：{}", signature.getName(),joinPoint.getTarget(),returnObject);
    }

}
