package com.example.employee.helpers;

import java.time.LocalDateTime;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.example.employee.model.Rights;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlParamSoucreForRights {
	
	public MapSqlParameterSource getParam(Rights rights) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		
		param.addValue("right_code", rights.getRightCode());
		param.addValue("right_name", rights.getRightName());
		param.addValue("right_status", rights.getRightStatus().getCode());
		param.addValue("createdDateTime", rights.getRightCreatedDateTime());
		param.addValue("updatedDateTime", LocalDateTime.now());
		param.addValue("createdBy", rights.getCreatedBy());
		param.addValue("updatedBy", rights.getUpdatedBy());

		
		return param;
		
	}

}
