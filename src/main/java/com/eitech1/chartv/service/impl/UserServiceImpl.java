package com.eitech1.chartv.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eitech1.chartv.Entity.User;
import com.eitech1.chartv.exceptions.ChartVException;
import com.eitech1.chartv.exceptions.ChartVPersistenceException;
import com.eitech1.chartv.messages.ResponseMessages;
import com.eitech1.chartv.request.dto.UpdateRequest;
import com.eitech1.chartv.response.dto.UserDto;
import com.eitech1.chartv.response.template.Response;
import com.eitech1.chartv.respository.UserRepository;
import com.eitech1.chartv.service.UserService;
import com.eitech1.chartv.service.util.DtoToEntityMapper;
import com.eitech1.chartv.service.util.EntityToDtoMapper;
import com.eitech1.chartv.validator.UserValidation;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EntityToDtoMapper entityToDtoMapper;

	@Autowired
	private DtoToEntityMapper dtoToEntityMapper;

	@Autowired
	private UserValidation userValidation;

	@Override
	public ResponseEntity<Response<List<UserDto>>> getAllActiveUsers() throws ChartVException {
		try {
			List<UserDto> userRes = new ArrayList<>();

			for (User user : userRepository.findByActive()) {
				userRes.add(entityToDtoMapper.convertUserDto(user));
			}
			return Response.success(userRes, HttpStatus.OK);

		} catch (Exception e) {
			throw new ChartVException(ResponseMessages.UNEXPECTED_ERROR, e.getCause());
		}

	}

	@Override
	public ResponseEntity<Response<List<UserDto>>> getAllUsers() throws ChartVException {
		try {
			List<UserDto> userRes = new ArrayList<>();

			for (User user : userRepository.findAll()) {
				userRes.add(entityToDtoMapper.convertUserDto(user));
			}
			return Response.success(userRes, HttpStatus.OK);

		} catch (Exception e) {
			throw new ChartVException(ResponseMessages.UNEXPECTED_ERROR, e.getCause());
		}

	}

	@Override
	public ResponseEntity<Response<UserDto>> deleteUser(int userid) throws ChartVException {
		try {
			userRepository.deleteById(userid);
			return Response.success(null, HttpStatus.OK);
		} catch (Exception e) {
			throw new ChartVException(ResponseMessages.UNEXPECTED_ERROR, e.getCause());
		}

	}

	@Override
	public ResponseEntity<Response<UserDto>> UpdateUser(int id, UpdateRequest userRequest) throws ChartVException {
		try {
			userValidation.validateUserById(id);

			User fetchedUser = userRepository.findByuserid(id);
			User updatedUser = userRepository.save(dtoToEntityMapper.convertToUpdateUser(userRequest, fetchedUser));
			UserDto userDTO = entityToDtoMapper.convertUserDto(updatedUser);

			return Response.success(userDTO, HttpStatus.OK);
		} catch (ChartVException e) {
			throw e;
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException
					&& e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {

				org.hibernate.exception.ConstraintViolationException hibernateException = (ConstraintViolationException) e
						.getCause();

				throw new ChartVPersistenceException(hibernateException.getCause().getLocalizedMessage(),
						hibernateException.getCause());

			}

			throw new ChartVException(ResponseMessages.UNEXPECTED_ERROR, e.getCause());
		}

	}

}
