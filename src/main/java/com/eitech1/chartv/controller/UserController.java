package com.eitech1.chartv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eitech1.chartv.exceptions.ChartVException;
import com.eitech1.chartv.request.dto.UpdateRequest;
import com.eitech1.chartv.response.dto.UserDto;
import com.eitech1.chartv.response.template.Response;
import com.eitech1.chartv.service.UserService;

import io.swagger.annotations.ApiOperation;

/**
 * @author Nilar
 *
 */


@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	@ApiOperation("get all the active users")
	public ResponseEntity<Response<List<UserDto>>> getAllActiveUsers() throws ChartVException{
	return	userService.getAllActiveUsers();
	}
	
	@GetMapping("/all")
	@ApiOperation("get all the users from system")
	public ResponseEntity<Response<List<UserDto>>> getAllUsers() throws ChartVException{
	return	userService.getAllUsers();
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation("delete a selected user")
	public ResponseEntity<Response<UserDto>> deleteUser(@PathVariable("id") int userId) throws ChartVException{
	return	userService.deleteUser(userId);
	}
	
	@PutMapping("/update/{id}")
	@ApiOperation("update user details")
	public ResponseEntity<Response<UserDto>> updateUser(@PathVariable("id") int id,@RequestBody UpdateRequest updateRequest) throws ChartVException{
	return	userService.UpdateUser(id, updateRequest);
	}
}
