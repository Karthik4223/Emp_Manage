package com.example.employee.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.Status;
import com.example.employee.model.Employee;
import com.example.employee.model.SearchCriteria;
import com.example.employee.repository.EmployeeRepo;
import com.example.employee.repository.EmployeeRepoSolr;
import com.example.employee.service.EmployeeService;
import com.example.employee.validations.Validate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	@Autowired
	private EmployeeRepoSolr employeeRepoSolr;
		
	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional(rollbackFor = EmployeeException.class)
	public boolean addEmployee(Employee employee) throws EmployeeException {
		try {
			employee.setName(employee.getName().trim());
			employee.setEmail(employee.getEmail().trim());
			employee.setPhoneNumber(employee.getPhoneNumber().trim());
			employee.setEmpCode(genEmpCode());
			employee.setEmpPassword(passwordEncoder.encode(getPassword(employee.getName(),employee.getPhoneNumber())));
			employee.setEmployeeStatus(Status.ACTIVE);
			employee.setEmpCreatedDateTime(LocalDateTime.now());
			
			Validate.validateEmployee(employee);
			Validate.validateEmpCode(employee.getEmpCode());
			Validate.validateEmpPassword(employee.getEmpPassword());
			
			boolean res= employeeRepo.addEmployee(employee);
			
			
			if(res) {
				
				return employeeRepoSolr.addEmployeeToSolr(employeeRepo.getEmployeeById(employee.getEmpCode()));
			}
			 
			return res;
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to add Employee");
	    }
	}


	@Override
	public List<Employee> getAllEmployees() throws EmployeeException {
		return employeeRepoSolr.getAllEmployees();
	}


	@Override
	@Transactional(rollbackFor = EmployeeException.class)
	public boolean updateEmployee(Employee employee) throws EmployeeException {
		try {
			Validate.validateEmployee(employee);
			
			employee.setEmpUpdatedDateTime(LocalDateTime.now());
			boolean res= employeeRepo.updateEmployee(employee);
			if(res) {
				return employeeRepoSolr.updateEmployeeInSolr(employeeRepo.getEmployeeById(employee.getEmpCode()));
			}
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to update employee");
	    }
		return false;
	}


	@Override
	@Transactional(rollbackFor = EmployeeException.class)
	public boolean updateEmployeeStatus(String empCode, Status newStatus, String updatedBy) throws EmployeeException {
		try {
			
			Validate.validateEmpCode(empCode);
 
	    	if(!Status.isValidStatus(newStatus.name())) {
	            throw new EmployeeException("Invalid Employee status");
	    	}
	    	
	    	Employee employee = employeeRepo.getEmployeeById(empCode);
	    	
	    	if(employee.getEmployeeStatus().equals(newStatus)) {
	    		throw new EmployeeException("The employee is already been "+newStatus.name().toLowerCase()+"ed by "+employee.getUpdatedBy());
	    	}
	    	
			boolean res= employeeRepo.updateEmployeeStatus(empCode, newStatus, updatedBy);
			if(res) {
				return employeeRepoSolr.updateEmployeeInSolr(employeeRepo.getEmployeeById(empCode));
			}
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to update employee");
	    }
		return false;
	}
	
	
	private String genEmpCode() {
		String prefix = "EMP";
		
		Integer eId = employeeRepo.getEmpId();
		
		if (eId == null) {
	        eId = 1;
	    }

	    String paddedId = String.format("%04d", eId);

	    return prefix + paddedId;
	}
	
	private String getPassword(String name, String phoneNumber) {
	    String namePart = name.substring(0, 4);
	    String phonePart = phoneNumber.substring(6);
	    return namePart + phonePart;
	}


	@Override
	public List<Employee> searchEmployees(SearchCriteria criteria) throws EmployeeException {
		Validate.validateCriteria(criteria);
		try {
			return employeeRepoSolr.findByCriteria(criteria);			
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Search Failed");
	    }
    }


	@Override
	public Employee getEmployeeById(String emp_code) throws EmployeeException {
		try {
			Validate.validateEmpCode(emp_code);
			return employeeRepoSolr.getEmployeeById(emp_code);			
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
			throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("No Employee Found with given employee code");
	    }
	}

}
