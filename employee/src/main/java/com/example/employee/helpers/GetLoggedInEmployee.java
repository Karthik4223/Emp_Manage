package com.example.employee.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.employee.model.EmployeeLogin;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetLoggedInEmployee {
	
	public static String getLoggedInEmployeeCode() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || !authentication.isAuthenticated()) {
	        return null;
	    }

	    Object principal = authentication.getPrincipal();
	    if (principal instanceof EmployeeLogin) {
	        return ((EmployeeLogin) principal).getEmpCode();
	    }
	    return principal.toString();
	}


}
