package com.eitech1.chartv.response.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
	
	private int userid;
	private String username;
	private String password;
	private String email;
	private String roles;
	private boolean isActive;

}
