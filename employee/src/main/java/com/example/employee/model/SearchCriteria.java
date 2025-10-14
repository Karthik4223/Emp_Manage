package com.example.employee.model;

import com.example.employee.enums.Status;

import lombok.Data;

@Data
public class SearchCriteria {
	
	private String  employeeNames;
    private String employeeEmail;
    private String employeeCode;
    private String employeePhoneNumber;
    private String employeeDepartment;
    private String searchKey;
    private Status employeeStatus;
  
}
