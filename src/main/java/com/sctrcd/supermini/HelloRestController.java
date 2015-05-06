package com.sctrcd.supermini;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestController {
    
    public HelloRestController() {
    }
    
    @RequestMapping(value = "/hello")
    public String countries() {
        return "Hello, World!";
    }

}
