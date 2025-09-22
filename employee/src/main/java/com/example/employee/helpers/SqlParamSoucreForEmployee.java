package com.example.employee.helpers;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.example.employee.model.Employee;

public class SqlParamSoucreForEmployee {
	
	public MapSqlParameterSource getParam(Employee employee) {
		
		
		MapSqlParameterSource param = new MapSqlParameterSource();
		
		param.addValue("emp_code", employee.getEmpCode());
		param.addValue("emp_password", employee.getEmpPassword());
		param.addValue("email", employee.getEmail());
		param.addValue("department", employee.getEmpDepartment());
		param.addValue("name", employee.getName());
		param.addValue("phone_number", employee.getPhoneNumber());
		param.addValue("gender", employee.getGender().getCode());
		param.addValue("country", employee.getCountry());
		param.addValue("state", employee.getState());
		param.addValue("city", employee.getCity());
		param.addValue("emp_status", employee.getEmployeeStatus().getCode());
		
		param.addValue("createdDateTime", employee.getEmpCreatedDateTime());
		param.addValue("updatedDateTime", employee.getEmpUpdatedDateTime());
		
		param.addValue("createdBy", employee.getCreatedBy());
		param.addValue("updatedBy", employee.getUpdatedBy());

		 return param;
		
		
	}

}
