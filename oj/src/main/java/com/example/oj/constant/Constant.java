package com.example.oj.constant;

public class Constant {

    public static String getId(String email) {
        String id = "";
        for(int i = 0; i < email.length(); i++) {
            if(email.charAt(i) == '@') break;
            id += email.charAt(i);
        }
        return id;
    }
}
