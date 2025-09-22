package com.example.employee.enums;

public enum Gender {
	MALE("M"),FEMALE("F");
	
	private String code;
	
	Gender(String code) {
		this.code=code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static Gender fromCode(String code) { 
	    for (Gender status : Gender.values()) {
	        if (status.code.equalsIgnoreCase(code)) {
	            return status;
	        }
	    }
		return null;
   }
	
	 public static boolean isValidStatus(String value) {
        for (Status status : Status.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
	 }

}
