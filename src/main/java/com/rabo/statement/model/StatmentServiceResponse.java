package com.rabo.statement.model;

import java.util.List;

import com.rabo.statement.constants.ResponseCode;



public class StatmentServiceResponse implements ServiceResponse {

	private ResponseCode serviceResponse;
	private List<Records> transactions;

	@Override
	public ResponseCode getServiceResponse() {
		return serviceResponse;
	}

	public void setServiceResponse(ResponseCode serviceResponse) {
		this.serviceResponse = serviceResponse;
	}

	public List<Records> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Records> transactions) {
		this.transactions = transactions;
	}

}
