package com.eitech1.chartv.service.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.eitech1.chartv.Entity.DataSet;
import com.eitech1.chartv.Entity.SheetEx;
import com.eitech1.chartv.Entity.Tab;
import com.eitech1.chartv.Entity.User;
import com.eitech1.chartv.request.dto.ResetPassRequest;
import com.eitech1.chartv.request.dto.UpdateRequest;
import com.eitech1.chartv.request.dto.UserRequest;
import com.eitech1.chartv.respository.SheetMetaRepository;

@Component
public class DtoToEntityMapper {
	
	@Autowired
	private SheetMetaRepository sheetMetaRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public DataSet convertToDataSet(String jsonData,Tab tab) {
		return DataSet.builder()
				.jsonData(jsonData)
				.tab(tab)
				.build();
		
	}
	
	public Tab convertToTab(String tabName,SheetEx sheet) {
		return Tab.builder()
				.tabName(tabName)
				.sheet(sheet)
				.build();
	}
	public SheetEx converToSheet(int id) {
		return SheetEx.builder()
				.uploadedDate(new Date())
				.sheetMeta(sheetMetaRepository.getOne(id))
				.build();
		
	}
	
	
	public User convertToUserOtp(User user,String token) {
		user.setToken(token);
		user.setOtpRequestedTime(new Date());
		
		return user;	
	}
	
	public User convertToUser(UserRequest user,String password) {
		System.out.println(user.getUsername());
		System.out.println(password);
		return User.builder()
				.username(user.getUsername())
				.email(user.getEmail())
				.password(passwordEncoder.encode(password))
				.roles(user.getRoles())
				.isActive(true)
				.build();
		
	}
	
	public User convertToResetUser(User user,ResetPassRequest resetRequest) {
		return User.builder()
				.userid(user.getUserid())
				.username(user.getUsername())
				.email(user.getEmail())
				.password(passwordEncoder.encode(resetRequest.getPassword()))
				.roles(user.getRoles())
				.isActive(true)
				.build();
		
	}
	
	public User convertToUpdateUser(UpdateRequest upRequest,User fetchedUser) {
		fetchedUser.setUsername(upRequest.getUsername());
		fetchedUser.setEmail(upRequest.getEmail());
		fetchedUser.setRoles(upRequest.getRole());
		fetchedUser.setActive(upRequest.isActive());
		return fetchedUser;
	}
	

}
