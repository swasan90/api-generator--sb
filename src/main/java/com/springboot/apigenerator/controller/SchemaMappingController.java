/**
 * 
 */
package com.springboot.apigenerator.controller;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.apigenerator.model.ResponseMessage;
import com.springboot.apigenerator.service.SchemaMappingService;

/**
 * @author swathy
 *
 */
@RestController
public class SchemaMappingController {
	
	@Autowired
	private SchemaMappingService schemaService;
	
	private ResponseMessage res;
	
	public SchemaMappingController() {
		this.res = new ResponseMessage();
	}
	
	@GetMapping(value="getSchemaData/{id}")
	public ResponseEntity<ResponseMessage> getSchemaData(@PathVariable String id) {		
		Set<String> result = schemaService.getSchemaColumns(UUID.fromString(id));
		return new ResponseEntity<ResponseMessage>(this.res.setData(result, true),HttpStatus.OK);		
	}

}
