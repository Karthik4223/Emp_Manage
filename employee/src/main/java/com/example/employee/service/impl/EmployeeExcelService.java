package com.example.employee.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.Gender;
import com.example.employee.model.EmployeeRequest;

@Service
public class EmployeeExcelService {

    public List<EmployeeRequest> parseExcel(MultipartFile file,String createdBy) throws EmployeeException {
    	
        List<EmployeeRequest> employees = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) 
            	rows.next();

            while (rows.hasNext()) {
                Row row = rows.next();
                EmployeeRequest emp = new EmployeeRequest();

                emp.setEmail(getCellValue(row, 0).trim());
                emp.setName(getCellValue(row, 1).trim());
                emp.setPhoneNumber(getCellValue(row, 2).trim());
                emp.setGender(Gender.valueOf(getCellValue(row, 3).trim().toUpperCase()));
                emp.setEmpDepartment(getCellValue(row, 4).trim());
                emp.setCountry(getCellValue(row, 5).trim());
                emp.setState(getCellValue(row, 6).trim());
                emp.setCity(getCellValue(row, 7).trim());

                emp.setCreatedBy(createdBy);

                employees.add(emp);
            }
        } catch (IOException e) {
        	throw new EmployeeException("Failed to get Excel File");
		}

        return employees;
    }

    private String getCellValue(Row row, int cellIdx) {
        Cell cell = row.getCell(cellIdx);
        
        if (cell == null) 
        	return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        } else {
            return "";
        }
    }
}
