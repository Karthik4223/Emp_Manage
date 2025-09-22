package com.example.employee.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class EmployeeRights {
	private String empCode;
	private List<String> rightCode;
	private Map<String, String> rightCodeAndName;
	private LocalDateTime empRightCreatedDateTime;
	private String createdBy;
	private String updatedBy;
}
