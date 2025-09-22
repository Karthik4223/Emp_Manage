package com.example.employee.helpers;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.example.employee.model.EmployeeRequest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlParamSourceForEmployeeRequest {
	
	public MapSqlParameterSource getParam(EmployeeRequest employeeRequest) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		
		param.addValue("emp_RequestId", employeeRequest.getEmpRequestId());
	    param.addValue("email", employeeRequest.getEmail());
	    param.addValue("department", employeeRequest.getEmpDepartment());
	    param.addValue("name", employeeRequest.getName());
	    param.addValue("phone_number", employeeRequest.getPhoneNumber());
	    param.addValue("gender", employeeRequest.getGender().getCode());
	    param.addValue("country", employeeRequest.getCountry());
	    param.addValue("state", employeeRequest.getState());
	    param.addValue("city", employeeRequest.getCity());
	    param.addValue("emp_RequestStatus", employeeRequest.getEmpRequestStatus().getCode());
	    param.addValue("createdDateTime", employeeRequest.getEmpCreatedDateTime());
        param.addValue("updatedDateTime", employeeRequest.getEmpUpdatedDateTime());
        param.addValue("createdBy", employeeRequest.getCreatedBy());
	    param.addValue("updatedBy", employeeRequest.getUpdatedBy());

		
		return param;
	}

}
