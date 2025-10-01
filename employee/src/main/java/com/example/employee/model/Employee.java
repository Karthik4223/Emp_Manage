package com.example.employee.model;

import com.example.employee.enums.Status;

import lombok.Data;

@Data
public class Employee extends Person {
	private String empCode;
	private Status employeeStatus;
}
