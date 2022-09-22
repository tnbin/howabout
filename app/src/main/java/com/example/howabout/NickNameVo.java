package com.example.howabout;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NickNameVo {

    public NickNameVo(String data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NickNameVo{" +
                "data='" + data + '\'' +
                '}';
    }
}

