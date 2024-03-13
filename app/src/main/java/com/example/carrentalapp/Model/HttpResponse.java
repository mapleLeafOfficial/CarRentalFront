package com.example.carrentalapp.Model;

public class HttpResponse {
    private int code;
    private String msg;

    // 无参构造函数
    public HttpResponse() {
    }

    // 带参构造函数
    public HttpResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // Getter和Setter方法
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    // toString方法
    @Override
    public String toString() {
        return "HttpResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
