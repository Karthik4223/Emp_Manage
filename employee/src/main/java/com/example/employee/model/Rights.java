package com.example.employee.model;

import java.time.LocalDateTime;

import com.example.employee.enums.Status;

import lombok.Data;

@Data
public class Rights {
	private String rightCode;
	private String rightName;
	private Status rightStatus;
	private String group;
	private LocalDateTime rightCreatedDateTime;
	private LocalDateTime rightUpdatedDateTime;
	private String createdBy;
	private String updatedBy;
}
