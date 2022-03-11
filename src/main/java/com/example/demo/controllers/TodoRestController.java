package com.example.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class TodoRestController {

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(value = "/getTodos", method = RequestMethod.GET)
    public ResponseEntity<String> getTodos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);
        return new ResponseEntity<>("Ops", HttpStatus.OK);
    }

}
