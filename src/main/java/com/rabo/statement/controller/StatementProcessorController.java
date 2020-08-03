package com.rabo.statement.controller;

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
import com.rabo.statement.service.StatementProcessorService;
import com.rabo.statement.uitil.ResponseUtil;

@RestController
public class StatementProcessorController implements StatementProcessorApi {

	public static final Logger LOG = LoggerFactory.getLogger(StatementProcessorController.class);

	@Autowired
	StatementProcessorService statementService;

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<StatementResponse> processRecords(List<Records> records) {
		LOG.info("inside processRecords()");

		StatmentServiceResponse serviceResponse = null;
		try {
			if (records != null) {
				serviceResponse = statementService.processJSONStatement(records);
			} else {
				return (ResponseEntity<StatementResponse>) ResponseUtil
						.preperEntityForBadRequest(ResponseCodeDescription.VALIDATION_ERROR, serviceResponse);
			}

		} catch (Exception exception) {
			LOG.error(StatementsProcessorConstants.EXCEPTION_IN_PROCESSING_JSON, exception);
			return (ResponseEntity<StatementResponse>) ResponseUtil
					.preperEntityForException(ResponseCodeDescription.INTERNAL_SERVER_ERROR, serviceResponse);
		}
		return (ResponseEntity<StatementResponse>) ResponseUtil.preperEntityForOk(serviceResponse);
	}

}
