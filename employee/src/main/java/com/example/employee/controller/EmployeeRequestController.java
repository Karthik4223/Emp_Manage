package com.example.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.employee.enums.EmployeeRequestStatus;
import com.example.employee.helpers.GetLoggedInEmployee;
import com.example.employee.model.EmployeeRequest;
import com.example.employee.service.EmployeeRequestService;
import com.example.employee.service.impl.EmployeeExcelService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employeeRequest")
public class EmployeeRequestController {
	
	@Autowired
	private EmployeeRequestService employeeRequestService;
	
	@Autowired
    private EmployeeExcelService employeeExcelService;
	
	@PreAuthorize("hasAuthority('RIGHT_EMPLOYEE_REQUEST_CREATE')")
	@PostMapping("/addEmployeeRequest")
	public ResponseEntity<String> addEmployeeRequest(@RequestBody EmployeeRequest employeeRequest){
		boolean res;
		try {
			employeeRequest.setCreatedBy(GetLoggedInEmployee.getLoggedInEmployeeCode());
			res = employeeRequestService.addEmployeeRequest(employeeRequest);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		if(res) {
			return ResponseEntity.ok("EmployeeRequest added successfully.");
		}else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add EmployeeRequest");
		}
	}
	
	@PreAuthorize("hasAuthority('RIGHT_EMPLOYEE_REQUEST_VIEW')")
	@GetMapping("/getAllEmployeeRequests")
	public ResponseEntity<List<EmployeeRequest>> getAllEmployeeRequests(){
		return ResponseEntity.ok(employeeRequestService.getAllEmployeeRequests());
		
	}
	

	@PreAuthorize("hasAuthority('RIGHT_EMPLOYEE_REQUEST_APPROVE_REJECT')")
	@PutMapping("/updateEmployeeRequestStatus/{ReqId}")
	public ResponseEntity<String> updateEmployeeRequestStatus(@PathVariable Integer ReqId,@RequestParam EmployeeRequestStatus newStatus) {
				
		boolean res = false;
		try {
			res = employeeRequestService.updateEmployeeRequestStatus(ReqId, newStatus, GetLoggedInEmployee.getLoggedInEmployeeCode());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		if(res) {
			return ResponseEntity.ok("EmployeeRequestStatus updated successfully.");
		}else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update EmployeeRequestStatus");
		}
	}
	
	
	@PreAuthorize("hasAuthority('RIGHT_EMPLOYEE_CREATE_EXCEL')")
    @PostMapping("/upload-excel")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
    	 boolean res = false;
    	try {
            List<EmployeeRequest> employees = employeeExcelService.parseExcel(file,GetLoggedInEmployee.getLoggedInEmployeeCode());
            
           res=employeeRequestService.addEmployeeRequests(employees);
        } catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

	    if(res) {
	    	return ResponseEntity.ok("Employee Requests Created");        	   
	    }else {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	    			.body("Failed to process excel file");           
	    }
	  
    }


}
