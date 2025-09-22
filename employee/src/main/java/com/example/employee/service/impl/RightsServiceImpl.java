package com.example.employee.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employee.customException.EmployeeException;
import com.example.employee.enums.Status;
import com.example.employee.model.Rights;
import com.example.employee.repository.RightsRepo;
import com.example.employee.repository.RightsRepoRedis;
import com.example.employee.service.RightsService;
import com.example.employee.validations.Validate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RightsServiceImpl implements RightsService{
	
	@Autowired
	private RightsRepo rightsRepo;
	
	@Autowired
	private RightsRepoRedis rightsRepoRedis;

	@Override
	@Transactional(rollbackFor = EmployeeException.class)
	public boolean addRights(Rights rights) throws EmployeeException {
		
		try {
			rights.setRightCode(genRightCode());
			rights.setRightStatus(Status.ACTIVE);
			rights.setRightCreatedDateTime(LocalDateTime.now());
			rights.setCreatedBy("ADMIN");
			Validate.validateRights(rights);
			
			boolean res= rightsRepo.addRights(rights);
			if(res) {
				return rightsRepoRedis.addRights(rightsRepo.getRightById(rights.getRightCode()));
			}
			return res;
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to add Rights");
	    }
	}

	@Override
	public List<Rights> getAllRights() {
		return rightsRepoRedis.getAllRights();
	}

	@Override
	@Transactional(rollbackFor = EmployeeException.class)
	public boolean updateRights(Rights rights) throws EmployeeException {
		try {
			Validate.validateRights(rights);
			
			Rights existingRights = rightsRepo.getRightById(rights.getRightCode());
			
			if(existingRights==null) {
				throw new EmployeeException("Right not Found");
			}
			
			boolean logres= rightsRepo.logRights(existingRights);
			if(!logres) {
				throw new EmployeeException("Failed to log right before update");
			}
			rights.setRightUpdatedDateTime(LocalDateTime.now());
			rights.setUpdatedBy("ADMIN");
			boolean res= rightsRepo.updateRights(rights);
			if(res) {
				return rightsRepoRedis.updateRights(rightsRepo.getRightById(rights.getRightCode()));
			}
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to update right");
	    }
		return false;
	}

	@Override
	@Transactional(rollbackFor = EmployeeException.class)
	public boolean updateRightsStatus(String rightCode, Status newStatus, String updatedBy) throws EmployeeException {
		try {
			if (rightCode == null || rightCode.isEmpty()) {
	            throw new EmployeeException("Right code is mandatory");
	        }
	                	
	    	if (!Validate.isAlphaNumeric(rightCode)) {
	            throw new EmployeeException("Right code must be alphanumeric");
	        }   
	    	
	    	if(!Status.isValidStatus(newStatus.name())) {
	            throw new EmployeeException("Invalid Right status");
	    	}
			
	    	Rights existingRights = rightsRepo.getRightById(rightCode);
	    	
	    	if(existingRights==null) {
	    		throw new EmployeeException("Right not Found");
	    	}
	    	
	    	boolean logres= rightsRepo.logRights(existingRights);
	    	if(!logres) {
	    		throw new EmployeeException("Failed to log right before updatestatus");
	    	}
	    	
			boolean res= rightsRepo.updateRightsStatus(rightCode, newStatus, updatedBy);
			if(res) {
				return rightsRepoRedis.updateRights(rightsRepo.getRightById(rightCode));
			}
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to update right");
	    }
		return false;
	}

	@Override
	@Transactional(rollbackFor = EmployeeException.class)
	public boolean deleteRights(String rightCode) throws EmployeeException {
		try {
			if (rightCode == null || rightCode.isEmpty()) {
	            throw new EmployeeException("Right code is mandatory");
	        }
	                	
	    	if (!Validate.isAlphaNumeric(rightCode)) {
	            throw new EmployeeException("Right code must be alphanumeric");
	        }   
	    
	    	Rights existingRights = rightsRepo.getRightById(rightCode);
	    	
	    	if(existingRights==null) {
	    		throw new EmployeeException("Right not Found");
	    	}
	    	
	    	boolean logres= rightsRepo.logRights(existingRights);
	    	if(!logres) {
	    		throw new EmployeeException("Failed to log right before delete");
	    	}
	    	
			boolean res= rightsRepo.deleteRights(rightCode);
			if(res) {
				return rightsRepoRedis.deleteRights(rightCode);
			}
		} catch (EmployeeException e) {
			log.error(e.getMessage(),e);
	        throw e;
	    } catch (Exception e) {
			log.error(e.getMessage(),e);
	        throw new EmployeeException("Failed to delete right");
	    }
		return false;
	}
	
	private String genRightCode() {
		String prefix = "RIG";
		
		Integer rId = rightsRepo.getRightId();
		
		if (rId == null) {
	        rId = 1;
	    }

	    String paddedId = String.format("%04d", rId);

	    return prefix + paddedId;
	}


}
