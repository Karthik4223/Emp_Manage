package com.example.employee.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.jms.MapMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.EmployeeRequestStatus;
import com.example.employee.helpers.GetLoggedInEmployee;
import com.example.employee.helpers.MessageCauseForException;
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
			log.info("{} - {}",GetLoggedInEmployee.getLoggedInEmployeeCode(),employeeRequest.getCreatedBy());
			if(!GetLoggedInEmployee.getLoggedInEmployeeCode().equalsIgnoreCase(employeeRequest.getCreatedBy())) {
				throw new EmployeeException("The loggedIn user miss-match");
			}
			return employeeRequestRepo.addEmployeeRequest(employeeRequest);
		}catch (DataIntegrityViolationException e) {
			log.error(e.getMessage(),e);
	        String column = MessageCauseForException.getMessageCause(e);

	        throw new EmployeeException("Duplicate entry found in column: " + column, e);
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
			employeeRequest.setCreatedBy(GetLoggedInEmployee.getLoggedInEmployeeCode());
			
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
			
			log.info("{} - {}",GetLoggedInEmployee.getLoggedInEmployeeCode(),updatedBy);
			if(!GetLoggedInEmployee.getLoggedInEmployeeCode().equalsIgnoreCase(updatedBy)) {
				throw new EmployeeException("The loggedIn user miss-match");
			}
	    	
			
			
			log.info(GetLoggedInEmployee.getLoggedInEmployeeCode());
			
			EmployeeRequest employeeRequest = employeeRequestRepo.getEmployeeRequestById(emp_RequestId);
			
			if(employeeRequest.getEmpRequestStatus() == EmployeeRequestStatus.TRANSIT || employeeRequest.getEmpRequestStatus() == EmployeeRequestStatus.APPROVED) {
				throw new EmployeeException("The Employee request is already been approved by some other user");
			}
			
			if(newStatus == EmployeeRequestStatus.REJECTED) {
				return 	employeeRequestRepo.updateEmployeeRequestStatus(emp_RequestId, EmployeeRequestStatus.REJECTED,updatedBy);
			}
			
//			if(newStatus==EmployeeRequestStatus.APPROVED) {
				jmsTemplate.send(QUEUE_STRING, session -> {
					MapMessage message = session.createMapMessage();
					message.setInt("emp_RequestId",emp_RequestId);
					message.setString("newStatus", newStatus.name());
					message.setJMSType("ChangeEmployeeRequestStatus");
					return message;
					});
//			}
			
			return 	employeeRequestRepo.updateEmployeeRequestStatus(emp_RequestId, EmployeeRequestStatus.TRANSIT,updatedBy);
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
	                	  	    
			return employeeRequestRepo.deleteEmployeeRequest(emp_RequestId);
			
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to delete employee");
	    }
	}

	@Override
	public boolean addEmployeeRequests(List<EmployeeRequest> employees) throws EmployeeException {
		try {
			for(EmployeeRequest employeeRequest:employees) {
				Validate.validateEmployeeRequest(employeeRequest);
				employeeRequest.setEmpRequestStatus(EmployeeRequestStatus.CREATED);
				employeeRequest.setEmpCreatedDateTime(LocalDateTime.now());
			}
			return employeeRequestRepo.addEmployeeRequests(employees);
			
		}catch (DataIntegrityViolationException e) {
			log.error(e.getMessage(),e);
	        String column = MessageCauseForException.getMessageCause(e);

	        throw new EmployeeException("Duplicate entry found in column: " + column, e);
		}catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to add Employee Request");
	    }
			
	}

}
