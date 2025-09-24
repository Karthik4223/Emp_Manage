package com.example.employee.service.impl;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.employee.customException.EmployeeException;
import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeLogin;
import com.example.employee.model.EmployeeRights;
import com.example.employee.service.EmployeeRightsService;
import com.example.employee.service.EmployeeService;

@Service
public class CustomEmployeeDetailsService implements UserDetailsService {

    private final EmployeeService employeeService;
    private final EmployeeRightsService employeeRightsService;

    public CustomEmployeeDetailsService(EmployeeService employeeService, EmployeeRightsService employeeRightsService) {
        this.employeeService = employeeService;
        this.employeeRightsService = employeeRightsService;
    }

    @Override
    public UserDetails loadUserByUsername(String empCode){
        Employee employee = null;
		try {
			employee = employeeService.getAllEmployeeById(empCode);
		} catch (EmployeeException e) {
			e.printStackTrace();
		}
        if (employee == null)
        	throw new UsernameNotFoundException("Employee not found");

        EmployeeRights rights = null;
		try {
			rights = employeeRightsService.getEmployeeRightsByEmpCode(empCode);
		} catch (EmployeeException e) {
			e.printStackTrace();
		}

        List<String> rightCodes = (rights != null && rights.getRightCode() != null) 
                                  ? rights.getRightCode() 
                                  : Collections.emptyList();

        return new EmployeeLogin(empCode, employee.getEmpPassword(), rightCodes);
    }
}
