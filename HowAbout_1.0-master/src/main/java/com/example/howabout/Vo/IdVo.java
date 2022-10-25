package com.example.howabout.Vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IdVo {


    public IdVo(String u_id) {
        this.u_id = u_id;
    }

    @SerializedName("u_id")
    @Expose
    private String u_id;

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    @Override
    public String toString() {
        return "IdVo{" +
                "u_id='" + u_id + '\'' +
                '}';
    }
}
