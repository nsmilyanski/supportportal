package com.example.supportportal.controller;

import com.example.supportportal.exception.ExceptionHandling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController extends ExceptionHandling {


    @GetMapping("/test")
    public String getFromSecureEndpoint() {
        return "Hi from Spring Security";
    }

}
