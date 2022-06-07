package com.application.constant.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ErrorEnum {
    UNKNOW_ERROR(1000L,"未知错误");
    private Long code;
    private String message;
    ErrorEnum(Long code,String message){
        this.code=code;
        this.message=message;
    }

}
