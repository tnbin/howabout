package com.example.howabout.functions;

public class User {
    //비밀번호 일치 확인
    public int check_pw(String pw, String rePw){
        int result;
        if(pw.equals(rePw)) {
            result = 1;
        }else {
            result = 0;
        }
        return result;
    }
}
