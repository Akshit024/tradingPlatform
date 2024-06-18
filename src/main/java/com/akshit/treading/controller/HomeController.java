package com.akshit.treading.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/welcome")
    public String home(){
        return "welcome to home page";
    }

    @GetMapping("/api")
    public String secure(){
        return "welcome to secure page";
    }
}
