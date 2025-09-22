package com.example.employee.service;

import java.util.List;

import com.example.employee.customException.EmployeeException;
import com.example.employee.model.EmployeeRights;

public interface EmployeeRightsService {
	boolean addEmployeeRights(EmployeeRights employeeRights) throws EmployeeException;

	List<EmployeeRights> getAllEmployeeRights();
	
	 EmployeeRights getEmployeeRightsByEmpCode(String emp_code) throws EmployeeException;


	boolean deleteEmployeeRights(String emp_code,  List<String> right_code) throws EmployeeException;

}
