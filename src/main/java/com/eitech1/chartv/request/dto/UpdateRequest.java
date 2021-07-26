package com.eitech1.chartv.request.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UpdateRequest {
	
	@NonNull
	private String username;
	@NonNull
	private String email;
	@NonNull
	private String role;
	private boolean isActive;	

}
