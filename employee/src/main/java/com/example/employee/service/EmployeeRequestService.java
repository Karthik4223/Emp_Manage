package com.example.employee.service;

import java.util.List;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.EmployeeRequestStatus;
import com.example.employee.model.EmployeeRequest;

public interface EmployeeRequestService {

	boolean addEmployeeRequest(EmployeeRequest employeeRequest) throws EmployeeException;

	List<EmployeeRequest> getAllEmployeeRequests();

	boolean updateEmployeeRequest(EmployeeRequest employeeRequest) throws EmployeeException;

	boolean updateEmployeeRequestStatus(Integer emp_RequestId, EmployeeRequestStatus newStatus, String updatedBy) throws EmployeeException;

	boolean deleteEmployeeRequest(Integer emp_RequestId) throws EmployeeException;

}
