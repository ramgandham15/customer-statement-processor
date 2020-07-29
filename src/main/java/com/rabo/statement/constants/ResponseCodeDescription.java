 package com.rabo.statement.constants;

public enum ResponseCodeDescription implements ResponseCode {
	
	INTERNAL_SERVER_ERROR("500", "INTERNAL_SERVER_ERROR"), 
	VALIDATION_ERROR("400",	"BAD_REQUEST"),
	SUCCESS("200","SUCCESSFUL"),
	DUPLICATE_REFERENCE("200","DUPLICATE_REFERENCE"),
	INCORRECT_END_BALANCE("200","INCORRECT_END_BALANCE"),
	DUPLICATE_REFERENCE_INCORRECT_END_BALANCE("200","DUPLICATE_REFERENCE_INCORRECT_END_BALANCE"),
	;
	
	private final String code;

	private final String descrption;

	ResponseCodeDescription(String code, String descrption) {
		this.code = code;
		this.descrption = descrption;
	}

	public String getCode() {
		return code;
	}

	public String getDescrption() {
		return descrption;
	}

}
