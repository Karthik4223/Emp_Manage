package com.example.employee.enums;

public enum Status {
	ACTIVE("A"),INACTIVE("I");
	
	private String code;
	
	Status(String code) {
		this.code=code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static Status fromCode(String code) { 
        for (Status status : Status.values()) {
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
