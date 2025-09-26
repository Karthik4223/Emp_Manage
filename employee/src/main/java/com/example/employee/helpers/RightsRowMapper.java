package com.example.employee.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.example.employee.enums.Status;
import com.example.employee.model.Rights;

public class RightsRowMapper implements RowMapper<Rights>{

	@Override
	public Rights mapRow(ResultSet rs, int rowNum) throws SQLException {
		Rights rights=new Rights();
		
		rights.setRightCode(rs.getString("right_code"));
		rights.setRightName(rs.getString("right_name"));
		rights.setRightStatus(rs.getString("right_status")!=null ? Status.fromCode(rs.getString("right_status")) : null);
		rights.setGroup(rs.getString("right_group"));
		rights.setRightCreatedDateTime(rs.getTimestamp("createdDateTime").toLocalDateTime());
		
		Timestamp timestamp = rs.getTimestamp("updatedDateTime");
		if (timestamp != null) {
			rights.setRightUpdatedDateTime(timestamp.toLocalDateTime());
		} else {
			rights.setRightUpdatedDateTime(null);
		}
		
		rights.setCreatedBy(rs.getString("createdBy"));
		rights.setUpdatedBy(rs.getString("updatedBy"));
		
		return rights;
	}
	

}
