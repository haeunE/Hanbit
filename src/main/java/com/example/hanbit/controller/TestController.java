package com.example.hanbit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/hanbit/test")
    public String test() {
        return "안녕하세요. test 중입니다.";
    }
}
