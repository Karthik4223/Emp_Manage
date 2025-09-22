package com.example.employee.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.example.employee.enums.Gender;
import com.example.employee.enums.Status;
import com.example.employee.model.Employee;

public class EmployeeRowMapper implements RowMapper<Employee>{

	@Override
	public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
		Employee employee =new Employee();
		
		employee.setEmpCode(rs.getString("emp_code"));
		employee.setEmpPassword(rs.getString("emp_password"));
		employee.setEmail(rs.getString("email"));
		employee.setEmpDepartment(rs.getString("department"));
		employee.setName(rs.getString("name"));
		employee.setPhoneNumber(rs.getString("phone_number"));
		employee.setGender(rs.getString("gender")!=null ? Gender.fromCode(rs.getString("gender")) : null);
		employee.setCountry(rs.getString("country"));
		employee.setState(rs.getString("state"));
		employee.setCity(rs.getString("city"));
		employee.setEmployeeStatus(rs.getString("emp_status")!=null ? Status.fromCode(rs.getString("emp_status")): null);
		employee.setEmpCreatedDateTime(rs.getTimestamp("createdDateTime").toLocalDateTime());
		
		Timestamp timestamp = rs.getTimestamp("updatedDateTime");
		if (timestamp != null) {
			employee.setEmpUpdatedDateTime(timestamp.toLocalDateTime());
		} else {
			employee.setEmpUpdatedDateTime(null);
		}
		
		employee.setCreatedBy(rs.getString("createdBy"));
		employee.setUpdatedBy(rs.getString("updatedBy"));
		
		
		
		return employee;
	}

}
