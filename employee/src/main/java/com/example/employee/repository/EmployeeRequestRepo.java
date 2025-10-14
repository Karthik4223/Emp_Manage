package com.example.employee.repository;

import java.util.List;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.EmployeeRequestStatus;
import com.example.employee.model.EmployeeRequest;

public interface EmployeeRequestRepo {
	
	boolean addEmployeeRequest(EmployeeRequest employeeRequest);

	List<EmployeeRequest> getAllEmployeeRequests();

	boolean updateEmployeeRequestStatus(Integer emp_RequestId, EmployeeRequestStatus newStatus, String updatedBy,String empCode) throws EmployeeException;

	EmployeeRequest getEmployeeRequestById(Integer empRequestId);

	boolean addEmployeeRequests(List<EmployeeRequest> employees);

	List<EmployeeRequest> getEmployeeRequestByPhonenumberAndEmail(String phoneNumber,String email);

	
}
