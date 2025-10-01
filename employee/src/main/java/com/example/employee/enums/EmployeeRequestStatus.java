package com.example.employee.enums;

public enum EmployeeRequestStatus {
	CREATED("C"),APPROVED("A"),REJECTED("R"),TRANSIT("T");
	
	private String code;
	
	EmployeeRequestStatus(String code) {
		this.code=code;
	}
	
	public String getCode() {
		return code;
	}
	
	 public static EmployeeRequestStatus fromCode(String code) { 
        for (EmployeeRequestStatus status : EmployeeRequestStatus.values()) {
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
