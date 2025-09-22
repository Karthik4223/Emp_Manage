package com.example.employee.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.employee.helpers.EmployeeRightsRowMapper;
import com.example.employee.helpers.SqlParamSourceForEmployeeRights;
import com.example.employee.model.EmployeeRights;
import com.example.employee.repository.EmployeeRightsMappingRepo;

@Repository
public class EmployeeRightsMappingRepoImpl implements EmployeeRightsMappingRepo{
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public boolean addEmployeeRights(EmployeeRights employeeRights) {
	    String sqlString = "INSERT INTO EmployeeRights(emp_code, right_code, createdDateTime, createdBy) " +
	                       "VALUES (:emp_code, :right_code, :createdDateTime, :createdBy)";

	    List<SqlParameterSource> batchParams = new ArrayList<>();

	    for (String rightCode : employeeRights.getRightCode()) {
	        MapSqlParameterSource param = SqlParamSourceForEmployeeRights.getParam(employeeRights);
	        param.addValue("right_code", rightCode);
	        batchParams.add(param);
	    }

	    int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sqlString, batchParams.toArray(new SqlParameterSource[0]));

	    return Arrays.stream(updateCounts).anyMatch(count -> count > 0);
	}


	@Override
	public List<EmployeeRights> getAllEmployeeRights() {
		String sqlString = "Select er.emp_code,er.right_code,r.right_name,er.createdDateTime,er.createdBy,er.updatedBy from EmployeeRights er "
				+ " JOIN Rights r on er.right_code = r.right_code";
		
	    List<EmployeeRights> empRights = namedParameterJdbcTemplate.query(sqlString, new EmployeeRightsRowMapper());

		
		Map<String, EmployeeRights> empRightsMap = new LinkedHashMap<>();

	    for (EmployeeRights right : empRights) {
	        String empCode = right.getEmpCode();

	        if (!empRightsMap.containsKey(empCode)) {
	        	empRightsMap.put(empCode, right);
	        } else {
	            EmployeeRights existing = empRightsMap.get(empCode);
	            existing.getRightCode().addAll(right.getRightCode());
	        }
	    }
		
	    return new ArrayList<>(empRightsMap.values());
	}

	@Override
	public EmployeeRights getEmployeeRightsByEmpCode(String emp_code) {
		String sqlString = "Select er.emp_code,er.right_code,r.right_name,er.createdDateTime,er.createdBy,er.updatedBy from EmployeeRights er "
				+ "JOIN Rights r on er.right_code = r.right_code where er.emp_code=:emp_code";
		
		MapSqlParameterSource param = new MapSqlParameterSource();
		
		param.addValue("emp_code", emp_code);
		
		List<EmployeeRights> employeeRights= namedParameterJdbcTemplate.query(sqlString,param, new EmployeeRightsRowMapper());
	
		return employeeRights!=null && !employeeRights.isEmpty() ? employeeRights.get(0) : null;
		
	}
	
	

	
	@Override
	public boolean deleteEmployeeRights(String empCode, List<String> rightCodes) {
	  
	    String sql = "DELETE FROM EmployeeRights WHERE emp_code = :emp_code AND right_code IN (:right_codes)";

	    MapSqlParameterSource params = new MapSqlParameterSource()
	            .addValue("emp_code", empCode)
	            .addValue("right_codes", rightCodes);

	    int rowsAffected = namedParameterJdbcTemplate.update(sql, params);

	    return rowsAffected > 0;
	}


	@Override
	public boolean logEmployeeRights(EmployeeRights employeeRights) {
	    String sqlString = "INSERT INTO EmployeeRights_log " +
	                       "SELECT emp_code, right_code, createdDateTime, createdBy, updatedBy, sysTime " +
	                       "FROM EmployeeRights " +
	                       "WHERE emp_code = :emp_code AND right_code = :right_code";

	    int totalRowsAffected = 0;

	    for (String rightCode : employeeRights.getRightCode()) {
	        MapSqlParameterSource param = new MapSqlParameterSource();
	        param.addValue("emp_code", employeeRights.getEmpCode());
	        param.addValue("right_code", rightCode);

	        int rowsAffected = namedParameterJdbcTemplate.update(sqlString, param);
	        totalRowsAffected += rowsAffected;
	    }

	    return totalRowsAffected > 0;
	}


	

}
