package com.example.employee.repository;

import com.example.employee.customException.EmployeeException;
import com.example.employee.model.Rights;

public interface RightsReposSolr {
	
	boolean addRightsToSolr(Rights rights) throws EmployeeException;

	boolean updateRightsInSolr(Rights rights) throws EmployeeException;

	boolean deleteRightsFromSolr(String rightCode) throws EmployeeException;


}
