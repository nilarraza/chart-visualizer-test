package com.eitech1.chartv.controller;

import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eitech1.chartv.exceptions.ChartVException;
import com.eitech1.chartv.response.dto.SheetExDto;
import com.eitech1.chartv.response.template.Response;
import com.eitech1.chartv.service.ExcelService;

import io.swagger.annotations.ApiOperation;

/**
 * @author Nilar
 *
 */

@RestController
@RequestMapping("/excel")
@CrossOrigin(origins = "*")
public class ExcelController {
	
	@Autowired
	private ExcelService excelService;
	
	@PostMapping(value="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiOperation("upload the excel sheet to read and save data")
	public ResponseEntity<Response<SheetExDto>> uploadData(@RequestPart("file") MultipartFile multipartFile) throws EncryptedDocumentException, InvalidFormatException, IOException, ChartVException {
		return excelService.readExcel(multipartFile);	
	}
	
	@GetMapping(value="/sheet/{type_id}")
	@ApiOperation("get list of particular type excel sheet by meta id")
	public ResponseEntity<Response<List<SheetExDto>>> getSheets(@PathVariable("type_id") int excelTypeId) throws ChartVException {
		return excelService.getExcel(excelTypeId);
	}
	

}
