package com.example.employee.service;

import java.util.List;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.EmployeeRequestStatus;
import com.example.employee.model.EmployeeRequest;

public interface EmployeeRequestService {

	boolean addEmployeeRequest(EmployeeRequest employeeRequest) throws EmployeeException;

	List<EmployeeRequest> getAllEmployeeRequests();


	boolean updateEmployeeRequestStatus(Integer emp_RequestId, EmployeeRequestStatus newStatus, String updatedBy) throws EmployeeException;


	boolean addEmployeeRequests(List<EmployeeRequest> employees) throws EmployeeException;

}
