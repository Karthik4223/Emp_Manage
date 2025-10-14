package com.example.employee.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.helpers.GetLoggedInEmployee;
import com.example.employee.model.EmployeeRights;
import com.example.employee.service.EmployeeRightsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employeeRights")
public class EmployeeRightsController {
	
	@Autowired
	private EmployeeRightsService employeeRightsService;
	
	@PreAuthorize("hasAuthority('RIGHT_EMPLOYEE_RIGHTS_MAPPING')")
	@PostMapping("/addEmployeeRights")
	public ResponseEntity<String> addEmployeeRights(@RequestBody EmployeeRights employeeRights){
		boolean res;
		try {
			res = employeeRightsService.addEmployeeRights(employeeRights,GetLoggedInEmployee.getLoggedInEmployeeCode());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		if(res) {
			return ResponseEntity.ok("EmployeeRights added successfully.");
		}else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add EmployeeRights");
		}
	}
	
	@PreAuthorize("hasAuthority('RIGHT_EMPLOYEE_RIGHTS_MAPPING')")
	@GetMapping("/getAllEmployeeRights")
	public ResponseEntity<List<EmployeeRights>> getAllEmployeeRights(){
		return ResponseEntity.ok(employeeRightsService.getAllEmployeeRights());
	}
	
	
	@PreAuthorize("hasAuthority('RIGHT_EMPLOYEE_RIGHTS_MAPPING')")
	@GetMapping("/getEmployeeRights/{emp_code}")
	public ResponseEntity<?> getEmployeeRightsByEmpCode(@PathVariable String emp_code) {
		
		try {
			EmployeeRights employeeRights =employeeRightsService.getEmployeeRightsByEmpCode(emp_code);
			if(employeeRights!=null) {
				return ResponseEntity.ok(employeeRights);				
			}else {
				return ResponseEntity.ok(Collections.EMPTY_LIST);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to get Assigned roles");
		}
	}
	
	

}
