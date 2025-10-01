package com.example.employee.model;

import java.time.LocalDateTime;

import com.example.employee.enums.Gender;

import lombok.Data;

@Data
public class Person {
	private String email;
	private String name;
	private String phoneNumber;
	private Gender gender;
	private String country;
	private String state;
	private String city;
	
	private String empPassword;
	private String empDepartment;
	private LocalDateTime empCreatedDateTime;
	private LocalDateTime empUpdatedDateTime;	
	private String createdBy;
	private String updatedBy;
	
}
