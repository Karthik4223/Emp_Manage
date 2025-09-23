package com.example.employee.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeRights {
	private String empCode;
	private List<String> rightCode;
    private List<String> rightName;
	private LocalDateTime empRightCreatedDateTime;
	private String createdBy;
	private String updatedBy;
}
