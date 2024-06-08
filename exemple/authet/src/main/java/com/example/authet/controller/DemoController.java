package com.example.authet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("user simple");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("adminurl");
    }
}
