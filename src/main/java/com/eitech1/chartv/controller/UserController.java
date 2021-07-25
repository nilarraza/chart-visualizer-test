package com.eitech1.chartv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eitech1.chartv.exceptions.ChartVException;
import com.eitech1.chartv.response.dto.UserDto;
import com.eitech1.chartv.response.template.Response;
import com.eitech1.chartv.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<Response<List<UserDto>>> getAllActiveUsers() throws ChartVException{
	return	userService.getAllActiveUsers();
	}

}
