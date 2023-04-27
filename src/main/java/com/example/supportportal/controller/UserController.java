package com.example.supportportal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {


    @GetMapping("/test")
    public String getFromSecureEndpoint() {
        return "Hi from Spring Security";
    }

}
