/**
 * 
 */
package com.springboot.apigenerator.service;

import java.util.Set;
import java.util.UUID;

/**
 * @author swathy
 *
 */
public interface SchemaMappingService {
	
	Set<String> getSchemaColumns(UUID id);

}
