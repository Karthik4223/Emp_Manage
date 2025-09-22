package com.example.employee.repository.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.employee.enums.Status;
import com.example.employee.helpers.RightsRowMapper;
import com.example.employee.helpers.SqlParamSoucreForRights;
import com.example.employee.model.Rights;
import com.example.employee.repository.RightsRepo;

@Repository
public class RightsRepoImpl implements RightsRepo{
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public boolean addRights(Rights rights) {
		
		String sqlString = "Insert into Rights (right_code,right_name,right_status,createdDateTime,createdBy) values "
				+ "(:right_code,:right_name,:right_status,:createdDateTime,:createdBy)";
		
		MapSqlParameterSource param = SqlParamSoucreForRights.getParam(rights);
		
		int res = namedParameterJdbcTemplate.update(sqlString, param);
		
		return res>0;
	}

	@Override
	public List<Rights> getAllRights() {
		String sqlString = "Select right_code,right_name,right_status,createdDateTime,updatedDateTime,createdBy,updatedBy from Rights";
		
		return namedParameterJdbcTemplate.query(sqlString, new RightsRowMapper());
	}
	
	@Override
	public Rights getRightById(String right_code) {
		String sqlString = "Select right_code,right_name,right_status,createdDateTime,updatedDateTime,createdBy,updatedBy from Rights "
				+ "where right_code=:right_code";
		
		 MapSqlParameterSource param = new MapSqlParameterSource();

		 param.addValue("right_code", right_code);
		
		 List<Rights> rights = namedParameterJdbcTemplate.query(sqlString, param,new RightsRowMapper());
	
		 return rights!=null ? rights.get(0) : null;
	}
	
	@Override
	public boolean updateRights(Rights rights) {
	    String sqlString = "UPDATE Rights SET "
	            + "right_name = :right_name, "
	            + "right_status = :right_status, "
	            + "updatedDateTime = :updatedDateTime, "
	            + "updatedBy = :updatedBy "
	            + "WHERE right_code = :right_code"; 

		MapSqlParameterSource param = SqlParamSoucreForRights.getParam(rights);

	    
	    int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);
	    return rowsEffected > 0;  
	}
	
	@Override
	public boolean updateRightsStatus(String rightCode, Status newStatus, String updatedBy) {
	    String sqlString = "UPDATE Rights SET "
	            + "right_status = :right_status, "
	            + "updatedDateTime = :updatedDateTime, "
	            + "updatedBy = :updatedBy "
	            + "WHERE right_code = :right_code";  

	    MapSqlParameterSource param = new MapSqlParameterSource();

	    param.addValue("right_code", rightCode);  
	    param.addValue("right_status", newStatus.getCode()); 
	    param.addValue("updatedDateTime", LocalDateTime.now()); 
	    param.addValue("updatedBy", updatedBy);

	    int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);
	    return rowsEffected > 0;  
	}
	
	@Override
	public boolean deleteRights(String rightCode) {
	    String sqlString = "DELETE FROM Rights WHERE right_code = :right_code";  
	    
	    MapSqlParameterSource param = new MapSqlParameterSource();
	    param.addValue("right_code", rightCode);

	    int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);
	    return rowsEffected > 0;
	}
	
	@Override
	public boolean logRights(Rights right) {
		String sqlRight = " Select right_id,right_code,right_name,right_status,createdDateTime,updatedDateTime,createdBy,updatedBy,sysTime from Rights "
				+ "where right_code=:right_code";
		
		
		String sqlString = "Insert into Rights_log "+sqlRight;
		MapSqlParameterSource param = new MapSqlParameterSource();
	    param.addValue("right_code", right.getRightCode());  
        
        int rowsEffected = namedParameterJdbcTemplate.update(sqlString, param);
		
		return rowsEffected>0;
		
	}

	@Override
	public Integer getRightId() {
		String sqlString ="Select MAX(right_id)+1 from Rights";
		
		Integer res = namedParameterJdbcTemplate.queryForObject(sqlString, new MapSqlParameterSource(), Integer.class);
		
		return res!=null ? res : null;
	}
	

}
