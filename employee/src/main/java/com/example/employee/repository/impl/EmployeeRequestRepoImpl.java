package com.example.employee.repository.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.EmployeeRequestStatus;
import com.example.employee.helpers.EmployeeRequestRowMapper;
import com.example.employee.helpers.SqlParamSourceForEmployeeRequest;
import com.example.employee.model.EmployeeRequest;
import com.example.employee.repository.EmployeeRequestRepo;

@Repository
public class EmployeeRequestRepoImpl implements EmployeeRequestRepo{
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;	

	@Override
	public boolean addEmployeeRequest(EmployeeRequest employeeRequest) {
		String sqlString = "Insert into EmployeeRequests(email,department,name,phone_number,gender,country,state,city,emp_RequestStatus,createdDateTime,updatedDateTime,createdBy) values "
				+ "(:email,:department,:name,:phone_number,:gender,:country,:state,:city,:emp_RequestStatus,:createdDateTime,:updatedDateTime,:createdBy)";
		
        MapSqlParameterSource param = SqlParamSourceForEmployeeRequest.getParam(employeeRequest);
        
        int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);
		return rowsEffected>0;
	}

	@Override
	public List<EmployeeRequest> getAllEmployeeRequests() {
		String sqlString = "Select emp_RequestId,email,department,name,phone_number,gender,country,state,city,emp_RequestStatus,createdDateTime,updatedDateTime,createdBy,updatedBy from EmployeeRequests";
		
		return namedParameterJdbcTemplate.query(sqlString, new EmployeeRequestRowMapper());
	}
	
	@Override
	public EmployeeRequest getEmployeeRequestById(Integer emp_RequestId) {
		String sqlString = "Select emp_RequestId,email,department,name,phone_number,gender,country,state,city,emp_RequestStatus,createdDateTime,updatedDateTime,createdBy,updatedBy from EmployeeRequests "
				+ "where emp_RequestId=:emp_RequestId";
		
		MapSqlParameterSource param = new MapSqlParameterSource();
	    
	    param.addValue("emp_RequestId", emp_RequestId);  
		
		List<EmployeeRequest> employeeRequests = namedParameterJdbcTemplate.query(sqlString,param, new EmployeeRequestRowMapper());
	
		return employeeRequests!=null ? employeeRequests.get(0) : null;
	}
	
	@Override
	public boolean updateEmployeeRequest(EmployeeRequest employeeRequest) throws EmployeeException {
		
		EmployeeRequest existingEmployeeRequest = getEmployeeRequestById(employeeRequest.getEmpRequestId());
		
		boolean logres=	logEmployeeRequest(existingEmployeeRequest);

		if(!logres) {
			throw new EmployeeException("Failed to log employee request before update");
		}
		
		
	    String sqlString = "UPDATE EmployeeRequests SET "
	            + "email = :email, "
	            + "department = :department, "
	            + "name = :name, "
	            + "phone_number = :phone_number, "
	            + "gender = :gender, "
	            + "country = :country, "
	            + "state = :state, "
	            + "city = :city, "
	            + "emp_RequestStatus = :emp_RequestStatus, "
	            + "updatedDateTime = :updatedDateTime, "
	            + "updatedBy = :updatedBy "
	            + "WHERE emp_RequestId = :emp_RequestId";

        MapSqlParameterSource param = SqlParamSourceForEmployeeRequest.getParam(employeeRequest);
	  
	    int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);
	    return rowsEffected > 0;
	}

	
	@Override
	public boolean updateEmployeeRequestStatus(Integer emp_RequestId, EmployeeRequestStatus newStatus, String updatedBy) throws EmployeeException {
	    
		EmployeeRequest existingEmployeeRequest = getEmployeeRequestById(emp_RequestId);

		boolean logres=	logEmployeeRequest(existingEmployeeRequest);

		if(!logres) {
			throw new EmployeeException("Failed to log employee request before update status");
		}
		
		
		String sqlString = "UPDATE EmployeeRequests SET "
	            + "emp_RequestStatus = :emp_RequestStatus, "
	            + "updatedDateTime = :updatedDateTime, "
	            + "updatedBy = :updatedBy "
	            + "WHERE emp_RequestId = :emp_RequestId";

	    MapSqlParameterSource param = new MapSqlParameterSource();
	    
	    param.addValue("emp_RequestId", emp_RequestId);  
	    param.addValue("emp_RequestStatus", newStatus.getCode()); 
	    param.addValue("updatedDateTime", LocalDateTime.now()); 
	    param.addValue("updatedBy", updatedBy); 

	    int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);
	    return rowsEffected > 0;
	}
	
	@Override
	public boolean deleteEmployeeRequest(Integer emp_RequestId) throws EmployeeException {
	    
		EmployeeRequest existingEmployeeRequest = getEmployeeRequestById(emp_RequestId);

		boolean logres=	logEmployeeRequest(existingEmployeeRequest);

		if(!logres) {
			throw new EmployeeException("Failed to log employee request before delete");
		}		
		
		String sqlString = "DELETE FROM EmployeeRequests WHERE emp_RequestId = :emp_RequestId";  

	    MapSqlParameterSource param = new MapSqlParameterSource();
	    param.addValue("emp_RequestId", emp_RequestId); 

	    int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);
	    return rowsEffected > 0;
	}
	
	
	private boolean logEmployeeRequest(EmployeeRequest employeeRequest) {
		String sqlEmployeReq = " Select emp_RequestId,email,department,name,phone_number,gender,country,state,city,emp_RequestStatus,createdDateTime,updatedDateTime,createdBy,updatedBy,sysTime from EmployeeRequests "
				+ "where emp_RequestId=:emp_RequestId";
		
		String sqlString = "Insert into EmployeeRequests_log "+sqlEmployeReq;
	    MapSqlParameterSource param = new MapSqlParameterSource();
	    param.addValue("emp_RequestId", employeeRequest.getEmpRequestId());  
        
        int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);
		return rowsEffected>0;
		
		
	}

	@Override
	public boolean addEmployeeRequests(List<EmployeeRequest> employees) {
		String sqlString = "Insert into EmployeeRequests(email,department,name,phone_number,gender,country,state,city,emp_RequestStatus,createdDateTime,updatedDateTime,createdBy) values "
				+ "(:email,:department,:name,:phone_number,:gender,:country,:state,:city,:emp_RequestStatus,:createdDateTime,:updatedDateTime,:createdBy)";
		
	    List<SqlParameterSource> batchParams = new ArrayList<>();
		
		for(EmployeeRequest employeeRequest : employees) {
			
			MapSqlParameterSource param = SqlParamSourceForEmployeeRequest.getParam(employeeRequest);
			batchParams.add(param);
		}
        
		int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sqlString, batchParams.toArray(new SqlParameterSource[0]));
	    return Arrays.stream(updateCounts).anyMatch(count -> count > 0);
	}

	@Override
	public List<EmployeeRequest> getEmployeeRequestByPhonenumberAndEmail(String phoneNumber,String email) {
		String sqlString = "Select emp_RequestId,email,department,name,phone_number,gender,country,state,city,emp_RequestStatus,createdDateTime,updatedDateTime,createdBy,updatedBy from EmployeeRequests "
				+ "where email=:email or phone_number=:phone_number and emp_RequestStatus IN('C','A')";
		
		MapSqlParameterSource param = new MapSqlParameterSource();
	    
	    param.addValue("email", email);  

	    param.addValue("phone_number", phoneNumber);  
		
		List<EmployeeRequest> employeeRequests = namedParameterJdbcTemplate.query(sqlString,param, new EmployeeRequestRowMapper());
	
	    return employeeRequests;
	}
	



}
