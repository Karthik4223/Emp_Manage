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

import com.example.employee.enums.Status;
import com.example.employee.model.Rights;
import com.example.employee.service.RightsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rights")
public class RightsController {
	
	@Autowired
	private RightsService rightsService;
	
	@PreAuthorize("hasAuthority('RIGHT_RIGHTS_CREATE')")
	@PostMapping("/addRight")
	public ResponseEntity<String> addRight(@RequestBody Rights rights){
		boolean res = false;
		try {
			res = rightsService.addRights(rights);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		if(res) {
			return ResponseEntity.ok("Right added successfully.");
		}else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add Right");
		}
	}
	
	@PreAuthorize("hasAuthority('RIGHT_RIGHTS_VIEW')")
	@GetMapping("/getAllRights")
	public List<Rights> getAllRights(){
		return rightsService.getAllRights();
	}

	
	@PreAuthorize("hasAuthority('RIGHT_RIGHTS_UPDATE')")
	@PutMapping("/updateRight")
	public ResponseEntity<String> updateRight(@RequestBody Rights right) {
		boolean res = false;
		try {
			res = rightsService.updateRights(right);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update Right");
		}
		
		if(res) {
			return ResponseEntity.ok("Right updated successfully.");
		}else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update right");
		}
		
	}
	
	@PreAuthorize("hasAuthority('RIGHT_RIGHTS_CHANGE_STATUS')")
	@PutMapping("/updateRightStatus/{rightCode}")
	public ResponseEntity<String> updateRightStatus(@PathVariable String rightCode,@RequestParam Status newStatus,@RequestParam String updatedBy) {
		boolean res = false;
		try {
			res = rightsService.updateRightsStatus(rightCode,newStatus,updatedBy);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		if(res) {
			return ResponseEntity.ok("RightStatus updated successfully.");
		}else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update Rightstatus");
		}
	}
	
	@PreAuthorize("hasAuthority('RIGHT_RIGHTS_DELETE')")
	@DeleteMapping("/deleteRight/{rightCode}")
	public ResponseEntity<String>  deleteEmployee(@PathVariable String rightCode){
		boolean res = false;
		try {
			res = rightsService.deleteRights(rightCode);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		if(res) {
			return ResponseEntity.ok("Right deleted successfully.");
		}else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete Right");
		}
		
	}
}
