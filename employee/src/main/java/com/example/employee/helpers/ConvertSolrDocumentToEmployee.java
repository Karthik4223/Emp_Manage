package com.example.employee.helpers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.solr.common.SolrDocument;

import com.example.employee.enums.Gender;
import com.example.employee.enums.Status;
import com.example.employee.model.Employee;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConvertSolrDocumentToEmployee {
	

	public Employee convertSolrDocumentToEmployee(SolrDocument doc) {
	    Employee employee = new Employee();

	    employee.setEmpCode((String) doc.getFieldValue("emp_code"));
	    employee.setEmpPassword((String) doc.getFieldValue("emp_password"));
	    employee.setEmail((String) doc.getFieldValue("email"));
	    employee.setEmpDepartment((String) doc.getFieldValue("department"));
	    employee.setName((String) doc.getFieldValue("name"));
	    employee.setPhoneNumber((String) doc.getFieldValue("phone_number"));

	    String genderCode = (String) doc.getFieldValue("gender");
	    if (genderCode != null) {
	        employee.setGender(Gender.fromCode(genderCode)); 
	    }

	    employee.setCountry((String) doc.getFieldValue("country"));
	    employee.setState((String) doc.getFieldValue("state"));
	    employee.setCity((String) doc.getFieldValue("city"));

	    String statusCode = (String) doc.getFieldValue("emp_status");
	    if (statusCode != null) {
	        employee.setEmployeeStatus(Status.fromCode(statusCode)); 
	    }

	    Date createdDate = (Date) doc.getFieldValue("createdDateTime");
	    if (createdDate != null) {
	        employee.setEmpCreatedDateTime(LocalDateTime.ofInstant(createdDate.toInstant(), ZoneId.systemDefault()));
	    }

	    Date updatedDate = (Date) doc.getFieldValue("updatedDateTime");
	    if (updatedDate != null) {
	        employee.setEmpUpdatedDateTime(LocalDateTime.ofInstant(updatedDate.toInstant(), ZoneId.systemDefault()));
	    }

	    employee.setCreatedBy((String) doc.getFieldValue("createdBy"));
	    employee.setUpdatedBy((String) doc.getFieldValue("updatedBy"));

	    return employee;
	}



}
