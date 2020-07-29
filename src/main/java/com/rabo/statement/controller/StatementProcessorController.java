package com.rabo.statement.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.rabo.statement.api.StatementProcessorApi;
import com.rabo.statement.constants.ResponseCodeDescription;
import com.rabo.statement.constants.StatementsProcessorConstants;
import com.rabo.statement.model.Records;
import com.rabo.statement.model.StatementResponse;
import com.rabo.statement.model.StatmentServiceResponse;
import com.rabo.statement.model.Transaction;
import com.rabo.statement.service.StatementProcessorService;
import com.rabo.statement.uitil.ResponseUtil;

@RestController
public class StatementProcessorController implements StatementProcessorApi {

	public static final Logger LOG = LoggerFactory.getLogger(StatementProcessorController.class);

    
	@Autowired
	StatementProcessorService statementService;
	
	@Override
	public ResponseEntity<StatementResponse> processRecords(List<Records> records) {
		LOG.info("inside processRecords()");
		StatementResponse response = new StatementResponse();
		StatmentServiceResponse serviceResponse=null;
		try {
			serviceResponse=statementService.processJSONStatement(records);
			List<Transaction> transactions = new ArrayList<>();
			
			if (serviceResponse != null && serviceResponse.getTransactions() != null) {
				
				serviceResponse.getTransactions().forEach(transaction -> {

					Transaction responseTransaction = new Transaction();
					responseTransaction.setReference(transaction.getReference());
					responseTransaction.setAccountNumber(transaction.getAccountNumber());
					transactions.add(responseTransaction);
					LOG.debug(StatementsProcessorConstants.FAILURE_RESPONSE,responseTransaction);
				});
			}
		
		
			LOG.info(StatementsProcessorConstants.STATEMENT_RESPONSE);
			response.setTransaction(transactions);
		} catch (Exception exception) {
			LOG.error(StatementsProcessorConstants.EXCEPTION_IN_PROCESSING_JSON, exception);
			return (ResponseEntity<StatementResponse>) ResponseUtil.preperEntityForException(ResponseCodeDescription.INTERNAL_SERVER_ERROR, response);
		}
	    return (ResponseEntity<StatementResponse>) ResponseUtil.preperEntityForOk(serviceResponse,response);
	}

}
