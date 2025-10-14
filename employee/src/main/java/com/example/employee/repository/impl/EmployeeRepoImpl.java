package com.example.employee.repository.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.employee.customException.EmployeeException;
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
	public Employee getEmployeeById(String emp_code) {
		String sqlString =" Select emp_code,emp_password,email,department,name,phone_number,gender,country,state,city,emp_status,createdDateTime,updatedDateTime,createdBy,updatedBy from Employees "
				+ "where emp_code=:emp_code";
		
		MapSqlParameterSource param = new MapSqlParameterSource();
	    
	    param.addValue("emp_code", emp_code); 
	    
	    List<Employee> employee = namedParameterJdbcTemplate.query(sqlString, param,new EmployeeRowMapper());
	    	    
	    return employee!=null && !employee.isEmpty() ? employee.get(0) : null;
		
	}	
	
	
	@Override
	public boolean updateEmployee(Employee employee) throws EmployeeException{
		
		Employee existingEmployee = getEmployeeById(employee.getEmpCode());
		
		boolean logres= logEmployee(existingEmployee);

		if(!logres) {
			throw new EmployeeException("Failed to log employee before update");
		}
		
	    String sqlString = "UPDATE Employees SET "
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
	public boolean updateEmployeeStatus(String empCode, Status newStatus, String updatedBy) throws EmployeeException {
		Employee existingEmployee = getEmployeeById(empCode);
		
		boolean logres= logEmployee(existingEmployee);

		if(!logres) {
			throw new EmployeeException("Failed to log employee before update status");
		}
		
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

	
	
	private boolean logEmployee(Employee employee) {
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

	@Override
	public Employee getEmployeeByEmail(String email) {
		String sqlString =" Select emp_code,emp_password,email,department,name,phone_number,gender,country,state,city,emp_status,createdDateTime,updatedDateTime,createdBy,updatedBy from Employees "
				+ "where email=:email";
		
		MapSqlParameterSource param = new MapSqlParameterSource();
	    
	    param.addValue("email", email); 
	    
	    List<Employee> employee = namedParameterJdbcTemplate.query(sqlString, param,new EmployeeRowMapper());
	    	    
	    return employee!=null && !employee.isEmpty() ? employee.get(0) : null;
	}


}
