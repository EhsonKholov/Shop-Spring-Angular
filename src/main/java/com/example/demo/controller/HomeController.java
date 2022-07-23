package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object index() {

        return "dist/app-test/index.html";
    }

}
