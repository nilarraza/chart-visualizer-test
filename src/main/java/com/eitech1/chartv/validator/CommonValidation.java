package com.eitech1.chartv.validator;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eitech1.chartv.exceptions.ChartVDataValidationException;
import com.eitech1.chartv.respository.ResetTokenRepository;
import com.eitech1.chartv.respository.SheetMetaRepository;

@Component
public class CommonValidation {
	
	@Autowired
	private SheetMetaRepository sheetMetaRepository;
	
	@Autowired
	private ResetTokenRepository resetTokenRepository;

	
	public void validateSheetMeta(int metaId) throws ChartVDataValidationException   {
		boolean valid = sheetMetaRepository.existsById(metaId);
		if (!valid) {
			throw new ChartVDataValidationException("Invalid Sheet Type Id", metaId);
		}
	}
	
	public void validateResetToken(String token) throws ChartVDataValidationException   {
		boolean valid = resetTokenRepository.existsByToken(token);
		if (!valid) {
			throw new ChartVDataValidationException("Password Reset Token is Invalid", token);
		}
	}
	
	public String validateToken(String token) throws ChartVDataValidationException   {
		boolean valid = resetTokenRepository.existsByToken(token);
		if (!valid) {
			return token;
		}else {
			 Random r = new Random();
			    String resetToken = String.format("%06d", r.nextInt(100001));
			    return resetToken;
		}
	}
	
}
