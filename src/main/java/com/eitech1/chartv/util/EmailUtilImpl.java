package com.eitech1.chartv.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.eitech1.chartv.exceptions.ChartVException;
import com.eitech1.chartv.messages.ResponseMessages;



@Component
public class EmailUtilImpl implements EmailUtil {
	
	@Autowired
	private JavaMailSender sender;

	@Override
	public void sendEmail(String ToAddress, String subject, String body) throws ChartVException {
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo(ToAddress);
			helper.setSubject(subject);
			helper.setText(body);
			
		} catch (MessagingException e) {
			throw new ChartVException(ResponseMessages.UNEXPECTED_ERROR, e.getCause());
		}
		
		sender.send(message);

	}

}
