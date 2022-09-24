package com.example.howabout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginVo {

    public LoginVo(String u_id, String u_pw) {
        this.u_id = u_id;
        this.u_pw = u_pw;
    }

    @SerializedName("u_id")
    @Expose
    private String u_id;
    @SerializedName("u_pw")
    @Expose
    private String u_pw;

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_pw() {
        return u_pw;
    }

    public void setU_pw(String u_pw) {
        this.u_pw = u_pw;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "u_id='" + u_id + '\'' +
                ", u_pw='" + u_pw + '\'' +
                '}';
    }
}
