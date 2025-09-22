package com.example.employee.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.jdbc.core.RowMapper;

import com.example.employee.model.EmployeeRights;

public class EmployeeRightsRowMapper implements RowMapper<EmployeeRights>{

	@Override
	public EmployeeRights mapRow(ResultSet rs, int rowNum) throws SQLException {
		EmployeeRights employeeRights = new EmployeeRights();
		employeeRights.setEmpCode(rs.getString("emp_code"));
		employeeRights.setRightCode(new ArrayList<>());
		employeeRights.getRightCode().add(rs.getString("right_code"));
		employeeRights.setRightCodeAndName(new HashMap<>());
		employeeRights.getRightCodeAndName().put(rs.getString("right_code"), rs.getString("right_name"));
		employeeRights.setEmpRightCreatedDateTime(rs.getTimestamp("createdDateTime").toLocalDateTime());
		employeeRights.setCreatedBy(rs.getString("createdBy"));
		employeeRights.setUpdatedBy(rs.getString("updatedBy"));
		
		return employeeRights;
	}
	

}
