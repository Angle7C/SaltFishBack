package com.application.aspect;

import com.application.constant.enums.ErrorEnum;
import com.application.model.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Component
@Slf4j
public class ExecptionHandler {
    private  static  final Logger log= LoggerFactory.getLogger("异常抓取");
    private  static  final ResultJson<String> json=new ResultJson<String>();
    @ExceptionHandler(Throwable.class)
    public ResultJson unkown(Throwable throwable){
        log.error("出现异常：[{}] [异常消息；{}] [异常地点：{}]",throwable.getMessage(),throwable.getCause(),throwable.getStackTrace()[0]);
        return new ResultJson(ErrorEnum.UNKNOW_ERROR);
    };
    @ExceptionHandler(IllegalArgumentException.class)
    public ResultJson unkown(IllegalArgumentException e){
        log.error("参数异常  异常消息；{}",e.getMessage());
//        ResultJson json=new ResultJson();
        json.error(2001L,e.getMessage());
        return json;
    };
}
