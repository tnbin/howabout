package com.example.howabout.Vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserVo implements Serializable {

    public UserVo(String u_nick, String u_id, String u_pw, String birth, Integer gender,String email) {
        this.u_nick = u_nick;
        this.u_id = u_id;
        this.u_pw = u_pw;
        this.birth = birth;
        this.gender = gender;
        this.email=email;
    }
    public UserVo(){
    }

    @SerializedName("u_nick")
    @Expose
    private String u_nick;
    @SerializedName("u_id")
    @Expose
    private String u_id;
    @SerializedName("u_pw")
    @Expose
    private String u_pw;
    @SerializedName("birth")
    @Expose
    private String birth;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("u_email")
    @Expose
    private String email;

    public String getU_nick() {
        return u_nick;
    }

    public void setU_nick(String u_nick) {
        this.u_nick = u_nick;
    }

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

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "u_nick='" + u_nick + '\'' +
                ", u_id='" + u_id + '\'' +
                ", u_pw='" + u_pw + '\'' +
                ", birth='" + birth + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                '}';
    }
}
