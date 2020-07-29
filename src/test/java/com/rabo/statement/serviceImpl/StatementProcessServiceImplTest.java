package com.rabo.statement.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabo.statement.constants.ResponseCodeDescription;
import com.rabo.statement.model.Records;
import com.rabo.statement.model.StatmentServiceResponse;
import com.rabo.statement.service.StatementProcessorService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatementProcessServiceImplTest {

	@Autowired
	StatementProcessorService statementProcessorService;

	List<Records> records = new ArrayList<>();
	List<Records> uniqueRecords = new ArrayList<>();
	List<Records> incorrectEndbalRecords = new ArrayList<>();
	List<Records> failureEndbalRecords = new ArrayList<>();

	@Before
	public void setUp() throws Exception {
		Records records1 = new Records();
		records1.setReference(Long.valueOf("194261"));
		records1.setAccountNumber("NL91RABO0315273637");
		records1.setDescription("description");
		records1.setEndBalance(-20.23);
		records1.setStartBalance("21.6");
		records1.setMutation("-41.83");
		Records records2 = new Records();
		records2.setReference(Long.valueOf("194261"));
		records2.setAccountNumber("NL91RABO0315273637");
		records2.setDescription("description");
		records2.setEndBalance(-20.23);
		records2.setStartBalance("21.6");
		records2.setMutation("-41.83");
		records.add(records1);
		records.add(records2);

		Records records3 = new Records();
		records3.setReference(Long.valueOf("194261"));
		records3.setAccountNumber("NL91RABO0315273637");
		records3.setDescription("description");
		records3.setEndBalance(-20.23);
		records3.setStartBalance("21.6");
		records3.setMutation("-41.83");
		Records records4 = new Records();
		records4.setReference(Long.valueOf("194263"));
		records4.setAccountNumber("NL91RABO0315273637");
		records4.setDescription("description");
		records4.setEndBalance(-20.23);
		records4.setStartBalance("21.6");
		records4.setMutation("-41.83");
		uniqueRecords.add(records3);
		uniqueRecords.add(records4);
		
		Records records5 = new Records();
		records5.setReference(Long.valueOf("194264"));
		records5.setAccountNumber("NL91RABO0315273637");
		records5.setDescription("description");
		records5.setEndBalance(-20.23);
		records5.setStartBalance("21.6");
		records5.setMutation("-41.83");
		Records records6 = new Records();
		records6.setReference(Long.valueOf("194263"));
		records6.setAccountNumber("NL91RABO0315273637");
		records6.setDescription("description");
		records6.setEndBalance(-20.23);
		records6.setStartBalance("21.6");
		records6.setMutation("-42.83");
		incorrectEndbalRecords.add(records5);
		incorrectEndbalRecords.add(records6);
		
		failureEndbalRecords.add(records6);

	}

	@Test
	public void testStamentProcessForDuplicates() throws Exception {

		StatmentServiceResponse expected = new StatmentServiceResponse();
		expected.setTransactions(records);
		expected.setServiceResponse(ResponseCodeDescription.DUPLICATE_REFERENCE);

		StatmentServiceResponse actual = statementProcessorService.processJSONStatement(records);
		assertThat(expected).isEqualToComparingFieldByFieldRecursively(actual);
	}

	@Test
	public void testStamentProcessForSucess() throws Exception {
		List<Records> records = new ArrayList<>();

		StatmentServiceResponse expected = new StatmentServiceResponse();
		expected.setTransactions(records);
		expected.setServiceResponse(ResponseCodeDescription.SUCCESS);

		StatmentServiceResponse actual = statementProcessorService.processJSONStatement(uniqueRecords);
		assertThat(expected).isEqualToComparingFieldByFieldRecursively(actual);
	}
	
	@Test
	public void testStamentProcessForIncoorectEndBalance() throws Exception {

		StatmentServiceResponse expected = new StatmentServiceResponse();
		expected.setTransactions(failureEndbalRecords);
		expected.setServiceResponse(ResponseCodeDescription.INCORRECT_END_BALANCE);

		StatmentServiceResponse actual = statementProcessorService.processJSONStatement(incorrectEndbalRecords);
		assertThat(expected).isEqualToComparingFieldByFieldRecursively(actual);
	}
	
	@Test
	public void testStamentProcessForDuplicateRefaAndIncoorectEndBalance() throws Exception {
		 List<Records> execduplicateIncorrectEndBal=new ArrayList<>();
		 execduplicateIncorrectEndBal.addAll(records);
		 execduplicateIncorrectEndBal.addAll(failureEndbalRecords);
		 
		 List<Records> duplicateIncorrectEndBal=new ArrayList<>();
		 duplicateIncorrectEndBal.addAll(records);
		 duplicateIncorrectEndBal.addAll(incorrectEndbalRecords);

		StatmentServiceResponse expected = new StatmentServiceResponse();
		expected.setTransactions(execduplicateIncorrectEndBal);
		expected.setServiceResponse(ResponseCodeDescription.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);

		StatmentServiceResponse actual = statementProcessorService.processJSONStatement(duplicateIncorrectEndBal);
		assertThat(expected).isEqualToComparingFieldByFieldRecursively(actual);
	}

}
