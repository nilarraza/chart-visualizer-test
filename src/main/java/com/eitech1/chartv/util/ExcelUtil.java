package com.eitech1.chartv.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import com.eitech1.chartv.exceptions.ChartVException;
import com.eitech1.chartv.messages.ResponseMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ExcelUtil {

	public Workbook getExcel(InputStream input) throws ChartVException {

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook;
		try {
			//workbook = WorkbookFactory.create(new File(excelPath));
			workbook = WorkbookFactory.create(input);
		} catch (EncryptedDocumentException e) {
			throw new ChartVException(ResponseMessages.ENCRYPTED_FILE, e.getCause());
		} catch (InvalidFormatException e) {
			throw new ChartVException(ResponseMessages.INCORRECT_FILE_FORMAT, e.getCause());
		} catch (Exception e) {
			throw new ChartVException(ResponseMessages.UNEXPECTED_ERROR, e.getCause());
		}
	
		return workbook;
	}

	public String createRowJson(List<String> excelData, List<String> headerList) throws ChartVException {

		HashMap<String, Double> map = new HashMap<String, Double>();

		int i = 0;
		for (String cellvalue : excelData) {
			map.put(headerList.get(i),(double) Math.round(Double.parseDouble(cellvalue)));
			i++;
		}

		String json;
		try {
			json = new ObjectMapper().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			throw new ChartVException(e.getCause());
		}
		System.out.println(json);

		return json;
	}

}
