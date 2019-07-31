/**
 * 
 */
package com.springboot.apigenerator.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.springboot.apigenerator.model.FieldWrapper;

/**
 * @author swathy
 *
 */
public interface SchemaMappingService {
	
	Set<String> getSchemaColumns(UUID id);
	
	List<FieldWrapper> getSchemaColumnsWithType(UUID id);

}
