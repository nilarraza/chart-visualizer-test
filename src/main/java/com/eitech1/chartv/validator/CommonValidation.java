package com.eitech1.chartv.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eitech1.chartv.exceptions.ChartVDataValidationException;
import com.eitech1.chartv.respository.SheetMetaRepository;
import com.eitech1.chartv.respository.UserRepository;

@Component
public class CommonValidation {
	
	@Autowired
	private SheetMetaRepository sheetMetaRepository;
	
	@Autowired
	private UserRepository userRepository;

	
	public void validateSheetMeta(int metaId) throws ChartVDataValidationException   {
		boolean valid = sheetMetaRepository.existsById(metaId);
		if (!valid) {
			throw new ChartVDataValidationException("Invalid Sheet Type Id", metaId);
		}
	}
	
	public void validateResetToken(Integer id,String token) throws ChartVDataValidationException   {
		boolean valid = userRepository.existsByUseridAndToken(id, token);
		if (!valid) {
			throw new ChartVDataValidationException("OTP is Invalid", token);
		}
	}
	
}
