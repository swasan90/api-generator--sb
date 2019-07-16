/**
 * 
 */
package com.springboot.apigenerator.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	@GetMapping(value = "/{projectName}/{domainName}", produces = "application/json")
	public ResponseEntity<ResponseMessage> listAllRecords(@PathVariable String projectName,
			@PathVariable String domainName) throws JsonProcessingException {
		List<Map<String, Object>> result = httpRequestService.listAll(projectName, domainName);

		return new ResponseEntity<ResponseMessage>(this.res.setMessage(listmapToJsonString(result), true),
				HttpStatus.OK);
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
	@PostMapping(value = "/{projectName}/{domainName}", consumes = "applciation/json", produces = "application/json")
	public ResponseEntity<ResponseMessage> saveRecord(@RequestBody RequestPayload reqPayload,
			@PathVariable String projectName, @PathVariable String domainName) throws Exception {
		if (httpRequestService.insertRecord(reqPayload, projectName, domainName)) {
			return new ResponseEntity<ResponseMessage>(this.res.setMessage("Successfully inserted new record", true),
					HttpStatus.CREATED);
		}
		return new ResponseEntity<ResponseMessage>(this.res.setMessage("Unable to persist record", false),
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
	@GetMapping(value = "/{projectName}/{domainName}/{domainId}", produces = "application/json")
	public ResponseEntity<ResponseMessage> showRecord(@PathVariable(name = "projectName") String projName,
			@PathVariable String domainName, @PathVariable UUID domainId) throws JsonProcessingException {
		List<Map<String, Object>> result = httpRequestService.getRecordByID(projName, domainName, domainId);
		return new ResponseEntity<ResponseMessage>(this.res.setMessage(listmapToJsonString(result), true),
				HttpStatus.OK);
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
	@PostMapping(value = "/{projectName}/{domainName}/{domainId}", produces = "application/json", consumes = "application/json")
	public ResponseEntity<ResponseMessage> updateRecord(@PathVariable String projectName,
			@PathVariable String domainName, @PathVariable UUID domainId, @RequestBody RequestPayload reqPayload) throws EntityFoundException {
		if (httpRequestService.updateRecord(reqPayload, projectName, domainName, domainId)) {
			return new ResponseEntity<ResponseMessage>(this.res.setMessage("Successfully updated record", true),
					HttpStatus.CREATED);
		}
		return new ResponseEntity<ResponseMessage>(this.res.setMessage("Unable to update record", false),
				HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Function to handle delete request
	 * @param projectName
	 * @param domainName
	 * @param domainId
	 * @return
	 * @throws EntityFoundException 
	 */
	@DeleteMapping(value = "/{projectName}/{domainName}/{domainId}", produces = "application/json", consumes = "application/json")
	public ResponseEntity<ResponseMessage> deleteRecord(@PathVariable String projectName,
			@PathVariable String domainName, @PathVariable UUID domainId) throws EntityFoundException {
		System.out.println("processing delete ");
		if (httpRequestService.deleteRecord(projectName, domainName, domainId)) {
			return new ResponseEntity<ResponseMessage>(this.res.setMessage("Successfully deleted record", true),
					HttpStatus.CREATED);
		}
		return new ResponseEntity<ResponseMessage>(this.res.setMessage("Unable to delete record", false),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Function to convert list of map to json string	
	 * @param list
	 * @return
	 * @throws JsonProcessingException
	 */
	private String listmapToJsonString(List<Map<String, Object>> list) throws JsonProcessingException {
		JSONArray json_arr = new JSONArray();
		if (list != null) {
			for (Map<String, Object> map : list) {
				JSONObject json_obj = new JSONObject();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					try {
						json_obj.put(key, value);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				json_arr.put(json_obj);
			}
		}
		return json_arr.toString();
	}

}
