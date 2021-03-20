package com.andrei.ticket_system.rest_controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeRestController {
    @GetMapping(value = "/greetings")
    public ResponseEntity<String> getGreeting(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "getGreeting");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String message = "Greetings " + authentication.getName() + " you are logged as " + authentication.getAuthorities().toString();
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(message);
    }
}
