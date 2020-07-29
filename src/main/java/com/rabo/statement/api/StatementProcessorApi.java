package com.rabo.statement.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rabo.statement.model.Records;
import com.rabo.statement.model.StatementResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping(value = "/api/rest/v1/statement/")
@Api(tags = { "Customer Statement Service" })
public interface StatementProcessorApi {
		@PostMapping(value = "processRecords", produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiOperation(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "Statement Json Processor", response = StatementResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Operation Successful "),
				@ApiResponse(code = 401, message = "You are not authorized to view the details"),
				@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
				@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
		public ResponseEntity<StatementResponse> processRecords(@RequestBody
				List<Records> transaction);
	

}
