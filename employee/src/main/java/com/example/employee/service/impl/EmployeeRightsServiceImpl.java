package com.example.employee.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employee.customException.EmployeeException;
import com.example.employee.model.EmployeeRights;
import com.example.employee.repository.EmployeeRightsMappingRepo;
import com.example.employee.service.EmployeeRightsService;
import com.example.employee.validations.Validate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeRightsServiceImpl implements EmployeeRightsService {
	
	@Autowired
	private EmployeeRightsMappingRepo employeeRightsMappingRepo;

	@Override
	public boolean addEmployeeRights(EmployeeRights employeeRights) throws EmployeeException {
	    try {
	        Validate.validateEmployeeRights(employeeRights);

	        List<String> rightsToAssign = employeeRights.getRightCode();
	        
	        EmployeeRights employeeRightsExisting = employeeRightsMappingRepo.getEmployeeRightsByEmpCode(employeeRights.getEmpCode());

	        List<String> rightsAlreadyAssigned = employeeRightsExisting != null && employeeRightsExisting.getRightCode() != null
	                							? employeeRightsExisting.getRightCode()
	                							: Collections.emptyList();
	        
	        if(rightsToAssign.isEmpty() && rightsAlreadyAssigned.isEmpty()) {
	        	throw new EmployeeException("Please select rights to assign");
	        }

	        List<String> finalRightsToAssign = new ArrayList<>();
	        List<String> rightsToRemove = new ArrayList<>();

	        Set<String> assignSet = new HashSet<>(rightsToAssign);
	        Set<String> existingSet = new HashSet<>(rightsAlreadyAssigned);

	        for (String right : assignSet) {
	            if (!existingSet.contains(right)) {
	                finalRightsToAssign.add(right);
	            }
	        }

	        for (String right : existingSet) {
	            if (!assignSet.contains(right)) {
	                rightsToRemove.add(right);
	            }
	        }

	        if (finalRightsToAssign.isEmpty() && rightsToRemove.isEmpty()) {
	            throw new EmployeeException("Selected Rights are already assigned");
	        }

	        if (!rightsToRemove.isEmpty()) {
	            employeeRightsMappingRepo.deleteEmployeeRights(employeeRights.getEmpCode(), rightsToRemove);
	        }

	        if (!finalRightsToAssign.isEmpty()) {
	            employeeRights.setRightCode(finalRightsToAssign);
	            employeeRights.setEmpRightCreatedDateTime(LocalDateTime.now());
	            employeeRights.setCreatedBy("ADMIN");
	            return employeeRightsMappingRepo.addEmployeeRights(employeeRights);
	        }

	        return true;

	    } catch (EmployeeException e) {
	        log.error(e.getMessage(), e);
	        throw e;
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        throw new EmployeeException("Failed to add EmployeeRight");
	    }
	}

	@Override
	public List<EmployeeRights> getAllEmployeeRights() {
		return employeeRightsMappingRepo.getAllEmployeeRights();

	}

	@Override
	@Transactional(rollbackFor = EmployeeException.class)
	public boolean deleteEmployeeRights(String emp_code,  List<String> right_code) throws EmployeeException {
		try {
			if (emp_code == null || emp_code.isEmpty()) {
	            throw new EmployeeException("Employee code is mandatory");
	        }
			
			if (right_code == null || right_code.isEmpty()) {
	            throw new EmployeeException("Right code is mandatory");
	        }
	                	  
			EmployeeRights existingEmployeeRights = employeeRightsMappingRepo.getEmployeeRightsByEmpCode(emp_code);
			boolean logres= employeeRightsMappingRepo.logEmployeeRights(existingEmployeeRights);
			if(!logres) {
				throw new EmployeeException("Failed to log EmployeeRequest before update");
			}
	    
			return employeeRightsMappingRepo.deleteEmployeeRights(emp_code,right_code);
			
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to delete employee right");
	    }
	}

	@Override
	public EmployeeRights getEmployeeRightsByEmpCode(String emp_code) throws EmployeeException {
		Validate.validateEmpCode(emp_code);
		
		return employeeRightsMappingRepo.getEmployeeRightsByEmpCode(emp_code);
		
	}


}
