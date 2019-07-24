/**
 * Class to implement process dynamic http request.
 */
package com.springboot.apigenerator.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.datastax.driver.core.querybuilder.Update;
import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.ProjectDomain;
import com.springboot.apigenerator.model.RequestPayload;
import com.springboot.apigenerator.repository.ProjectDomainRepository;

/**
 * @author swathy
 *
 */
@Service("httpRequestService")
public class HttpRequestHandleServiceImpl implements HttpRequestHandleService {

	private Logger logger = LoggerFactory.getLogger(HttpRequestHandleServiceImpl.class);

	@Autowired
	private ProjectDomainRepository projRepo;

	@Autowired
	private CassandraOperations cassandraTemplate;

	@Value("${cassandra.keyspace}")
	private String keySpaceName;

	/**
	 * Function to list all the records from the given table.
	 * 
	 * @param projectName,domainName
	 */
	@Override
	public List<Map<String, Object>> listAll(String projectName, String domainName) {
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
		logger.info("Entering try catch block");
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

	/**
	 * Function to get record by id.
	 * 
	 * @param project_name,domain_name,id
	 * @return List<Map<String,Object>>
	 */
	@Override
	public List<Map<String, Object>> getRecordByID(String project_name, String domain_name, UUID id) {
		ProjectDomain project = projRepo.findByProjectNameAndDomainName(project_name, domain_name);
		if (project != null) {
			Where where = findById(id, domain_name);
			return cassandraTemplate.getCqlOperations().queryForList(where);
		}
		return null;

	}

	/**
	 * Function to update the record for the given table.
	 * 
	 * @param projectName,domainName,domainId
	 * @return boolean
	 * @throws EntityFoundException
	 */
	@Override
	public boolean updateRecord(RequestPayload payload, String projectName, String domainName, UUID domainId)
			throws EntityFoundException {
		ProjectDomain proj = projRepo.findByProjectNameAndDomainName(projectName, domainName);
		try {
			Where where = findById(domainId, domainName);
			boolean isEmpty = cassandraTemplate.getCqlOperations().queryForList(where).isEmpty();
			if (proj != null && !isEmpty) {
				Update update = QueryBuilder.update(keySpaceName, domainName);
				for (Entry<String, Object> entry : payload.attributes.entrySet()) {
					update.with().and(QueryBuilder.set(entry.getKey(), entry.getValue()));
				}
				update.where(QueryBuilder.eq("id", domainId)).and(QueryBuilder.eq("project_id", proj.getId()));
				return cassandraTemplate.getCqlOperations().execute(update);
			}
		} catch (DataIntegrityViolationException e) {
			throw new EntityFoundException(e.getMessage());
		}
		return false;

	}

	/**
	 * Function to get where query by id.
	 * 
	 * @param id
	 * @param domain_name
	 * @return
	 */
	private Where findById(UUID id, String domain_name) {
		Clause clause = QueryBuilder.eq("id", id);
		Where where = QueryBuilder.select().from(domain_name).where(clause);
		return where;

	}

	@Override
	public boolean deleteRecord(String projectName, String domainName, UUID domainId) throws EntityFoundException {
		ProjectDomain proj = projRepo.findByProjectNameAndDomainName(projectName, domainName);
		try {
			Where where = findById(domainId, domainName);
			boolean isEmpty = cassandraTemplate.getCqlOperations().queryForList(where).isEmpty();
			if (proj != null && !isEmpty) {
				com.datastax.driver.core.querybuilder.Delete.Where delete = QueryBuilder.delete().from(domainName)
						.where(QueryBuilder.eq("id", domainId)).and(QueryBuilder.eq("project_id", proj.getId()));
				return cassandraTemplate.getCqlOperations().execute(delete);
			}
		} catch (DataIntegrityViolationException e) {
			throw new EntityFoundException(e.getMessage());
		}
		return false;

	}

}
