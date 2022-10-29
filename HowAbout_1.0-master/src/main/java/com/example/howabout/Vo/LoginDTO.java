package com.example.howabout.Vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDTO {

    @SerializedName("success")
    @Expose
    private int success;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("token")
    @Expose
    private String token;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
