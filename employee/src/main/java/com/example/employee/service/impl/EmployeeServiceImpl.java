package com.example.employee.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jms.core.JmsTemplate;
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
	private JmsTemplate jmsTemplate;
	
	private static final String QUEUE_STRING = "employee-queue";
	
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
			
//		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
//		 @Override
//		 public void afterCommit() {
//			if(res) {
//				jmsTemplate.send(QUEUE_STRING, session -> {
//					MapMessage message = session.createMapMessage();
//					message.setString("emp_code", employee.getEmpCode());
//					message.setJMSType("MAP");
//					return message;
//					});
//				}
//	        }
//	    });
			
			if(res) {
				return employeeRepoSolr.addEmployeeToSolr(employeeRepo.getAllEmployeeById(employee.getEmpCode()));
			}
			 
			return res;
		}catch (DataIntegrityViolationException e) {
			log.error(e.getMessage(),e);
	        Throwable rootCause = e.getRootCause();
	        String message = rootCause != null ? rootCause.getMessage() : e.getMessage();
	        String column = "unknown column";
	        if (message != null) {
	            int idx = message.indexOf("for key");
	            if (idx != -1) {
	                column = message.substring(idx + 8).replaceAll("['`]", "").trim();
	            }
	        }
	        throw new EmployeeException("Duplicate entry found in column: " + column, e);
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
			
			Employee existingEmployee = employeeRepo.getAllEmployeeById(employee.getEmpCode());
			
			if(existingEmployee==null) {
				throw new EmployeeException("Employee not Found");
			}
			
			boolean logres= employeeRepo.logEmployee(existingEmployee);
			if(!logres) {
				throw new EmployeeException("Failed to log employee before update");
			}
			employee.setEmpUpdatedDateTime(LocalDateTime.now());
			boolean res= employeeRepo.updateEmployee(employee);
			if(res) {
				return employeeRepoSolr.updateEmployeeInSolr(employeeRepo.getAllEmployeeById(employee.getEmpCode()));
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
			
	    	Employee existingEmployee = employeeRepo.getAllEmployeeById(empCode);
	    	
	    	if(existingEmployee==null) {
	    		throw new EmployeeException("Employee not Found");
	    	}
	    	
	    	boolean logres= employeeRepo.logEmployee(existingEmployee);
	    	if(!logres) {
	    		throw new EmployeeException("Failed to log employee before updateSatsu");
	    	}
	    	
			boolean res= employeeRepo.updateEmployeeStatus(empCode, newStatus, updatedBy);
			if(res) {
				return employeeRepoSolr.updateEmployeeInSolr(employeeRepo.getAllEmployeeById(empCode));
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
	public boolean deleteEmployee(String empCode) throws EmployeeException {
		try {
	    	
			Validate.validateEmpCode(empCode);
			
	    	Employee existingEmployee = employeeRepo.getAllEmployeeById(empCode);
	    	
	    	if(existingEmployee==null) {
	    		throw new EmployeeException("Employee not Found");
	    	}
	    	
	    	boolean logres= employeeRepo.logEmployee(existingEmployee);
	    	if(!logres) {
	    		throw new EmployeeException("Failed to log employee before delete");
	    	}
	    
			boolean res= employeeRepo.deleteEmployee(empCode);
			if(res) {
				return employeeRepoSolr.deleteEmployeeFromSolr(empCode);
			}
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to delete employee");
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
	public Employee getAllEmployeeById(String emp_code) throws EmployeeException {
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
