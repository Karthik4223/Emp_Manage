package com.example.employee.model;

import java.time.LocalDateTime;

import com.example.employee.enums.EmployeeRequestStatus;

import lombok.Data;

@Data
public class EmployeeRequest extends Person{
	private Integer empRequestId;
	private String empDepartment;
	private EmployeeRequestStatus empRequestStatus;
	private LocalDateTime empCreatedDateTime;
	private LocalDateTime empUpdatedDateTime;
	private String createdBy;
	private String updatedBy;

}
