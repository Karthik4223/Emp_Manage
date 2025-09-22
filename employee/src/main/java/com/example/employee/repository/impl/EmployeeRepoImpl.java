package com.example.employee.repository.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.employee.enums.Status;
import com.example.employee.helpers.EmployeeRowMapper;
import com.example.employee.helpers.SqlParamSoucreForEmployee;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepo;

@Repository
public class EmployeeRepoImpl implements EmployeeRepo{
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private SqlParamSoucreForEmployee sqlParamSoucreForEmployee = new SqlParamSoucreForEmployee();


	@Override
	public boolean addEmployee(Employee employee) {
		String sqlString = "Insert into Employees(emp_code,emp_password,email,department,name,phone_number,gender,country,state,city,emp_status,createdDateTime,updatedDateTime,createdBy) values "
				+ "(:emp_code,:emp_password,:email,:department,:name,:phone_number,:gender,:country,:state,:city,:emp_status,:createdDateTime,:updatedDateTime,:createdBy)";
		
        MapSqlParameterSource param = sqlParamSoucreForEmployee.getParam(employee);
        
        int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);
		
		return rowsEffected>0;
	}

	@Override
	public List<Employee> getAllEmployees() {
		String sqlString =" Select emp_code,emp_password,email,department,name,phone_number,gender,country,state,city,emp_status,createdDateTime,updatedDateTime,createdBy,updatedBy from Employees";
		
		return namedParameterJdbcTemplate.query(sqlString, new EmployeeRowMapper());
	}	
	
	
	@Override
	public Employee getAllEmployeeById(String emp_code) {
		String sqlString =" Select emp_code,emp_password,email,department,name,phone_number,gender,country,state,city,emp_status,createdDateTime,updatedDateTime,createdBy,updatedBy from Employees "
				+ "where emp_code=:emp_code";
		
		MapSqlParameterSource param = new MapSqlParameterSource();
	    
	    param.addValue("emp_code", emp_code); 
	    
	    List<Employee> employee = namedParameterJdbcTemplate.query(sqlString, param,new EmployeeRowMapper());
	    	    
	    return employee!=null && !employee.isEmpty() ? employee.get(0) : null;
		
	}	
	
	
	@Override
	public boolean updateEmployee(Employee employee) {
	    String sqlString = "UPDATE Employees SET "
	            + "emp_password = :emp_password, "
	            + "email = :email, "
	            + "department = :department, "
	            + "name = :name, "
	            + "phone_number = :phone_number, "
	            + "gender = :gender, "
	            + "country = :country, "
	            + "state = :state, "
	            + "city = :city, "
	            + "emp_status = :emp_status, "
	            + "updatedDateTime = :updatedDateTime, "
	            + "updatedBy = :updatedBy "
	            + "WHERE emp_code = :emp_code";

	    MapSqlParameterSource param = sqlParamSoucreForEmployee.getParam(employee);
	    		
	    int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);

	    return rowsEffected > 0;
	}
	
	@Override
	public boolean updateEmployeeStatus(String empCode, Status newStatus, String updatedBy) {
	    String sqlString = "UPDATE Employees SET "
	            + "emp_status = :emp_status, "
	            + "updatedDateTime = :updatedDateTime, "
	            + "updatedBy = :updatedBy "
	            + "WHERE emp_code = :emp_code"; 

	    MapSqlParameterSource param = new MapSqlParameterSource();
	    
	    param.addValue("emp_code", empCode); 
	    param.addValue("emp_status", newStatus.getCode()); 
	    param.addValue("updatedDateTime", LocalDateTime.now()); 
	    param.addValue("updatedBy", updatedBy); 

	    int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);

	    return rowsEffected > 0;
	}


	@Override
	public boolean deleteEmployee(String empCode) {
	    String sqlString = "DELETE FROM Employees WHERE emp_code = :emp_code";  

	    MapSqlParameterSource param = new MapSqlParameterSource();
	    param.addValue("emp_code", empCode); 

	    int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);

	    return rowsEffected > 0;
	}
	
	@Override
	public boolean logEmployee(Employee employee) {
		String sqlEmployee=" Select emp_id,emp_code,emp_password,email,department,name,phone_number,gender,country,state,city,emp_status,createdDateTime,updatedDateTime,createdBy,updatedBy,sysTime from Employees "
				+ "where emp_code=:emp_code";

		String sqlString = "Insert into Employees_log "+sqlEmployee;
		MapSqlParameterSource param = new MapSqlParameterSource();
	    param.addValue("emp_code", employee.getEmpCode());  
        
        int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);
		
		return rowsEffected>0;
	}

	@Override
	public Integer getEmpId() {
		String sqlString ="Select MAX(emp_id)+1 from Employees";
		
		Integer res = namedParameterJdbcTemplate.queryForObject(sqlString, new MapSqlParameterSource(), Integer.class);
		
		return res!=null ? res : null;
	}


}
