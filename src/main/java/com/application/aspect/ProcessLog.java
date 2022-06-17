package com.application.aspect;

import com.application.model.DTO.RecordDTO;
import com.application.model.ResultJson;
import com.application.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ProcessLog {
    @Autowired
    private RecordService recordService;

    @AfterReturning(value = "execution(public * com.application.controller.JudgeController.subCompiler())",returning = "json")
    public void exitProcess(JoinPoint joinPoint, ResultJson<RecordDTO> json){
        if(json.getCode()==1000L){
            recordService.runProcess(json.getData());
        }
    }

}
