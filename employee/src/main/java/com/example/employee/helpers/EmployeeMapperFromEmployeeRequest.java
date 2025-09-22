package com.example.employee.helpers;

import com.example.employee.enums.Status;
import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeRequest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmployeeMapperFromEmployeeRequest {
	
	public Employee employeeMapperFromEmployeeRequest(EmployeeRequest employeeRequest) {
		Employee employee = new Employee();
		
		employee.setEmail(employeeRequest.getEmail());
		employee.setName(employeeRequest.getName());
		employee.setPhoneNumber(employeeRequest.getPhoneNumber());
		employee.setEmpDepartment(employeeRequest.getEmpDepartment());
		employee.setGender(employeeRequest.getGender());
		employee.setCountry(employeeRequest.getCountry());
		employee.setCity(employeeRequest.getCity());
		employee.setState(employeeRequest.getState());
		employee.setEmployeeStatus(Status.ACTIVE);
		
		return employee;
		
		
	}

}
