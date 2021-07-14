package com.eitech1.chartv.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.eitech1.chartv.Entity.User;
import com.eitech1.chartv.service.AdminService;

@RestController
public class AdminController {
    public AdminService adminservice;

    public AdminController(AdminService adminservice) {
        this.adminservice = adminservice;
    }

    @PostMapping(path = "/admin/register")
    public User createUser(@RequestBody User user) {
        return adminservice.createUser(user);
    }

}