package com.example.employee.repository.impl;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.employee.customException.EmployeeException;
import com.example.employee.model.Rights;
import com.example.employee.repository.RightsReposSolr;

@Repository
public class RightsRepoImplSolr implements RightsReposSolr{
	
	@Autowired
	private SolrClient solrClient;

	@Override
	public boolean addRightsToSolr(Rights rights) throws EmployeeException {
		
	    String solrCollection = "Rights";

		
		 SolrInputDocument document = new SolrInputDocument();
         document.addField("right_code", rights.getRightCode());
         document.addField("right_name", rights.getRightName());
         document.addField("right_status", rights.getRightStatus().getCode());
         
         if (rights.getRightCreatedDateTime() != null) {
    		 Date createdDate = Date.from(rights.getRightCreatedDateTime().atZone(ZoneId.systemDefault()).toInstant());
    		 document.addField("createdDateTime", createdDate);
    		}
    		
    		if (rights.getRightUpdatedDateTime() != null) {
    		 Date updatedDate = Date.from(rights.getRightUpdatedDateTime().atZone(ZoneId.systemDefault()).toInstant());
    		 document.addField("updatedDateTime", updatedDate);
    		}
    		
         document.addField("createdBy", rights.getCreatedBy());
         document.addField("updatedBy", rights.getUpdatedBy());

         document.addField("sysTime", new Date());	

         
        UpdateResponse res;
		try {
			res = solrClient.add(solrCollection,document);
			solrClient.commit(solrCollection);
		} catch (SolrServerException | IOException e) {
			throw new EmployeeException("Falied to add Rights to Solr");
		}
        
         
		return res.getStatus()==0;
	}
	
	private SolrDocument existingDoc = null;
	
	@Override
	public boolean updateRightsInSolr(Rights rights) throws EmployeeException {
	    String solrCollection = "Rights";
	    String rightCode = rights.getRightCode();

		try {
			existingDoc = solrClient.getById(solrCollection, rightCode);
		} catch (SolrServerException | IOException e) {
			throw new EmployeeException("Falied to update Rights in Solr");
		}
	    deleteRightsFromSolr(rightCode);

	    SolrInputDocument updatedDoc = new SolrInputDocument();

	    if (existingDoc != null) {
	        existingDoc.getFieldNames().forEach(field -> {
	            updatedDoc.addField(field, existingDoc.getFieldValue(field));
	        });
	    }

	    updatedDoc.setField("right_code", rightCode);
	    updatedDoc.setField("right_name", rights.getRightName());
	    updatedDoc.setField("right_status", rights.getRightStatus().getCode());

	    if (rights.getRightCreatedDateTime() != null) {
	        Date createdDate = Date.from(rights.getRightCreatedDateTime().atZone(ZoneId.systemDefault()).toInstant());
	        updatedDoc.setField("createdDateTime", createdDate);
	    }

	    if (rights.getRightUpdatedDateTime() != null) {
	        Date updatedDate = Date.from(rights.getRightUpdatedDateTime().atZone(ZoneId.systemDefault()).toInstant());
	        updatedDoc.setField("updatedDateTime", updatedDate);
	    }

	    updatedDoc.setField("createdBy", rights.getCreatedBy());
	    updatedDoc.setField("updatedBy", rights.getUpdatedBy());

	    updatedDoc.setField("sysTime", new Date());

	    UpdateResponse response = null;
		try {
			response = solrClient.add(solrCollection, updatedDoc);
			solrClient.commit(solrCollection);
		} catch (SolrServerException | IOException e) {
			throw new EmployeeException("Falied to update Rights in Solr");

		}

	    return response.getStatus() == 0;
	}

	@Override
	public boolean deleteRightsFromSolr(String rightCode) throws EmployeeException  {
	    String solrCollection = "Rights";
	    
	    String query = "right_code:\"" + rightCode + "\"";
	    UpdateResponse response = null;
		try {
			response = solrClient.deleteByQuery(solrCollection, query);
			solrClient.commit(solrCollection);
		} catch (SolrServerException | IOException e) {
			throw new EmployeeException("Falied to delete rights from Solr");
		}

	    return response.getStatus() == 0;
	}


	
}
