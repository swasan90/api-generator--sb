/**
 * 
 */
package com.springboot.apigenerator.controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.apigenerator.model.FieldWrapper;
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
	
	@GetMapping(value="getColumnData/{id}")
	public ResponseEntity<ResponseMessage> getSchemaData(@PathVariable String id) {		
		Set<String> result = schemaService.getSchemaColumns(UUID.fromString(id));
		return new ResponseEntity<ResponseMessage>(this.res.setData(result, true),HttpStatus.OK);		
	}
	
	@GetMapping(value="getSchemaMapping/{id}")
	public ResponseEntity<ResponseMessage> getSchemaMapping(@PathVariable String id) {		
		List<FieldWrapper> result = schemaService.getSchemaColumnsWithType(UUID.fromString(id));		 
		return new ResponseEntity<ResponseMessage>(this.res.setData(result, true),HttpStatus.OK);		
	}

}
