package com.example.employee.model;

import com.example.employee.enums.EmployeeRequestStatus;

import lombok.Data;

@Data
public class EmployeeRequest extends Person{
	private Integer empRequestId;
	private EmployeeRequestStatus empRequestStatus;
}
