/**
 * 
 */
package com.springboot.apigenerator.service;

import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.SchemaBuilder;

/**
 * @author swathy
 *
 */
public interface SchemaBuilderService {
	
	void createSchemaForDomain(SchemaBuilder schema) throws EntityFoundException;

}
