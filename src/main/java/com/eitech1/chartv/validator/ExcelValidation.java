package com.eitech1.chartv.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eitech1.chartv.Entity.SheetMeta;
import com.eitech1.chartv.exceptions.ChartVDataValidationException;
import com.eitech1.chartv.respository.SheetMetaRepository;

@Component
public class ExcelValidation {

    String sheet = "Telecoms Adspend Forecasts.xlsx";

    @Autowired
    private SheetMetaRepository sheetMetaRepository;

    public void validateColumn(List<String> headerList, List<String> rowValueList) throws ChartVDataValidationException {

        if (headerList.size() != rowValueList.size()) {
            throw new ChartVDataValidationException(
                    "length of header columns and length of row columns are different ", headerList + " " + rowValueList);
        }

    }

    public void validateSheetName(String sheetName, int typeId) throws ChartVDataValidationException {
        SheetMeta sheetMeta = sheetMetaRepository.getOne(typeId);

        if (sheetName.equals(sheetMeta.getSheetName() + ".xlsx") == false) {
            throw new ChartVDataValidationException("Sheet name should follow the structure", sheetMeta.getSheetName());
        }

    }

}
