package com.example.employee.repository;

import java.util.List;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.Status;
import com.example.employee.model.Rights;

public interface RightsRepo {
	
	boolean addRights(Rights rights);

	List<Rights> getAllRights();

	boolean updateRights(Rights rights) throws EmployeeException;

	boolean updateRightsStatus(String rightCode, Status newStatus, String updatedBy) throws EmployeeException;

	boolean deleteRights(String rightCode) throws EmployeeException;

	Rights getRightById(String right_code);

	Integer getRightId();

	Rights getRightByName(String rightName);
	
}
