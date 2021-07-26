package com.eitech1.chartv.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eitech1.chartv.exceptions.ChartVDataValidationException;
import com.eitech1.chartv.exceptions.ChartVException;
import com.eitech1.chartv.respository.UserRepository;

@Component
public class UserValidation {
	@Autowired
	private UserRepository userRepository;

	public void validateUser(String username)throws ChartVException {
		boolean valid = userRepository.existsByUsername(username);
		System.out.println("test username :" + valid);
		if (valid) {
			throw new ChartVDataValidationException("Username Already Exists", username);
		}
	}
	
	public void validateUserEmail(String email)throws ChartVException {
		boolean valid = userRepository.existsByEmail(email);
		System.out.println("test email: "+valid);
		if (valid) {
			throw new ChartVDataValidationException("Email Already Exists", email);
		}
	}
	
	public void validateUsername(String username)throws ChartVException {
		boolean valid = userRepository.existsByUsername(username);
		System.out.println("test username :" + valid);
		if (!valid) {
			throw new ChartVDataValidationException("Username Not Found", username);
		}
	}
	
	public void validateUserById(int id)throws ChartVException {
		boolean valid = userRepository.existsById(id);
		if (!valid) {
			throw new ChartVDataValidationException("User not found for this id", id);
		}
	}
}
