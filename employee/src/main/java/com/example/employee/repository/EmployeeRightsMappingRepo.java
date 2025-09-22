package com.example.employee.repository;

import java.util.List;

import com.example.employee.model.EmployeeRights;

public interface EmployeeRightsMappingRepo {

	boolean addEmployeeRights(EmployeeRights employeeRights);

	List<EmployeeRights> getAllEmployeeRights();

	EmployeeRights getEmployeeRightsByEmpCode(String emp_code);

	boolean deleteEmployeeRights(String emp_code, List<String> rightCodes);

	boolean logEmployeeRights(EmployeeRights employeeRights);
}
