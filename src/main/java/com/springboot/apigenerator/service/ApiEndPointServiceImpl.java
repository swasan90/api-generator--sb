/**
 * 
 */
package com.springboot.apigenerator.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.springboot.apigenerator.exceptions.EntityNotFoundException;

/**
 * @author swathy
 *
 */
@Service("apiEndpointService")
public class ApiEndPointServiceImpl implements ApiEndPointService{
	
	@Autowired
	private CassandraOperations cassandraTemplate;

	@Override
	public List<Map<String, Object>> getAllEndPointsForId(UUID id) throws EntityNotFoundException {	 
		Where where = QueryBuilder.select().all().from("api_endpoints").where().and(QueryBuilder.eq("project_id", id));	
		try {			
			return cassandraTemplate.getCqlOperations().queryForList(where);
		}catch(DataAccessException e) {
			throw new  EntityNotFoundException("No record found for this project id");
		}	
	}

}
