package com.application.model;

import com.application.constant.enums.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultJson<T> {
    private Long Code;
    private String message;
    private T data;
    private List<T> list;
    public ResultJson(ErrorEnum errorEnum){
        this.Code=errorEnum.getCode();
        this.message=errorEnum.getMessage();
    }
}
