package com.eitech1.chartv.util;

import com.eitech1.chartv.exceptions.ChartVException;

public interface EmailUtil {
	void sendEmail(String ToAddress,String subject,String body)throws ChartVException;
}
