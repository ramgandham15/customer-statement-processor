package com.rabo.statement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rabo.statement.model.Records;
import com.rabo.statement.model.StatmentServiceResponse;
@Service
public interface StatementProcessorService {
	public StatmentServiceResponse processJSONStatement(List<Records> records);

}
