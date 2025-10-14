package com.example.employee.repository;

import java.util.List;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.Status;
import com.example.employee.model.Employee;


public interface EmployeeRepo {
	
	boolean addEmployee(Employee employee);

	List<Employee> getAllEmployees();

	boolean updateEmployee(Employee employee) throws EmployeeException;
	
	boolean updateEmployeeStatus(String empCode, Status newStatus, String updatedBy) throws EmployeeException;

	Employee getEmployeeById(String emp_code);

	Integer getEmpId();

	Employee getEmployeeByEmail(String email);

}
