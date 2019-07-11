package com.springboot.apigenerator.service;

import java.util.Optional;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.FieldWrapper;
import com.springboot.apigenerator.model.ProjectDomain;
import com.springboot.apigenerator.model.SchemaBuilder;
import com.springboot.apigenerator.repository.ProjectDomainRepository;

@Service("schemaBuilderService")
public class SchemaBuilderServiceImpl implements SchemaBuilderService {

	private Logger logger = LoggerFactory.getLogger(SchemaBuilderServiceImpl.class);

	@Autowired
	private ProjectDomainRepository projectDomainRepo;
	
	@Autowired
	private CassandraOperations cassandraTemplate;
	 

	@Override
	public void createSchemaForDomain(SchemaBuilder schema) throws EntityFoundException {

		Optional<ProjectDomain> project = projectDomainRepo.findById(schema.getProjectId());
		try {
			if (project.isPresent()) {	
				logger.info("Constructing Query");
				StringJoiner strJoin = new StringJoiner(",");					 
				for (FieldWrapper field : schema.getFields()) {						
					 StringBuilder strBuild = new StringBuilder();
					 strBuild.append(field.getFieldName()).append(" ").append(findFieldType(field));		
					 strJoin.add(strBuild);					 
				}	
				String cqlQuery = "CREATE TABLE "+project.get().getDomainName().toLowerCase()+
						"( project_id UUID , id UUID,"+strJoin+",PRIMARY KEY(project_id,id))";
 				cassandraTemplate.getCqlOperations().execute(cqlQuery);
 				logger.info("created table");
			}

		} catch (DataIntegrityViolationException e) {
			logger.error("Catching Exception " + e.getMessage());
			throw new EntityFoundException(e.getMessage());
		}

	}

	private String findFieldType(FieldWrapper field) {
		String fieldType;
		switch (field.getFieldType()) {
		case "text":
			fieldType = "text";
			break;
		case "number":
			fieldType = "int";
			break;
		case "boolean":
			fieldType = "boolean";
			break;
		default:
			fieldType = "Invalid datatype";
			break;

		}

		return fieldType;

	}
}
