package com.application.aspect;

import cn.hutool.extra.mail.MailException;
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
    @ExceptionHandler(Throwable.class)
    public ResultJson unkownHandle(Throwable throwable){
        ResultJson json=new ResultJson();
        log.error("出现异常：{} 异常消息；{} 异常地点：{}",throwable.getMessage(),throwable.getCause(),throwable.getStackTrace()[0]);
        return new ResultJson(ErrorEnum.UNKNOW_ERROR);
    };
    @ExceptionHandler(IllegalArgumentException.class)
    public ResultJson argumentHandle(IllegalArgumentException e){
        ResultJson json=new ResultJson();
        log.error("参数异常  异常消息；{}",e.getMessage());
//        ResultJson json=new ResultJson();
        json.error(2001L,e.getMessage());
        return json;
    }
    @ExceptionHandler(MailException.class)
    public ResultJson mailHandle(MailException e){
        ResultJson json=new ResultJson();
        log.error("邮箱异常：{},出现异常:{}",e,e.getMessage());
        json.error( 2002L,"邮箱异常");
        return json;
    }
}
