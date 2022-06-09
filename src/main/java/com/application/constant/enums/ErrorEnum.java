package com.application.constant.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ErrorEnum {
    UNKNOW_ERROR(2000L,"未知错误"),
    ARGUMENT_ERROR(2001L,"校验参数异常");
    private Long code;
    private String message;
    ErrorEnum(Long code,String message){
        this.code=code;
        this.message=message;
    }

}
