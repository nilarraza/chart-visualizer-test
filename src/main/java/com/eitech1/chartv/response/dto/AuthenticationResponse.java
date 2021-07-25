package com.eitech1.chartv.response.dto;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {
	
	 private final String jwt;
	    private final UserDetails user;

}
