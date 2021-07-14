package com.eitech1.chartv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.eitech1.chartv.Entity.User;
import com.eitech1.chartv.service.UserService;


@RestController
public class UserController {
    @Autowired
    public UserService userservice;

    public UserController(UserService userservice) {
        this.userservice = userservice;
    }

    @GetMapping(path = "/user/{id}")
    public User getById(@PathVariable("id") Long id) {
        return userservice.getById(id);
    }

}