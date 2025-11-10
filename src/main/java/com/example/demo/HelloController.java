package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "API funcionando! 🚀";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World via Docker!";
    }
}
