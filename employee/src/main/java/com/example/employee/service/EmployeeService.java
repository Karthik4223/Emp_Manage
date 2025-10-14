package com.example.employee.service;

import java.util.List;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.Status;
import com.example.employee.model.Employee;
import com.example.employee.model.SearchCriteria;

public interface EmployeeService {
	boolean addEmployee(Employee employee) throws EmployeeException;
	
	List<Employee> getAllEmployees() throws EmployeeException;

	boolean updateEmployee(Employee employee) throws EmployeeException;

	boolean updateEmployeeStatus(String empCode, Status newStatus, String updatedBy) throws EmployeeException;

	List<Employee> searchEmployees(SearchCriteria criteria) throws EmployeeException;
	
	Employee getEmployeeById(String emp_code) throws EmployeeException;

}
