package com.example.employee.helpers;

import org.springframework.dao.DataIntegrityViolationException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageCauseForException {
	
	public String getMessageCause(DataIntegrityViolationException e) {
		
		Throwable rootCause = e.getRootCause();
        String message = rootCause != null ? rootCause.getMessage() : e.getMessage();
        String column = "unknown column";
        if (message != null) {
            int idx = message.indexOf("for key");
            if (idx != -1) {
                column = message.substring(idx + 8).replaceAll("['`]", "").trim();
            }
        }
        return column;
	}

}
