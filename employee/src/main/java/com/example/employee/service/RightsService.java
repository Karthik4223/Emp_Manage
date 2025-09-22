package com.example.employee.service;

import java.util.List;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.Status;
import com.example.employee.model.Rights;

public interface RightsService {
	
	boolean addRights(Rights rights) throws EmployeeException;
	
	List<Rights> getAllRights();
	
	boolean updateRights(Rights rights) throws EmployeeException;

	boolean updateRightsStatus(String rightCode, Status newStatus, String updatedBy) throws EmployeeException;

	boolean deleteRights(String rightCode) throws EmployeeException;


}
