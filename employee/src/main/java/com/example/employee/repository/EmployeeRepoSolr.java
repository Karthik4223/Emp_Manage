package com.example.employee.repository;

import java.util.List;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.Status;
import com.example.employee.model.Employee;
import com.example.employee.model.SearchCriteria;

public interface EmployeeRepoSolr {
	
	boolean addEmployeeToSolr(Employee employee) throws EmployeeException;

	boolean updateEmployeeInSolr(Employee employee) throws EmployeeException;

	boolean deleteEmployeeFromSolr(String empCode) throws EmployeeException;

	boolean updateEmployeeStatusInSolr(String empCode, Status newStatus,String updatedBy) throws EmployeeException;

	List<Employee> getAllEmployees() throws EmployeeException;

	Employee getEmployeeById(String empCode) throws EmployeeException;

	List<Employee> findByCriteria(SearchCriteria criteria) throws EmployeeException;

}
