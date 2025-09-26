package com.example.employee.repository;

import java.util.List;

import com.example.employee.enums.Status;
import com.example.employee.model.Rights;

public interface RightsRepoRedis {
	
	boolean addRights(Rights rights);

	List<Rights> getAllRights();

	boolean updateRights(Rights rights);

	boolean updateRightsStatus(String rightCode, Status newStatus, String updatedBy);

	boolean deleteRights(String rightCode);

	Rights getRightById(String right_code);
	
	List<String> getRightsByGroup(String group);

}
