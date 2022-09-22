package com.example.howabout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserVo {
    public UserVo(String u_nick, String u_id, String u_pw, Integer birthYear, Integer birthMonth, Integer birthDay, Integer gender) {
        this.u_nick = u_nick;
        this.u_id = u_id;
        this.u_pw = u_pw;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.gender = gender;
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
    @SerializedName("birthYear")
    @Expose
    private Integer birthYear;
    @SerializedName("birthMonth")
    @Expose
    private Integer birthMonth;
    @SerializedName("birthDay")
    @Expose
    private Integer birthDay;
    @SerializedName("gender")
    @Expose
    private Integer gender;

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

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(Integer birthMonth) {
        this.birthMonth = birthMonth;
    }

    public Integer getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Integer birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "u_nick='" + u_nick + '\'' +
                ", u_id='" + u_id + '\'' +
                ", u_pw='" + u_pw + '\'' +
                ", birthYear=" + birthYear +
                ", birthMonth=" + birthMonth +
                ", birthDay=" + birthDay +
                ", gender=" + gender +
                '}';
    }
}
