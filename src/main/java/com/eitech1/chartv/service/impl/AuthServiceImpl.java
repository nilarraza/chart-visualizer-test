package com.eitech1.chartv.service.impl;

import java.util.Random;

import javax.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.eitech1.chartv.Entity.ResetToken;
import com.eitech1.chartv.Entity.User;
import com.eitech1.chartv.exceptions.ChartVException;
import com.eitech1.chartv.exceptions.ChartVPersistenceException;
import com.eitech1.chartv.messages.ResponseMessages;
import com.eitech1.chartv.request.dto.AuthenticationRequest;
import com.eitech1.chartv.request.dto.ResetPassRequest;
import com.eitech1.chartv.request.dto.UserRequest;
import com.eitech1.chartv.response.dto.AuthenticationResponse;
import com.eitech1.chartv.response.dto.UserDto;
import com.eitech1.chartv.response.template.Response;
import com.eitech1.chartv.respository.ResetTokenRepository;
import com.eitech1.chartv.respository.UserRepository;
import com.eitech1.chartv.service.AuthService;
import com.eitech1.chartv.service.MyUserDetailsService;
import com.eitech1.chartv.service.util.DtoToEntityMapper;
import com.eitech1.chartv.service.util.EntityToDtoMapper;
import com.eitech1.chartv.util.EmailUtil;
import com.eitech1.chartv.util.JwtUtil;
import com.eitech1.chartv.validator.CommonValidation;
import com.eitech1.chartv.validator.UserValidation;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserValidation userValidation;
	
	@Autowired
	private CommonValidation commonValidation;
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private ResetTokenRepository resetTokenRepository;
	
	@Autowired
	private EntityToDtoMapper entityToDtoMapper;
	
	@Autowired
	private DtoToEntityMapper dtoToEntityMapper;
	
	@Autowired
	private EmailUtil emailUtil;
	

	@Override
	public ResponseEntity<Response<UserDto>> saveUser(UserRequest userRequest) throws ChartVException {
		
		try {
			userValidation.validateUser(userRequest.getUsername());
			userValidation.validateUserEmail(userRequest.getEmail());
			
//			String pass = userRequest.getPassword();
//			String encpass = passwordEncoder.encode(pass);
//			userRequest.setPassword(encpass);
			
			//dtoToEntityMapper.convertToUser(userRequest);
			 Random r = new Random();
			    String password = String.format("%06d", r.nextInt(100001));
			
			User user = userRepository.save(dtoToEntityMapper.convertToUser(userRequest,password));
		UserDto userDTO =	entityToDtoMapper.convertUserDto(user);
			
		emailUtil.sendEmail(
				user.getEmail(), 
				"Login Credencials \n", 
				"Please find your login credencials here \n"+ 
				"username :"+ user.getUsername() +"\n"+ 
				"password :"+ password);
		
			return Response.success(userDTO, HttpStatus.CREATED);
			
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

	@Override
	public ResponseEntity<Response<AuthenticationResponse>> createAuthenticationToken(AuthenticationRequest authenticationRequest)
			throws ChartVException {
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new ChartVException(ResponseMessages.USERNAME_PASSWORD, e);
			
		}catch (Exception e) {
			throw new ChartVException(ResponseMessages.UNEXPECTED_ERROR, e.getCause());
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		final UserDetails user = userDetails;
		
		return Response.success(new AuthenticationResponse(jwt, user), HttpStatus.OK);
	}
	
	
	

	@Override
	@Transactional
	public ResponseEntity<Response<ResetToken>> findUser(String username) throws ChartVException {
		
		try {
			userValidation.validateUsername(username);
			
			User user = userRepository.findByUsername(username);

	//		resetTokenRepository.deleteByUser(user);  //need to delete
			
		
			 String token=commonValidation.validateToken(generateToken());
			    
			ResetToken resetTokenEntity = dtoToEntityMapper.convertToResetToken(user,token );
			resetTokenRepository.save(resetTokenEntity);
					
			emailUtil.sendEmail(
					user.getEmail(), 
					"Password Reset Token", 
					"Your Password Reset Token :  "+token);
			
			return Response.success(resetTokenEntity, HttpStatus.OK);
			
		} catch (ChartVException e) {
			throw e;
		}catch (Exception e) {
			
			if (e instanceof org.springframework.dao.DataIntegrityViolationException
					&& e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {

				org.hibernate.exception.ConstraintViolationException hibernateException = (ConstraintViolationException) e
						.getCause();

				throw new ChartVPersistenceException(hibernateException.getCause().getLocalizedMessage(),
						hibernateException.getCause());

			}

			//throw new ChartVException(ResponseMessages.UNEXPECTED_ERROR, e.getCause());
			throw new ChartVException(e);
		
		}

	}

	@Override
	public ResponseEntity<Response<UserDto>> resetTokenValidate(String token) throws ChartVException {
			try {
			commonValidation.validateResetToken(token);
			
			ResetToken resetToken =	resetTokenRepository.findByToken(token);
			User user=resetToken.getUser();
			UserDto userDTO = entityToDtoMapper.convertUserDto(user);
			
			resetTokenRepository.delete(resetToken); 		//need to delete
			resetTokenRepository.deleteById(resetToken.getId()); 		//need to delete

			
			return Response.success(userDTO, HttpStatus.OK);
			
		} catch (ChartVException e) {
			throw e;
		}catch (Exception e) {
			throw new ChartVException(ResponseMessages.UNEXPECTED_ERROR, e.getCause());
			//throw new ChartVException(e);
		}		
	}

	@Override
	public ResponseEntity<Response<UserDto>> resetPassword(ResetPassRequest resetRequest) throws ChartVException {
	
		try {
			
			User user = userRepository.findByuserid(resetRequest.getId());
			User userReset = dtoToEntityMapper.convertToResetUser(user, resetRequest);
			userRepository.save(userReset);
			UserDto userDTO = entityToDtoMapper.convertUserDto(userReset);
			
			return Response.success(userDTO,HttpStatus.OK);
			
		} catch (Exception e) {
			throw new ChartVException(ResponseMessages.UNEXPECTED_ERROR, e.getCause());
		}
		
	}
	
	public String generateToken() {
		
		 Random r = new Random();
		    String resetToken = String.format("%04d", r.nextInt(1001));
		    System.out.println(resetToken);
		    
		return resetToken;	
	}

	




}
