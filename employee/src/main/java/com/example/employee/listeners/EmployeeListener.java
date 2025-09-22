package com.example.employee.listeners;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.example.employee.customException.EmployeeException;
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
	
	 Logger logger = LoggerFactory.getLogger(EmployeeListener.class);

	
	
	@JmsListener(destination = "employee-queue",selector = "JMSType ='MAP'")
	public void receiveMessage(MapMessage message) throws EmployeeException {
		String emp_code;
		try {
			emp_code = message.getString("emp_code");
			employeeRepoSolr.addEmployeeToSolr(employeeRepo.getAllEmployeeById(emp_code));
			logger.trace("Received: {}", emp_code);
			logger.trace("Data By EMPID: {}", employeeRepo.getAllEmployeeById(emp_code));
		} catch (JMSException e) {
			throw new EmployeeException("Falied to get employee code from queue");
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
		}
	}
	
	
	@JmsListener(destination = "employee-queue",selector = "JMSType ='Approved'")
	public void receiveMessagefromEmployeeRequest(MapMessage message) throws EmployeeException {
		Integer emp_RequestId;
		try {
			emp_RequestId = message.getInt("emp_RequestId");
		} catch (JMSException e) {
			throw new EmployeeException("Falied to get employee RequestId from queue");
		}
		
		logger.trace("Received: {}", emp_RequestId);
		logger.trace("Data By EMPID: {}", employeeRequestRepo.getEmployeeRequestById(emp_RequestId));

		EmployeeRequest employeeRequest =employeeRequestRepo.getEmployeeRequestById(emp_RequestId);
		Employee employee = EmployeeMapperFromEmployeeRequest.employeeMapperFromEmployeeRequest(employeeRequest);
		
	try {
			employeeService.addEmployee(employee);
		} catch (EmployeeException e) {
			e.printStackTrace();
		}
	}
	
	

}
