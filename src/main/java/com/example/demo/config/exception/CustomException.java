package com.example.demo.config.exception;
/*
* 自定义异常
*/
public class CustomException  extends RuntimeException{
    //返回状态码
    private Integer code;
    //返回异常
    private String msg;

    public CustomException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
