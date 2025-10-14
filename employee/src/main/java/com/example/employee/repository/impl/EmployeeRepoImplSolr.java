package com.example.employee.repository.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.Status;
import com.example.employee.helpers.ConvertSolrDocumentToEmployee;
import com.example.employee.model.Employee;
import com.example.employee.model.SearchCriteria;
import com.example.employee.repository.EmployeeRepoSolr;

@Repository
public class EmployeeRepoImplSolr implements EmployeeRepoSolr{
	
	@Autowired
	private SolrClient solrClient;
	
	@Override
	public boolean addEmployeeToSolr(Employee employee) throws EmployeeException  {
	    String solrCollection = "Employees";

        SolrInputDocument document = new SolrInputDocument();

        document.addField("emp_code", employee.getEmpCode());
        document.addField("emp_password", employee.getEmpPassword());
        document.addField("email", employee.getEmail());
        document.addField("department", employee.getEmpDepartment());
        document.addField("name", employee.getName());
        document.addField("phone_number", employee.getPhoneNumber());
        document.addField("gender", employee.getGender().getCode());
        document.addField("country", employee.getCountry());
        document.addField("state", employee.getState());
        document.addField("city", employee.getCity());
        document.addField("emp_status", employee.getEmployeeStatus().getCode());

		if (employee.getEmpCreatedDateTime() != null) {
		 Date createdDate = Date.from(employee.getEmpCreatedDateTime().atZone(ZoneId.systemDefault()).toInstant());
		 document.addField("createdDateTime", createdDate);
		}
		
		if (employee.getEmpUpdatedDateTime() != null) {
		 Date updatedDate = Date.from(employee.getEmpUpdatedDateTime().atZone(ZoneId.systemDefault()).toInstant());
		 document.addField("updatedDateTime", updatedDate);
		}
		
		document.addField("createdBy", employee.getCreatedBy());
		document.addField("updatedBy", employee.getUpdatedBy());
		document.addField("sysTime", new Date());	

        UpdateResponse res = null;
		try {
			res = solrClient.add(solrCollection, document);
			solrClient.commit(solrCollection);
		} catch (SolrServerException | IOException e) {
			throw new EmployeeException("Falied to add Employee to Solr");
		}
        
		return res.getStatus()==0;
	}
	
	@Override
	public boolean updateEmployeeInSolr(Employee employee) throws EmployeeException  {
	    String solrCollection = "Employees";
	    String empCode = employee.getEmpCode();
	    
		
	    deleteEmployeeFromSolr(empCode);
		

	    SolrInputDocument updatedDoc = new SolrInputDocument();

	    updatedDoc.setField("emp_code", empCode);
	    updatedDoc.setField("emp_password", employee.getEmpPassword());
	    updatedDoc.setField("email", employee.getEmail());
	    updatedDoc.setField("department", employee.getEmpDepartment());
	    updatedDoc.setField("name", employee.getName());
	    updatedDoc.setField("phone_number", employee.getPhoneNumber());
	    updatedDoc.setField("gender", employee.getGender().getCode());
	    updatedDoc.setField("country", employee.getCountry());
	    updatedDoc.setField("state", employee.getState());
	    updatedDoc.setField("city", employee.getCity());
	    updatedDoc.setField("emp_status", employee.getEmployeeStatus().getCode());

	    if (employee.getEmpCreatedDateTime() != null) {
		  Date createdDate = Date.from(employee.getEmpCreatedDateTime().atZone(ZoneId.systemDefault()).toInstant());
		  updatedDoc.addField("createdDateTime", createdDate);
		}
		
		if (employee.getEmpUpdatedDateTime() != null) {
		  Date updatedDate = Date.from(employee.getEmpUpdatedDateTime().atZone(ZoneId.systemDefault()).toInstant());
		  updatedDoc.addField("updatedDateTime", updatedDate);
		}

	    updatedDoc.setField("createdBy", employee.getCreatedBy());
	    updatedDoc.setField("updatedBy", employee.getUpdatedBy());

	    updatedDoc.setField("sysTime", new Date()); 
	    
	    UpdateResponse res = null;
		try {
		    res = solrClient.add(solrCollection, updatedDoc);
			solrClient.commit(solrCollection);
		} catch (SolrServerException | IOException e) {
			throw new EmployeeException("Falied to add Employee to Solr");
		}

	    return res.getStatus() == 0;
	}
	
	@Override
	public boolean deleteEmployeeFromSolr(String empCode) throws EmployeeException  {
	    String solrCollection = "Employees";

	    String query = "emp_code:\"" + empCode + "\"";
	    UpdateResponse response = null;
		try {
			response = solrClient.deleteByQuery(solrCollection, query);
			solrClient.commit(solrCollection);
		} catch (SolrServerException | IOException e) {
			throw new EmployeeException("Falied to delete Employee from Solr");
		}

	    return response.getStatus() == 0;
	}
	
	
	@Override
	public boolean updateEmployeeStatusInSolr(String empCode, Status newStatus , String updatedBy) throws EmployeeException  {
	    String solrCollection = "Employees";

	    SolrInputDocument doc = new SolrInputDocument();

	    doc.addField("emp_code", empCode);
	    
	    Map<String, String> StatusMap = new HashMap<>();
	    StatusMap.put("set", newStatus.getCode());
	    
	    Map<String, Date> UpdateMap = new HashMap<>();
	    UpdateMap.put("set", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
	    
	    Map<String, Date> SysTimeMap = new HashMap<>();
	    SysTimeMap.put("set", new Date());
	    
	    Map<String, String> updateByMap = new HashMap<>();
	    updateByMap.put("set", updatedBy);
	    

	    doc.addField("emp_status", StatusMap);
	    doc.addField("updatedDateTime", UpdateMap);
	    doc.addField("updatedBy", updateByMap);
	    doc.addField("sysTime", SysTimeMap);

	    UpdateResponse response;
		try {
			response = solrClient.add(solrCollection, doc);
			solrClient.commit(solrCollection);
		} catch (SolrServerException | IOException e) {
			throw new EmployeeException("Falied to Update EmployeeStatus in Solr");
		}

	    return response.getStatus() == 0;
	}
	
	@Override
	public List<Employee> getAllEmployees() throws EmployeeException {
	    String solrCollection = "Employees";
	    List<Employee> employees = new ArrayList<>();

	    SolrQuery query = new SolrQuery();
	    query.setQuery("*:*");
	    query.setRows(1000000);
	    query.setSort("emp_code", SolrQuery.ORDER.asc);


	    try {
	        QueryResponse response = solrClient.query(solrCollection, query);
	        SolrDocumentList docs = response.getResults();

	        for (SolrDocument doc : docs) {
	            employees.add(ConvertSolrDocumentToEmployee.convertSolrDocumentToEmployee(doc));
	        }

	    } catch (SolrServerException | IOException e) {
	        throw new EmployeeException("Failed to fetch all employees from Solr", e);
	    }

	    return employees;
	}

	@Override
	public Employee getEmployeeById(String empCode) throws EmployeeException {
	    String solrCollection = "Employees";

	    try {
	        SolrDocument doc = solrClient.query(solrCollection, new SolrQuery("emp_code:\"" + empCode + "\"")).getResults().stream().findFirst().orElse(null);

	        if (doc == null) {
	            return null; 
	        }

	        return ConvertSolrDocumentToEmployee.convertSolrDocumentToEmployee(doc);

	    } catch (SolrServerException | IOException e) {
	        throw new EmployeeException("Failed to fetch employee by id from Solr", e);
	    }
	}

	@Override
	public List<Employee> findByCriteria(SearchCriteria criteria) throws EmployeeException {
		SolrQuery solrQuery = new SolrQuery();

		List<String> queryParts = new ArrayList<>();

		if (criteria.getEmployeeNames() != null && !criteria.getEmployeeNames().isEmpty()) {

		    String namesQuery = "name:*" + criteria.getEmployeeNames() + "*";

		    if (!namesQuery.isEmpty()) queryParts.add(namesQuery);
		}

		if (criteria.getEmployeeDepartment() != null && !criteria.getEmployeeDepartment().isEmpty()) {
		    String depQuery = "department:*" + criteria.getEmployeeDepartment() + "*" ;
		    if (!depQuery.isEmpty()) queryParts.add(depQuery);
		}

		if (criteria.getEmployeeStatus() != null) {
		    queryParts.add("emp_status:*" + criteria.getEmployeeStatus().getCode() + "*");
		}

		if (criteria.getEmployeeEmail() != null && !criteria.getEmployeeEmail().isEmpty()) {
		    String emailQuery = "email:*" + criteria.getEmployeeEmail() + "*";
		    
		    if (!emailQuery.isEmpty()) queryParts.add(emailQuery);
		}

		if (criteria.getEmployeeCode() != null && !criteria.getEmployeeCode().isEmpty()) {
		    String codeQuery = "emp_code:*" + criteria.getEmployeeCode() + "*";
		    
		    if (!codeQuery.isEmpty()) queryParts.add(codeQuery);
		}

		if (criteria.getEmployeePhoneNumber() != null && !criteria.getEmployeePhoneNumber().isEmpty()) {
		    String phoneQuery = "phone_number:*" + criteria.getEmployeePhoneNumber() + "*";
		                       
		    if (!phoneQuery.isEmpty()) queryParts.add(phoneQuery);
		}

		if (criteria.getSearchKey() != null && !criteria.getSearchKey().isEmpty()) {
		    String[] tokens = criteria.getSearchKey().trim().split("\\s*\\|\\s*|\\s+");
		    List<String> tokenQueries = new ArrayList<>();
		    for (String token : tokens) {
		        if (!token.isEmpty()) {
		            tokenQueries.add("name:*" + token + "* OR email:*" + token + "* OR emp_code:*" + token + "*");
		        }
		    }
		    if (!tokenQueries.isEmpty()) queryParts.add(String.join(" OR ", tokenQueries));
		}

		String queryString = queryParts.isEmpty() ? "*:*" : String.join(" OR ", queryParts);

		solrQuery.setQuery(queryString);
		solrQuery.setRows(10000);


        QueryResponse response = null;
		try {
			response = solrClient.query("Employees", solrQuery);
		} catch (SolrServerException | IOException e) {
			throw new EmployeeException("Failed to perform search");
		}
		
        SolrDocumentList docs = response.getResults();

        List<Employee> results = new ArrayList<>();

        for (SolrDocument doc : docs) {
            Employee emp = ConvertSolrDocumentToEmployee.convertSolrDocumentToEmployee(doc);
            results.add(emp);
        }
        
        
		return results;
	}

}
