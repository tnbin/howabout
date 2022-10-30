package com.example.howabout.DTO;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NickNameVo {

    public NickNameVo(String u_nick) {
        this.u_nick = u_nick;
    }

    @SerializedName("u_nick")
    @Expose
    private String u_nick;

    public String getU_nick() {
        return u_nick;
    }

    public void setU_nick(String u_nick) {
        this.u_nick = u_nick;
    }

    @Override
    public String toString() {
        return "NickNameVo{" +
                "u_nick='" + u_nick + '\'' +
                '}';
    }
}

