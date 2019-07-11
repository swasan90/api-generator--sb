/**
 * Class to implement create schema for the given domain.
 */
package com.springboot.apigenerator.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

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

	/**
	 * Function to implement create schema method.
	 * @return boolean
	 *   
	 */
	@Override
	public boolean createSchemaForDomain(SchemaBuilder schema) {
		Optional<ProjectDomain> project = projectDomainRepo.findById(schema.getProjectId());
		if (project.isPresent()) {

			logger.info("Constructing Query");
			StringBuilder fields = new StringBuilder();

			// Constructing field name and field type as single string using string builder.
			for (FieldWrapper field : schema.getFields()) {
				fields.append(field.getFieldName()).append(" ").append(findFieldType(field)).append(",");
			}

			// Constructing cql query to create table.
			StringBuilder cqlQuery = constructCqlQuery(project.get(), fields);

			// Creating table using cassandra template.
			cassandraTemplate.getCqlOperations().execute(cqlQuery.toString());

			logger.info("created table");
			return true;
		} else {
			logger.error("No project object exists with the project id " + project.get().getId());
			return false;
		}

	}

	/**
	 * Function to return the cassandra data type for the given field type.
	 * 
	 * @param field
	 * @return fieldType
	 */
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

	/**
	 * Function to construct cql query to create table.
	 * 
	 * @param project
	 * @param fields
	 * @return cqlQuery
	 */
	private StringBuilder constructCqlQuery(ProjectDomain project, StringBuilder fields) {
		StringBuilder cqlQuery = new StringBuilder();
		cqlQuery.append("CREATE TABLE ").append(project.getDomainName().toLowerCase())
				.append("(project_id UUID, id UUID,").append(fields).append("PRIMARY KEY(project_id,id))");
		return cqlQuery;
	}
}
