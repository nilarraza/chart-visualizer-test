package com.eitech1.chartv.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.eitech1.chartv.exceptions.ChartVException;
import com.eitech1.chartv.request.dto.UpdateRequest;
import com.eitech1.chartv.response.dto.UserDto;
import com.eitech1.chartv.response.template.Response;

public interface UserService {
	
	public ResponseEntity<Response<List<UserDto>>>  getAllActiveUsers() throws ChartVException;
	public ResponseEntity<Response<List<UserDto>>>  getAllUsers() throws ChartVException;
	public ResponseEntity<Response<UserDto>>  deleteUser(int userid) throws ChartVException;
	public ResponseEntity<Response<UserDto>>  UpdateUser(int id,UpdateRequest userRequest) throws ChartVException;
	

}
