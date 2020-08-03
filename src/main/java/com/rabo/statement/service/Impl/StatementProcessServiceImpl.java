package com.rabo.statement.service.Impl;

import java.util.ArrayList;
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
import com.rabo.statement.model.StatementResponse;
import com.rabo.statement.model.StatmentServiceResponse;
import com.rabo.statement.model.Transaction;
import com.rabo.statement.service.StatementProcessorService;

@Service
public class StatementProcessServiceImpl implements StatementProcessorService {

	private static final Logger LOG = LoggerFactory.getLogger(StatementProcessServiceImpl.class);

	@Override
	public StatmentServiceResponse processJSONStatement(List<Records> records) {
		List<Records> recordsData = null;
		StatmentServiceResponse statmentServiceResponse = new StatmentServiceResponse();
		LOG.info("processJSONStatement");
		recordsData = filterDuplicateReference(records);
		List<Records> failureEndBalance = getEndBalanceErrorRecords(records);
		statmentServiceResponse = validateRecords(recordsData, statmentServiceResponse, failureEndBalance);

		return statmentServiceResponse;
	}

	/**
	 * @param recordsData
	 * @param statmentServiceResponse
	 * @param failureEndBalance
	 * @return 
	 */
	private StatmentServiceResponse validateRecords(List<Records> recordsData, StatmentServiceResponse statmentServiceResponse,
			List<Records> failureEndBalance) {
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
		statmentServiceResponse = serviceResponseValidation(statmentServiceResponse);
		return statmentServiceResponse;
	}

	/**
	 * @param statmentServiceResponse
	 * @return 
	 */
	private StatmentServiceResponse serviceResponseValidation(StatmentServiceResponse statmentServiceResponse) {
		List<Transaction> transactions = new ArrayList<>();
		StatementResponse response=new StatementResponse();
		if (statmentServiceResponse != null && statmentServiceResponse.getTransactions() != null) {
			
			statmentServiceResponse.getTransactions().forEach(transaction -> {

				Transaction responseTransaction = new Transaction();
				responseTransaction.setReference(transaction.getReference());
				responseTransaction.setAccountNumber(transaction.getAccountNumber());
				transactions.add(responseTransaction);
			});

			LOG.info(StatementsProcessorConstants.STATEMENT_RESPONSE);
			response.setTransaction(transactions);
			statmentServiceResponse.setStatementResponse(response);
		}
		return statmentServiceResponse;
	}

	/**
	 * Filters the duplicate records reference number
	 * 
	 * @param records
	 * @return
	 */
	private List<Records> filterDuplicateReference(List<Records> records) {
		List<Records> duplicate = records.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream() // perform group by count																							
				.filter(e -> e.getValue() > 1L).map(Entry<Records, Long>::getKey).collect(Collectors.toList());// take only those element whose count is greater than 1 and using map take only value
																													
		return records.stream().map(stmt -> {
			for (Records duplicateStmt : duplicate) {
				if (duplicateStmt.getReference().equals(stmt.getReference())) {
					return stmt;
				}
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());

	}

	/**
	 * @return List<Records> if startbalance - mutation != endbalance then
	 *         endbalance is wrong that list ll be returned.
	 */
	public List<Records> getEndBalanceErrorRecords(List<Records> records) {
		List<Records> endBalanceErrorRecords = new ArrayList<Records>();
		for (Records record : records) {
			if (Math.round((record.getStartBalance() - record.getMutation()) - Math.round(record.getEndBalance())) != 0) {
				endBalanceErrorRecords.add(record);
			}
		}
		return endBalanceErrorRecords;
	}

}
