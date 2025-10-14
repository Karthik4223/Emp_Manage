package com.example.employee.listeners;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.EmployeeRequestStatus;
import com.example.employee.helpers.EmployeeMapperFromEmployeeRequest;
import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeRequest;
import com.example.employee.repository.EmployeeRepo;
import com.example.employee.repository.EmployeeRepoSolr;
import com.example.employee.repository.EmployeeRequestRepo;
import com.example.employee.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeListener {
	
	@Autowired
	private EmployeeRepoSolr employeeRepoSolr;
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	@Autowired
	private EmployeeRequestRepo employeeRequestRepo;
	
	@Autowired
	private EmployeeService employeeService;
	
	
	@JmsListener(destination = "employee-queue",selector = "JMSType ='ChangeEmployeeRequestStatus'")
	public void receiveMessagefromEmployeeRequest(MapMessage message) throws EmployeeException {
		
		Integer emp_RequestId;
		String updatedBy;
		try {
			emp_RequestId = message.getInt("emp_RequestId");
			updatedBy = message.getString("updatedBy");
			log.info("Listtned : {}",emp_RequestId);
		} catch (JMSException e) {
			log.error(e.getMessage(),e);
			throw new EmployeeException("Falied to get employee RequestId from queue");
		}
		
		
		EmployeeRequest employeeRequest =employeeRequestRepo.getEmployeeRequestById(emp_RequestId);
		
		log.info("employee req: {}",employeeRequest);
		
//		if(EmployeeRequestStatus.TRANSIT.equals(employeeRequest.getEmpRequestStatus())) {
				
			Employee employee = EmployeeMapperFromEmployeeRequest.employeeMapperFromEmployeeRequest(employeeRequest);
		try {
			log.info("employee : {}",employee);

			 boolean res =	employeeService.addEmployee(employee);
			 if(res) {
				 Employee employeeCreated = employeeRepo.getEmployeeByEmail(employee.getEmail());
				log.info("employeeCreadted : {}",employeeCreated);

				 employeeRequestRepo.updateEmployeeRequestStatus(emp_RequestId, EmployeeRequestStatus.APPROVED,updatedBy,employeeCreated.getEmpCode());
			 }
			} catch (EmployeeException e) {
				log.error(e.getMessage(),e);
				throw new EmployeeException("Failed to add Employee");	
			}
//		}
	
	}

}
