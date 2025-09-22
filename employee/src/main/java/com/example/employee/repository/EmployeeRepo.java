package com.example.employee.repository;

import java.util.List;

import com.example.employee.enums.Status;
import com.example.employee.model.Employee;


public interface EmployeeRepo {
	
	boolean addEmployee(Employee employee);

	List<Employee> getAllEmployees();

	boolean updateEmployee(Employee employee);
	
	boolean updateEmployeeStatus(String empCode, Status newStatus, String updatedBy);

	boolean deleteEmployee(String empCode);

	Employee getAllEmployeeById(String emp_code);

	boolean logEmployee(Employee employee);

	Integer getEmpId();


}
