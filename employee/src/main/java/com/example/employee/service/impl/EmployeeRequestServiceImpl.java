package com.example.employee.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.jms.MapMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.EmployeeRequestStatus;
import com.example.employee.model.EmployeeRequest;
import com.example.employee.repository.EmployeeRequestRepo;
import com.example.employee.service.EmployeeRequestService;
import com.example.employee.validations.Validate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeRequestServiceImpl implements EmployeeRequestService{
	
	@Autowired
	private EmployeeRequestRepo employeeRequestRepo;
		
	@Autowired
	private JmsTemplate jmsTemplate;
	
	private static final String QUEUE_STRING = "employee-queue";
	


	@Override
	public boolean addEmployeeRequest(EmployeeRequest employeeRequest) throws EmployeeException {		
		try {
			Validate.validateEmployeeRequest(employeeRequest);
			employeeRequest.setEmpRequestStatus(EmployeeRequestStatus.CREATED);
			employeeRequest.setEmpCreatedDateTime(LocalDateTime.now());
			employeeRequest.setCreatedBy("ADMIN");
			return employeeRequestRepo.addEmployeeRequest(employeeRequest);
		}catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to add Employee Request");
	    }
		
	}

	@Override
	public List<EmployeeRequest> getAllEmployeeRequests() {
		return employeeRequestRepo.getAllEmployeeRequests();
	}

	@Override
	@Transactional(rollbackFor = EmployeeException.class)
	public boolean updateEmployeeRequest(EmployeeRequest employeeRequest) throws EmployeeException {
		try {
			Validate.validateEmployeeRequest(employeeRequest);
			
			EmployeeRequest existingEmployeeRequest = employeeRequestRepo.getEmployeeRequestById(employeeRequest.getEmpRequestId());
			
			if(existingEmployeeRequest==null) {
				throw new EmployeeException("EmployeeRequest not Found");
			}
			
			boolean logres= employeeRequestRepo.logEmployeeRequest(existingEmployeeRequest);
			if(!logres) {
				throw new EmployeeException("Failed to log EmployeeRequest before update");
			}
			
			employeeRequest.setEmpUpdatedDateTime(LocalDateTime.now());
			employeeRequest.setUpdatedBy("ADMIN");
			return employeeRequestRepo.updateEmployeeRequest(employeeRequest);
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to update employee");
	    }
	}

	@Override
	@Transactional(rollbackFor = EmployeeException.class)
	public boolean updateEmployeeRequestStatus(Integer emp_RequestId, EmployeeRequestStatus newStatus, String updatedBy) throws EmployeeException {
		try {
			
			if (emp_RequestId == null) {
	            throw new EmployeeException("EmployeeRequest id is mandatory");
	        }  
			
			if (emp_RequestId <0) {
	            throw new EmployeeException("EmployeeRequest id cannot be negative");
	        }  
	    	
//	    	if(!EmployeeRequestStatus.isValidStatus(newStatus.name())) {
//	            throw new EmployeeException("Invalid EmployeeRequest status");
//	    	}
			
			EmployeeRequest existingEmployeeRequest = employeeRequestRepo.getEmployeeRequestById(emp_RequestId);
			
			if(existingEmployeeRequest==null) {
				throw new EmployeeException("EmployeeRequest not Found");
			}
			
			boolean logres= employeeRequestRepo.logEmployeeRequest(existingEmployeeRequest);
			if(!logres) {
				throw new EmployeeException("Failed to log EmployeeRequest before update");
			}
			
			if(newStatus==EmployeeRequestStatus.APPROVED) {
				jmsTemplate.send(QUEUE_STRING, session -> {
					MapMessage message = session.createMapMessage();
					message.setInt("emp_RequestId",emp_RequestId);
					message.setJMSType("Approved");
					return message;
					});
			}
			
			return employeeRequestRepo.updateEmployeeRequestStatus(emp_RequestId, newStatus, updatedBy);
			
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to update employee");
	    }
	}

	@Override
	@Transactional(rollbackFor = EmployeeException.class)
	public boolean deleteEmployeeRequest(Integer emp_RequestId) throws EmployeeException {
		try {
			if (emp_RequestId == null) {
	            throw new EmployeeException("Employee code is mandatory");
	        }
			
			if (emp_RequestId <0) {
	            throw new EmployeeException("Employee id cannot be negative");
	        }  
	                	  
			EmployeeRequest existingEmployeeRequest = employeeRequestRepo.getEmployeeRequestById(emp_RequestId);
			
			if(existingEmployeeRequest==null) {
				throw new EmployeeException("EmployeeRequest not Found");
			}
			
			boolean logres= employeeRequestRepo.logEmployeeRequest(existingEmployeeRequest);
			if(!logres) {
				throw new EmployeeException("Failed to log EmployeeRequest before update");
			}
	    
			return employeeRequestRepo.deleteEmployeeRequest(emp_RequestId);
			
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to delete employee");
	    }
	}

}
