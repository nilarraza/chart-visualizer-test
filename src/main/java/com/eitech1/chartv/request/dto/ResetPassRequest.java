package com.eitech1.chartv.request.dto;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ResetPassRequest {
	
	@Column(name = "user_id")
	private int id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "new_password")
	private String password;
	

}
