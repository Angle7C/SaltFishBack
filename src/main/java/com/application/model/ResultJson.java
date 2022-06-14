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
    public ResultJson ok(String message){
        return ok(message,null);
    }
    public ResultJson  ok(String message,T data){return ok(message,data,null);}
    public ResultJson  ok(String message,T data,List<T> list){return ok(1000L,message,data,list);}
    public ResultJson  ok(Long code,String message,T data,List<T> list){
        this.Code=code;this.message=message;this.data=data;this.list=list;
        return this;

    }
    public void error(Long Code,String message){
        error(Code,message,null);
    }
    public void error(Long Code,String message,T data){
        error(Code,message,data,null);

    }
    public void error(Long Code,String message,T data,List<T> list){
        this.Code=Code;this.message=message;this.data=data;this.list=list;
    }
}
