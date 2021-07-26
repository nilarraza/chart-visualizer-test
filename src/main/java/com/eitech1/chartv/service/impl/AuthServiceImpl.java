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
	private EntityToDtoMapper entityToDtoMapper;
	
	@Autowired
	private DtoToEntityMapper dtoToEntityMapper;
	
	@Autowired
	private EmailUtil emailUtil;
	
	private static final long OTP_VALID_DURATION= 5 *60 * 1000; //5 minutes
	

	@Override
	public ResponseEntity<Response<UserDto>> saveUser(UserRequest userRequest) throws ChartVException {
		
		try {
			userValidation.validateUser(userRequest.getUsername());
			userValidation.validateUserEmail(userRequest.getEmail());

			 Random r = new Random();
			    String password = String.format("%06d", r.nextInt(100001));
			    
			    emailUtil.sendEmail(
						userRequest.getEmail(), 
						"Login Credencials \n", 
						"Please find your login credencials here \n"+ 
						"username :"+ userRequest.getUsername() +"\n"+ 
						"password :"+ password);
			
			User user = userRepository.save(dtoToEntityMapper.convertToUser(userRequest,password));
		UserDto userDTO =	entityToDtoMapper.convertUserDto(user);
			
		
		
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
	public ResponseEntity<Response<UserDto>> findUser(String username) throws ChartVException {
		
		try {
			userValidation.validateUsername(username);
			User fetched = userRepository.findByUsername(username);			
			String token=generateToken();
			    
			User otpUser = dtoToEntityMapper.convertToUserOtp(fetched, token);
			userRepository.save(otpUser);
			UserDto user=entityToDtoMapper.convertUserDto(otpUser);
					
			emailUtil.sendEmail(
					fetched.getEmail(), 
					"Password Reset Token", 
					"Your Password Reset Token :  "+token);
			
			return Response.success(user, HttpStatus.OK);
			
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

			throw new ChartVException(ResponseMessages.UNEXPECTED_ERROR, e.getCause());
			//throw new ChartVException(e);
		
		}

	}

	@Override
	public ResponseEntity<Response<UserDto>> resetTokenValidate(int userId,String token) throws ChartVException {
			try {
			commonValidation.validateResetToken(userId, token);
			User fetchedUser = userRepository.findByuserid(userId);
			
			long currentTimeMillis = System.currentTimeMillis();
			long otpRequestedTimeMillis = fetchedUser.getOtpRequestedTime().getTime();
			
			if(otpRequestedTimeMillis + OTP_VALID_DURATION < currentTimeMillis ) {
				fetchedUser.setOtpRequestedTime(null);
				fetchedUser.setToken(null);
				userRepository.save(fetchedUser);
				throw new ChartVException(ResponseMessages.OTP_EXP);
			}
			
			UserDto userDTO = entityToDtoMapper.convertUserDto(fetchedUser);
			fetchedUser.setOtpRequestedTime(null);
			fetchedUser.setToken(null);
			userRepository.save(fetchedUser);
			
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
			emailUtil.sendEmail(
					user.getEmail(),
					"Updated Password", 
					"your new password : "+ userReset.getPassword());
			
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
