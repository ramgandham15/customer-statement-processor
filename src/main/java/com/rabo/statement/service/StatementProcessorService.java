package com.rabo.statement.service;

import java.util.List;

import com.rabo.statement.model.Records;
import com.rabo.statement.model.StatmentServiceResponse;

public interface StatementProcessorService {
	public StatmentServiceResponse processJSONStatement(List<Records> records);

}
