package com.example.employee.repository;

import java.util.List;

import com.example.employee.enums.EmployeeRequestStatus;
import com.example.employee.model.EmployeeRequest;

public interface EmployeeRequestRepo {
	
	boolean addEmployeeRequest(EmployeeRequest employeeRequest);

	List<EmployeeRequest> getAllEmployeeRequests();

	boolean updateEmployeeRequest(EmployeeRequest employeeRequest);

	boolean updateEmployeeRequestStatus(Integer emp_RequestId, EmployeeRequestStatus newStatus, String updatedBy);

	boolean deleteEmployeeRequest(Integer emp_RequestId);

	boolean logEmployeeRequest(EmployeeRequest employeeRequest);

	EmployeeRequest getEmployeeRequestById(Integer empRequestId);

	
}
