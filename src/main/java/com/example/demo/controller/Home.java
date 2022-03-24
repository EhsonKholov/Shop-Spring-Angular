package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class Home {


    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ResponseEntity<?> Home() {

        return new ResponseEntity<>("Home page", HttpStatus.OK);
    }

}
