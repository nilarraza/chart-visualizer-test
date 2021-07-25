package com.eitech1.chartv.request.dto;


import javax.persistence.Column;

import lombok.Data;


@Data
public class UserRequest {

	@Column(name = "username")
	private String username;
	
	@Column(name = "email")
	private String email;
	
//	@Column(name = "password")
//	private String password;
	
	@Column(name = "roles")
	private String roles;
	
	

}
