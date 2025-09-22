package com.example.employee.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.example.employee.enums.EmployeeRequestStatus;
import com.example.employee.enums.Gender;
import com.example.employee.model.EmployeeRequest;

public class EmployeeRequestRowMapper implements RowMapper<EmployeeRequest>{

	@Override
	public EmployeeRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
		EmployeeRequest employeeRequest = new EmployeeRequest();
		
		employeeRequest.setEmpRequestId(rs.getInt("emp_RequestId"));
		employeeRequest.setEmail(rs.getString("email"));
		employeeRequest.setEmpDepartment(rs.getString("department"));
		employeeRequest.setName(rs.getString("name"));
		employeeRequest.setPhoneNumber(rs.getString("phone_number"));
		employeeRequest.setGender(rs.getString("gender")!=null ? Gender.fromCode(rs.getString("gender")) : null);
		employeeRequest.setCountry(rs.getString("country"));
		employeeRequest.setState(rs.getString("state"));
		employeeRequest.setCity(rs.getString("city"));
		employeeRequest.setEmpRequestStatus(rs.getString("emp_RequestStatus")!=null ? EmployeeRequestStatus.fromCode(rs.getString("emp_RequestStatus")): null);
		employeeRequest.setEmpCreatedDateTime(rs.getTimestamp("createdDateTime").toLocalDateTime());
		
		Timestamp timestamp = rs.getTimestamp("updatedDateTime");
		if (timestamp != null) {
		    employeeRequest.setEmpUpdatedDateTime(timestamp.toLocalDateTime());
		} else {
		    employeeRequest.setEmpUpdatedDateTime(null);
		}
		
		employeeRequest.setCreatedBy(rs.getString("createdBy"));
		employeeRequest.setUpdatedBy(rs.getString("updatedBy"));
		
		
		return employeeRequest;
	}

}
