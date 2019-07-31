/**
 * 
 */
package com.springboot.apigenerator.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.RequestPayload;
import com.springboot.apigenerator.model.ResponseMessage;
import com.springboot.apigenerator.service.HttpRequestHandleService;
import com.springboot.apigenerator.validation.ValidPayloadImpl;

/**
 * @author swathy
 *
 */
@RestController
@RequestMapping("/apigenerator")
public class HttpRequestHandleController {

	private ResponseMessage res;

	public HttpRequestHandleController() {
		this.res = new ResponseMessage();
	}

	@Autowired
	private HttpRequestHandleService httpRequestService;
 

	/**
	 * Function to handle get request.
	 * 
	 * @param projectName
	 * @param domainName
	 * @return
	 * @throws JsonProcessingException
	 */
	@GetMapping(value = "/{projectName}/{domainName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> listAllRecords(@PathVariable String projectName,
			@PathVariable String domainName) {
		List<Map<String, Object>> result = httpRequestService.listAll(projectName, domainName);
		return new ResponseEntity<ResponseMessage>(this.res.setData(result, true), HttpStatus.OK);
	}

	/**
	 * Function to handle post request.
	 * 
	 * @param reqPayload
	 * @param projectName
	 * @param domainName
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/{projectName}/{domainName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> saveRecord(@PathVariable String projectName, @PathVariable String domainName,
			@RequestBody RequestPayload reqPayload) throws Exception {
		if (httpRequestService.insertRecord(reqPayload, projectName, domainName)) {
			return new ResponseEntity<ResponseMessage>(this.res.setData("Successfully inserted new record", true, null),
					HttpStatus.CREATED);
		}

		return new ResponseEntity<ResponseMessage>(this.res.setData("Unable to persist record", false, null),
				HttpStatus.BAD_REQUEST);

	}

	/**
	 * Function to show record by record id for the given table and application.
	 * 
	 * @param projName
	 * @param domainName
	 * @param domainId
	 * @return
	 * @throws JsonProcessingException
	 */
	@GetMapping(value = "/{projectName}/{domainName}/{domainId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> showRecord(@PathVariable(name = "projectName") String projName,
			@PathVariable String domainName, @PathVariable UUID domainId) throws JsonProcessingException {
		List<Map<String, Object>> result = httpRequestService.getRecordByID(projName, domainName, domainId);
		return new ResponseEntity<ResponseMessage>(this.res.setData(result, true), HttpStatus.OK);
	}

	/**
	 * Function to update record.
	 * 
	 * @param projectName
	 * @param domainName
	 * @param domainId
	 * @param reqPayload
	 * @return
	 * @throws EntityFoundException
	 */
	@PostMapping(value = "/{projectName}/{domainName}/{domainId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> updateRecord(@PathVariable String projectName,
			@PathVariable String domainName, @PathVariable UUID domainId, @RequestBody RequestPayload reqPayload)
			throws EntityFoundException {
		if (httpRequestService.updateRecord(reqPayload, projectName, domainName, domainId)) {
			return new ResponseEntity<ResponseMessage>(this.res.setData("Successfully updated record", true, null),
					HttpStatus.CREATED);
		}
		return new ResponseEntity<ResponseMessage>(this.res.setData("Unable to update record", false, null),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Function to handle delete request
	 * 
	 * @param projectName
	 * @param domainName
	 * @param domainId
	 * @return
	 * @throws EntityFoundException
	 */
	@DeleteMapping(value = "/{projectName}/{domainName}/{domainId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> deleteRecord(@PathVariable String projectName,
			@PathVariable String domainName, @PathVariable UUID domainId) throws EntityFoundException {
		System.out.println("processing delete ");
		if (httpRequestService.deleteRecord(projectName, domainName, domainId)) {
			return new ResponseEntity<ResponseMessage>(this.res.setData("Successfully deleted record", true, null),
					HttpStatus.CREATED);
		}
		return new ResponseEntity<ResponseMessage>(this.res.setData("Unable to delete record", false, null),
				HttpStatus.BAD_REQUEST);
	}

}
