package com.application.aspect;

import com.application.model.DTO.RecordDTO;
import com.application.model.ResultJson;
import com.application.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ProcessLog {
    @Autowired
    private RecordService recordService;
    @Pointcut(" execution(public * com.application.controller.JudgeController.subCompiler())")
    public void point(){}
    @AfterReturning(value = "point()",returning = "json")
    public void exitProcess(JoinPoint joinPoint, ResultJson<RecordDTO> json){
        log.info("准备运行程序");
        if(json.getCode()==1000L){
//            recordService.runProcess(json.getData());
        }
        log.info("完成运行程序");
    }

}
