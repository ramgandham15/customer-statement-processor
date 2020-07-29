package com.rabo.statement.service.Impl;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rabo.statement.constants.ResponseCodeDescription;
import com.rabo.statement.constants.StatementsProcessorConstants;
import com.rabo.statement.model.Records;
import com.rabo.statement.model.StatmentServiceResponse;
import com.rabo.statement.service.StatementProcessorService;

@Service
public class StatementProcessServiceImpl implements StatementProcessorService {

	private static final Logger LOG = LoggerFactory.getLogger(StatementProcessServiceImpl.class);

	@Override
	public StatmentServiceResponse processJSONStatement(List<Records> records) {
		List<Records> recordsData = null;
		StatmentServiceResponse statmentServiceResponse = new StatmentServiceResponse();
		try {

			LOG.info("processJSONStatement");
			recordsData = filterDuplicateReference(records);
			List<Records> failureEndBalance = validateMutation(records);
			if (recordsData.size() > 0 && failureEndBalance.size() > 0) {
				recordsData.addAll(failureEndBalance);
				statmentServiceResponse.setTransactions(recordsData);
				statmentServiceResponse
						.setServiceResponse(ResponseCodeDescription.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);
			} else if (recordsData.size() > 0) {
				statmentServiceResponse.setTransactions(recordsData);
				statmentServiceResponse.setServiceResponse(ResponseCodeDescription.DUPLICATE_REFERENCE);

			} else if (failureEndBalance.size() > 0) {
				statmentServiceResponse.setTransactions(failureEndBalance);
				statmentServiceResponse.setServiceResponse(ResponseCodeDescription.INCORRECT_END_BALANCE);
			} else {
				statmentServiceResponse.setTransactions(recordsData);
				statmentServiceResponse.setServiceResponse(ResponseCodeDescription.SUCCESS);
			}

		} catch (Exception exception) {
			LOG.error("processJSONStatement", exception);
		}
		return statmentServiceResponse;
	}

	/**
	 * Filters the duplicate records reference number
	 * 
	 * @param transactions
	 * @return
	 */
	private List<Records> filterDuplicateReference(List<Records> transactions) {
		List<Records> duplicate = transactions.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream() // perform group by count																							
				.filter(e -> e.getValue() > 1L).map(Entry<Records, Long>::getKey).collect(Collectors.toList());// take only those element whose count is greater than 1 and using map take only value
																													
		return transactions.stream().map(stmt -> {
			for (Records duplicateStmt : duplicate) {
				if (duplicateStmt.getReference().equals(stmt.getReference())) {
					return stmt;
				}
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());

	}

	/**
	 * Filters the records with incorrect end balance 
	 * 
	 * @param records
	 * @return
	 */
	private List<Records> validateMutation(List<Records> records) {
		return records.stream().filter(record -> !isValid(record)).collect(Collectors.toList());
	}

	private boolean isValid(Records record) {

		if (record.getMutation().contains(StatementsProcessorConstants.ADD)) {
			return (Math.round(Double.parseDouble(record.getStartBalance())
					+ Double.parseDouble(record.getMutation())) == Math.round(record.getEndBalance()));
		} else {
			String mutaionRemoval = record.getMutation().replace(StatementsProcessorConstants.SUBSTRCT,
					StatementsProcessorConstants.EMPTY);
			return (Math.round(Double.parseDouble(record.getStartBalance()) - Double.parseDouble(mutaionRemoval)) == Math
					.round(record.getEndBalance()));
		}

	}

}
