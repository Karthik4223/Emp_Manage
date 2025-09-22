package com.example.employee.model;

import java.util.List;

import com.example.employee.enums.Status;

import lombok.Data;

@Data
public class SearchCriteria {
	
	private List<String>  employeeNames;
    private List<String> employeeEmail;
    private List<String> employeeCode;
    private List<String> employeePhoneNumber;
    private List<String> employeeDepartment;
    private String searchKey;
    private Status employeeStatus;
  
}
