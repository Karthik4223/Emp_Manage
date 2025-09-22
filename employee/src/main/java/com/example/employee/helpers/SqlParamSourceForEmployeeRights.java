package com.example.employee.helpers;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.example.employee.model.EmployeeRights;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlParamSourceForEmployeeRights {
	
	public  MapSqlParameterSource getParam(EmployeeRights employeeRights) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		
		param.addValue("emp_code", employeeRights.getEmpCode());
//		param.addValue("right_code", employeeRights.getRigCode());
		param.addValue("createdDateTime", employeeRights.getEmpRightCreatedDateTime());
		param.addValue("createdBy", employeeRights.getCreatedBy());
		param.addValue("updatedBy", employeeRights.getUpdatedBy());
		
		return param;

	}

}
