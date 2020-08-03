package com.rabo.statement.uitil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rabo.statement.constants.ResponseCode;
import com.rabo.statement.model.Response;
import com.rabo.statement.model.ServiceResponse;
import com.rabo.statement.model.StatmentServiceResponse;
import com.rabo.statement.model.Status;


public interface ResponseUtil {

	public static ResponseEntity<?> preperEntityForException(ResponseCode responseCode, StatmentServiceResponse serviceResponse) {
		Status status = serviceResponse.getStatementResponse().getStatus();
		status.setStatusCode(responseCode.getCode());
		status.setStatusDescription(responseCode.getDescrption());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serviceResponse);
	}

	public static ResponseEntity<?> preperEntityForBadRequest(ResponseCode responseCode, StatmentServiceResponse serviceResponse) {
		Status status = serviceResponse.getStatementResponse().getStatus();
		status.setStatusCode(responseCode.getCode());
		status.setStatusDescription(responseCode.getDescrption());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceResponse);
	}

	public static ResponseEntity<?> preperEntityForOk(ServiceResponse serviceResponse) {
		Status status = serviceResponse.getStatementResponse().getStatus();
		if (serviceResponse != null) {
			status.setStatusCode(serviceResponse.getServiceResponse().getCode());
			status.setStatusDescription(serviceResponse.getServiceResponse().getDescrption());
		}
		return ResponseEntity.status(HttpStatus.OK).body(serviceResponse.getStatementResponse());
	}
}
