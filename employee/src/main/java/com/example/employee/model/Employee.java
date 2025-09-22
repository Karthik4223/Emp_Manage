package com.example.employee.model;

import java.time.LocalDateTime;

import com.example.employee.enums.Status;

import lombok.Data;

@Data
public class Employee extends Person {
	private String empCode;
	private String empPassword;
	private String empDepartment;
	private Status employeeStatus;
	private LocalDateTime empCreatedDateTime;
	private LocalDateTime empUpdatedDateTime;	
	private String createdBy;
	private String updatedBy;
}
