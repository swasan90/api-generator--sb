/**
 * 
 */
package com.springboot.apigenerator.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.springboot.apigenerator.exceptions.EntityNotFoundException;

/**
 * @author swathy
 *
 */
public interface ApiEndPointService {
	 
	List<Map<String, Object>> getAllEndPointsForId(UUID id) throws EntityNotFoundException;
}
