package com.eitech1.chartv.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eitech1.chartv.exceptions.ChartVException;
import com.eitech1.chartv.request.dto.AuthenticationRequest;
import com.eitech1.chartv.request.dto.ResetPassRequest;
import com.eitech1.chartv.request.dto.UserRequest;
import com.eitech1.chartv.response.dto.UserDto;
import com.eitech1.chartv.response.template.Response;
import com.eitech1.chartv.respository.UserRepository;
import com.eitech1.chartv.service.AuthService;

import io.swagger.annotations.ApiOperation;

/**
 * @author Nilar
 *
 */


@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private AuthService authService;

	@Autowired
	AuthenticationManager authenticationManager;

	@PostMapping("/signup")
	@ApiOperation("save new user")
	public ResponseEntity<Response<UserDto>> saveUser(@RequestBody UserRequest userRequest) throws ChartVException {
		return authService.saveUser(userRequest);

	}

	@PostMapping("/signin")
	@ApiOperation("login and get jwt token")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws ChartVException {
		return authService.createAuthenticationToken(authenticationRequest);
	}

	@GetMapping("/findme/{username}")
	@ApiOperation("find user account and get OTP through email (forget password)")
	public ResponseEntity<?> findMe(@PathVariable("username") String username) throws ChartVException {
		return authService.findUser(username);
	}

	@GetMapping("/tokenvalidate/{token}/{id}")
	@ApiOperation("validate OTP to reset the password")
	public ResponseEntity<Response<UserDto>> passwordTokenValidation(@PathVariable("id")int id,@PathVariable("token") String token) throws ChartVException {
		return authService.resetTokenValidate(id, token);
	}

	@PostMapping("/resetpassword")
	@ApiOperation("update new password ")
	public ResponseEntity<Response<UserDto>> resetPassword(@RequestBody ResetPassRequest resetRequest) throws ChartVException {
		return authService.resetPassword(resetRequest);
		
	}

	
}
