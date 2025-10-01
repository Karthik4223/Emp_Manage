package com.example.employee.validations;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.Status;
import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeRequest;
import com.example.employee.model.EmployeeRights;
import com.example.employee.model.Rights;
import com.example.employee.model.SearchCriteria;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Validate {
	
	public void validateEmpCode(String EmpCode) throws EmployeeException {
		if (EmpCode == null || EmpCode.isEmpty()) {
            throw new EmployeeException("Employee code is mandatory");
        }
                	
    	if (!isAlphaNumeric(EmpCode)) {
            throw new EmployeeException("Employee code must be alphanumeric");
        }   
		
	}
	
	public void validateEmpPassword(String EmpPassword) throws EmployeeException {
		if (EmpPassword == null || EmpPassword.isEmpty()) {
            throw new EmployeeException("Password is mandatory");
        }
	}
	
	public void validateEmployee(Employee employee) throws EmployeeException {
		if (employee == null) {
            throw new EmployeeException("Employee cannot be null");
        }  
        
        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            throw new EmployeeException("Email is mandatory");
        }
        
        if (!isValidEmail(employee.getEmail())) {
            throw new EmployeeException("Invalid email format");
        }
        

        
        if (employee.getEmpDepartment() == null || employee.getEmpDepartment().isEmpty()) {
            throw new EmployeeException("Department is mandatory");
        }
        
        if (employee.getName() == null || employee.getName().isEmpty()) {
            throw new EmployeeException("Name is mandatory");
        }
        
        if (employee.getName().length()<4) {
            throw new EmployeeException("Name must be atleast 4 characters");
        }
        
        if (!isAlphaWithSpaces(employee.getName())) {
            throw new EmployeeException("Employee name must contain only letters and spaces");
        }
        
        if (employee.getPhoneNumber() == null || employee.getPhoneNumber().isEmpty()) {
            throw new EmployeeException("Phone number is mandatory");
        }
        

        if (employee.getPhoneNumber().trim().length()!=10) {
            throw new EmployeeException("Phone number should contains 10 digits");
        }
        
        
        if (!isValidPhoneNumber(employee.getPhoneNumber())) {
            throw new EmployeeException("Phone number should stats with numbers between [6,9]");
        }
        
        if (employee.getGender() == null) {
            throw new EmployeeException("Gender is mandatory");
        }
        
        if (employee.getCountry() == null || employee.getCountry().isEmpty()) {
            throw new EmployeeException("Country is mandatory");
        }
        
        if (employee.getState() == null || employee.getState().isEmpty()) {
            throw new EmployeeException("State is mandatory");
        }
        
        if (employee.getCity() == null || employee.getCity().isEmpty()) {
            throw new EmployeeException("City is mandatory");
        }
		
	}
	
	public void validateEmployeeRequest(EmployeeRequest employeeRequest) throws EmployeeException {
		if (employeeRequest == null) {
            throw new EmployeeException("EmployeeRequest cannot be null");
        }

        if (employeeRequest.getEmail() == null || employeeRequest.getEmail().isEmpty()) {
            throw new EmployeeException("Email is mandatory");
        }

        if (!isValidEmail(employeeRequest.getEmail())) {
            throw new EmployeeException("Invalid email format");
        }

        if (employeeRequest.getEmpDepartment() == null || employeeRequest.getEmpDepartment().isEmpty()) {
            throw new EmployeeException("Department is mandatory");
        }

        if (employeeRequest.getName() == null || employeeRequest.getName().isEmpty()) {
            throw new EmployeeException("Name is mandatory");
        }
        
        if (employeeRequest.getName().length()<4) {
            throw new EmployeeException("Name must be atleast 4 characters");
        }
        
        if (!isAlphaWithSpaces(employeeRequest.getName())) {
            throw new EmployeeException("Employee name must contain only letters and spaces");
        }

        if (employeeRequest.getPhoneNumber() == null || employeeRequest.getPhoneNumber().isEmpty()) {
            throw new EmployeeException("Phone number is mandatory");
        }
        
        if (employeeRequest.getPhoneNumber().trim().length()!=10) {
            throw new EmployeeException("Phone number should contains 10 digits");
        }
        

        if (!isValidPhoneNumber(employeeRequest.getPhoneNumber())) {
            throw new EmployeeException("Phone number should stats with numbers between [6,9]");
        }

        if (employeeRequest.getGender() == null) {
            throw new EmployeeException("Gender is mandatory");
        }

        if (employeeRequest.getCountry() == null || employeeRequest.getCountry().isEmpty()) {
            throw new EmployeeException("Country is mandatory");
        }

        if (employeeRequest.getState() == null || employeeRequest.getState().isEmpty()) {
            throw new EmployeeException("State is mandatory");
        }

        if (employeeRequest.getCity() == null || employeeRequest.getCity().isEmpty()) {
            throw new EmployeeException("City is mandatory");
        }

	}
	
	public void validateRights(Rights rights) throws EmployeeException {
		if (rights == null) {
            throw new EmployeeException("Rights object cannot be null");
        }
		
        if (rights.getRightCode() == null || rights.getRightCode().isEmpty()) {
            throw new EmployeeException("Right code is mandatory");
        }
        
        if (!isAlphaNumeric(rights.getRightCode())) {
            throw new EmployeeException("Right code must be alphanumeric");
        }
        
        if(!Status.isValidStatus(rights.getRightStatus().name())) {
            throw new EmployeeException("Invalid right status");
    	}
        
        if (rights.getRightName() == null || rights.getRightName().isEmpty()) {
            throw new EmployeeException("Right name is mandatory");
        }
        
        if (!isAlphaWithSpacesRight(rights.getRightName())) {
            throw new EmployeeException("Right name must contain only letters and spaces");
        }
	}
	
	public  void validateEmployeeRights(EmployeeRights employeeRights) throws EmployeeException {
		if (employeeRights == null) {
            throw new EmployeeException("Rights object cannot be null");
        }
		

        if (employeeRights.getEmpCode() == null || employeeRights.getEmpCode().isEmpty()) {
            throw new EmployeeException("Employee code is mandatory");
        }
        
        if (!isAlphaNumeric(employeeRights.getEmpCode())) {
            throw new EmployeeException("Employee code must be alphanumeric");
        }
		
//        if (employeeRights.getRightCode() == null || employeeRights.getRightCode().isEmpty()) {
//            throw new EmployeeException("Rights cannot be empty");
//        }
//        
//        if (employeeRights.getRightCode().stream().anyMatch(e -> !isAlphaNumeric(e))) {
//            throw new EmployeeException("Right code must be alphanumeric");
//        }

        		
	}
	

	public boolean isValidEmail(String email) {
	    return email.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
	}

	public boolean isValidPhoneNumber(String phoneNumber) {
	    return phoneNumber.matches("^[6-9][0-9]{9}$");
	}
	
	public boolean isAlphaNumeric(String input) {
	    return input.matches("^[a-zA-Z0-9]+$");
	}

//	private boolean isAlphaNumericWithSpaces(String input) {
//	    return input.matches("^[a-zA-Z0-9 ]+$");
//	}
	
	public boolean isAlphaWithSpaces(String input) {
	    return input.matches("^[a-zA-Z ]+$");
	}
	
	public boolean isAlphaWithSpacesRight(String input) {
	    return input.matches("^[a-zA-Z_ ]+$");
	}

	public static void validateCriteria(SearchCriteria criteria) throws EmployeeException {
	    if (criteria == null) {
	        throw new EmployeeException("Search criteria cannot be empty");
	    }

	    boolean isEmpty =
	        (criteria.getEmployeeNames() == null || criteria.getEmployeeNames().isEmpty()) &&
	        (criteria.getEmployeeCode() == null || criteria.getEmployeeCode().isEmpty()) &&
	        (criteria.getEmployeeDepartment() == null || criteria.getEmployeeDepartment().isEmpty()) &&
	        (criteria.getEmployeePhoneNumber() == null || criteria.getEmployeePhoneNumber().isEmpty()) &&
	        (criteria.getEmployeeStatus() == null) &&
	        (criteria.getSearchKey() == null || criteria.getSearchKey().isEmpty()) &&
	        (criteria.getEmployeeEmail() == null || criteria.getEmployeeEmail().isEmpty());

	    if (isEmpty) {
	        throw new EmployeeException("Search criteria cannot be empty");
	    }
	}

}
