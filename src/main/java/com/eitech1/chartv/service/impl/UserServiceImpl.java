package com.eitech1.chartv.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eitech1.chartv.Entity.User;
import com.eitech1.chartv.exceptions.ChartVException;
import com.eitech1.chartv.messages.ResponseMessages;
import com.eitech1.chartv.response.dto.UserDto;
import com.eitech1.chartv.response.template.Response;
import com.eitech1.chartv.respository.UserRepository;
import com.eitech1.chartv.service.UserService;
import com.eitech1.chartv.service.util.EntityToDtoMapper;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EntityToDtoMapper entityToDtoMapper;

	@Override
	public ResponseEntity<Response<List<UserDto>>> getAllActiveUsers() throws ChartVException {
		try {
			List<UserDto> userRes=new ArrayList<>();
			
			for(User user:userRepository.findByActive()) {
				userRes.add(entityToDtoMapper.convertUserDto(user));
			}
			return Response.success(userRes, HttpStatus.OK);
			
		}catch (Exception e) {
			throw new ChartVException(ResponseMessages.UNEXPECTED_ERROR, e.getCause());
		}
		
	}

}
