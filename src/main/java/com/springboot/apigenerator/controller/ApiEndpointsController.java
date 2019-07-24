/**
 * 
 */
package com.springboot.apigenerator.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.apigenerator.exceptions.EntityNotFoundException;
import com.springboot.apigenerator.model.ResponseMessage;
import com.springboot.apigenerator.service.ApiEndPointService;

/**
 * @author swathy
 *
 */
@RestController
public class ApiEndpointsController {

	@Autowired
	private ApiEndPointService apiService;

	private ResponseMessage res;
	
	public ApiEndpointsController() {
		this.res = new ResponseMessage();
	}

	@GetMapping(value = "/getEndPoints/{projectId}")
	public ResponseEntity<ResponseMessage> getApiEndpoints(@PathVariable String projectId)
			throws EntityNotFoundException {
		List<Map<String, Object>> endPoints = apiService.getAllEndPointsForId(UUID.fromString(projectId));	 
		return new ResponseEntity<ResponseMessage>(this.res.setData(endPoints, true),HttpStatus.OK);
		
	}

}
