package com.springproject.ecommercecore.controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @PostMapping("/echo")
    public String echo(@RequestBody String requestBody) {
        System.out.println("Raw JSON recibido: " + requestBody);  // ✅ Ver qué JSON llega
        return requestBody;
    }
}
