package com.example.oj.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class Test {

    @MessageMapping("/test")
    public void cc() {
        System.out.println("I miss you");
    }
}
