package com.example.employee.repository.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.employee.enums.Status;
import com.example.employee.model.Rights;
import com.example.employee.repository.RightsRepoRedis;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class RightsRepoImplRedis implements RightsRepoRedis {
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
    private ObjectMapper objectMapper;
	


	@Override
	public boolean addRights(Rights rights) {
        redisTemplate.opsForHash().put("rights", rights.getRightCode(), rights);
		return true;
	}

	@Override
	public List<Rights> getAllRights() {
	    Map<Object, Object> rightCodes = redisTemplate.opsForHash().entries("rights");

	    List<Rights> rightsList = new ArrayList<>();
	    
	    for (Object rightsObj : rightCodes.values()) {
	        if (rightsObj != null) {
	            Rights rights = objectMapper.convertValue(rightsObj, Rights.class);
	            rightsList.add(rights);
	        }
	    }
	    
        rightsList.sort(Comparator.comparing(Rights::getRightCode));

	    return rightsList;
	}



	@Override
	public boolean updateRights(Rights rights) {
	    if (redisTemplate.opsForHash().hasKey("rights", rights.getRightCode())) {
	        redisTemplate.opsForHash().put("rights", rights.getRightCode(), rights);
	        return true;
	    }
	    return false;
	}
	
	@Override
	public boolean updateRightsStatus(String rightCode, Status newStatus, String updatedBy) {
	    Object obj = redisTemplate.opsForHash().get("rights", rightCode);
	    if (obj == null) {
	        return false;
	    }
	    
	    Rights rights = (Rights) obj;
	    rights.setRightStatus(newStatus);
	    rights.setUpdatedBy(updatedBy);
	    rights.setRightUpdatedDateTime(LocalDateTime.now());
	    redisTemplate.opsForHash().put("rights", rightCode, rights);
	    return true;
	}

	@Override
	public boolean deleteRights(String rightCode) {
	    Long removedFromHash = redisTemplate.opsForHash().delete("rights", rightCode);
	    return (removedFromHash != null && removedFromHash > 0);
	}


	@Override
	public Rights getRightById(String rightCode) {
	    Object obj = redisTemplate.opsForHash().get("rights", rightCode);
	    if (obj == null) {
	        return null;
	    }
	    return objectMapper.convertValue(obj, Rights.class);
	}



}
