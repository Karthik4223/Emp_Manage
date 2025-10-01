package com.example.employee.repository;

import java.util.List;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.EmployeeRequestStatus;
import com.example.employee.model.EmployeeRequest;

public interface EmployeeRequestRepo {
	
	boolean addEmployeeRequest(EmployeeRequest employeeRequest);

	List<EmployeeRequest> getAllEmployeeRequests();

	boolean updateEmployeeRequest(EmployeeRequest employeeRequest) throws EmployeeException;

	boolean updateEmployeeRequestStatus(Integer emp_RequestId, EmployeeRequestStatus newStatus, String updatedBy) throws EmployeeException;

	boolean deleteEmployeeRequest(Integer emp_RequestId) throws EmployeeException;

	EmployeeRequest getEmployeeRequestById(Integer empRequestId);

	boolean addEmployeeRequests(List<EmployeeRequest> employees);

	
}
