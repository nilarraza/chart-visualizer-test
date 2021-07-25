package com.eitech1.chartv.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eitech1.chartv.Entity.ResetToken;
import com.eitech1.chartv.exceptions.ChartVException;
import com.eitech1.chartv.request.dto.AuthenticationRequest;
import com.eitech1.chartv.request.dto.ResetPassRequest;
import com.eitech1.chartv.request.dto.UserRequest;
import com.eitech1.chartv.response.dto.AuthenticationResponse;
import com.eitech1.chartv.response.dto.UserDto;
import com.eitech1.chartv.response.template.Response;

@Service
public interface AuthService {
	
	ResponseEntity<Response<UserDto>> saveUser(UserRequest userRequest)throws ChartVException;
	ResponseEntity<Response<AuthenticationResponse>> createAuthenticationToken(AuthenticationRequest authenticationRequest)throws ChartVException;
	ResponseEntity<Response<ResetToken>> findUser(String username)throws ChartVException;
	ResponseEntity<Response<UserDto>> resetTokenValidate(String token)throws ChartVException;
	ResponseEntity<Response<UserDto>> resetPassword(ResetPassRequest resetRequest)throws ChartVException;

}
