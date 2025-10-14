package com.example.employee.service;

import java.util.List;

import com.example.employee.customException.EmployeeException;
import com.example.employee.model.EmployeeRights;

public interface EmployeeRightsService {
	boolean addEmployeeRights(EmployeeRights employeeRights, String createdBy) throws EmployeeException;

	List<EmployeeRights> getAllEmployeeRights();
	
	 EmployeeRights getEmployeeRightsByEmpCode(String emp_code) throws EmployeeException;

}
