/**
 * Interface to define schema builder methods.
 */
package com.springboot.apigenerator.service;

import com.springboot.apigenerator.model.SchemaBuilder;

/**
 * @author swathy
 *
 */
public interface SchemaBuilderService {
	
	//Function declaration to create schema for dynamic domain
	boolean createSchemaForDomain(SchemaBuilder schema);

}
