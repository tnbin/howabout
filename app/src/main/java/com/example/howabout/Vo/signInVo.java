package com.example.howabout.Vo;

public class signInVo {
    public signInVo(int success, String msg, UserVo userVo) {
        this.success = success;
        this.msg = msg;
        this.userVo = userVo;
    }

    private int success;
    private String msg;
    private UserVo userVo;

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

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    @Override
    public String toString() {
        return "signInVo{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", userVo=" + userVo +
                '}';
    }
}
