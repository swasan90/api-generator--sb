/**
 * Class to implement process dynamic http request.
 */
package com.springboot.apigenerator.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.springboot.apigenerator.model.ProjectDomain;
import com.springboot.apigenerator.model.RequestPayload;
import com.springboot.apigenerator.repository.ProjectDomainRepository;

/**
 * @author swathy
 *
 */
@Service("httpRequestService")
public class HttpRequestHandleServiceImpl implements HttpRequestHandleService {

	@Autowired
	private ProjectDomainRepository projRepo;

	@Autowired
	private CassandraOperations cassandraTemplate;

	/**
	 * Function to list all the records from the given table.
	 * 
	 * @param projectName,domainName
	 */
	@Override
	public <T> List<Map<String, Object>> listAll(String projectName, String domainName) {
		Select select = QueryBuilder.select().from(domainName);
		return cassandraTemplate.getCqlOperations().queryForList(select);
	}

	/**
	 * Function to insert record on the given table.
	 * 
	 * @param payload, projectName,domainName
	 * @throws Exception
	 */
	@Override
	public boolean insertRecord(RequestPayload payload, String projectName, String domainName) throws Exception {
		ProjectDomain proj = projRepo.findByProjectNameAndDomainName(projectName, domainName);
		try {
			if (proj != null) {
				payload.setProjectId(proj.getId());
				Insert insert = QueryBuilder.insertInto(domainName).values(payload.getFieldNames(),
						payload.getFieldValues());
				System.out.println(insert);
				return cassandraTemplate.getCqlOperations().execute(insert);
			}
			return false;
		} catch (DataIntegrityViolationException e) {
			throw new Exception(e.getMessage());
		}

	}

	@Override
	public List<Map<String, Object>> getRecordByID(String project_name, String domain_name, UUID id) {
		ProjectDomain project = projRepo.findByProjectNameAndDomainName(project_name, domain_name);
		if(project !=null) {
			Clause clause = QueryBuilder.eq("id",id);
			Where where = QueryBuilder.select().from(domain_name).where(clause);
			System.out.println(where);
			return cassandraTemplate.getCqlOperations().queryForList(where);
		}
		return null;
		 
	}
	
	

}
