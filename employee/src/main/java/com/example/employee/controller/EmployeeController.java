package com.example.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.Status;
import com.example.employee.model.Employee;
import com.example.employee.model.SearchCriteria;
import com.example.employee.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	
	@PreAuthorize("hasAuthority('RIGHT_EMPLOYEE_CREATE')")
	@PostMapping("/addEmployee")
	public ResponseEntity<String> addEmployee(@RequestBody Employee employee){
		boolean res = false;
		try {
			res = employeeService.addEmployee(employee);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		if(res) {
			return ResponseEntity.ok("Employee added successfully.");
		}else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add Employee");
		}
	}
	
	@PreAuthorize("hasAuthority('RIGHT_EMPLOYEE_VIEW')")
	@GetMapping("/getAllEmployees")
	public ResponseEntity<List<Employee>> getAllEmployees(){
		try {
			return ResponseEntity.ok(employeeService.getAllEmployees());
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@PreAuthorize("hasAuthority('RIGHT_EMPLOYEE_EDIT')")
	@PutMapping("/updateEmployee")
	public ResponseEntity<String> updateEmployee(@RequestBody Employee employee) {
		boolean res = false;
		try {
			res = employeeService.updateEmployee(employee);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		if(res) {
			return ResponseEntity.ok("Employee updated successfully.");
		}else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update Employee");
		}
		
	}

	@PreAuthorize("hasAuthority('RIGHT_EMPLOYEE_CHANGE_STATUS')")
	@PutMapping("/updateEmpStatus/{empCode}")
	public ResponseEntity<String> updateEmployeeStatus(@PathVariable String empCode,@RequestParam Status newStatus,@RequestParam String updatedBy) {
		boolean res = false;
		try {
			res = employeeService.updateEmployeeStatus(empCode,newStatus,updatedBy);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		if(res) {
			return ResponseEntity.ok("EmployeeStatus updated successfully.");
		}else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update EmployeeStatus");
		}
	}

	@PreAuthorize("hasAuthority('RIGHT_EMPLOYEE_DELETE')")
	@DeleteMapping("/deleteEmployee/{empCode}")
	public ResponseEntity<String>  deleteEmployee(@PathVariable String empCode){
		boolean res = false;
		try {
			res = employeeService.deleteEmployee(empCode);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		if(res) {
			return ResponseEntity.ok("Employee deleted successfully.");
		}else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete Employee");
		}
		
	}
	
	
	@PostMapping("/search")
    public ResponseEntity<?> searchEmployees(@RequestBody SearchCriteria criteria) {
        List<Employee> employees = null;
		try {
			employees = employeeService.searchEmployees(criteria);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
        return ResponseEntity.ok(employees);
    }

	

}
