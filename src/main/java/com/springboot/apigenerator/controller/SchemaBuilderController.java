/**
 * Class to handle  request and response of schema builder
 */
package com.springboot.apigenerator.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.apigenerator.model.ResponseMessage;
import com.springboot.apigenerator.model.SchemaBuilder;
import com.springboot.apigenerator.service.SchemaBuilderService;

/**
 * @author swathy
 *
 */
@RestController
public class SchemaBuilderController {

	private Logger logger = LoggerFactory.getLogger(SchemaBuilderController.class);
	
	@Autowired
	private SchemaBuilderService schemaBuilderService;
	
	private ResponseMessage res;

	public SchemaBuilderController() {
		this.res = new ResponseMessage();
	}
	
	/**
	 * Function to build schema for the given domain
	 * @param schema
	 * @return res
	 *  
	 */
	@PostMapping(value="/buildSchema",consumes="application/json",produces="application/json")
	public ResponseEntity<ResponseMessage> buildSchema(@Valid @RequestBody SchemaBuilder schema) {
		if(schemaBuilderService.createSchemaForDomain(schema)) {
			logger.info("Successfully created table for the given domain ");
			return new ResponseEntity<ResponseMessage>(this.res.setMessage("Successfully created table",true), HttpStatus.CREATED);
			
		}else {
			logger.error("Unable to create table");
			return new ResponseEntity<ResponseMessage>(
					this.res.setMessage("Project/domain doesn't exists.Unable to create",false),
					HttpStatus.BAD_REQUEST);
		}
	}
	
}
