/**
 * Class to implement create schema for the given domain.
 */
package com.springboot.apigenerator.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.exceptions.QueryValidationException;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.core.schemabuilder.SchemaStatement;
import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.FieldWrapper;
import com.springboot.apigenerator.model.ProjectDomain;
import com.springboot.apigenerator.model.SchemaGenerator;
import com.springboot.apigenerator.model.SchemaMapping;
import com.springboot.apigenerator.repository.ProjectDomainRepository;
import com.springboot.apigenerator.repository.SchemaRepository;

@Service("schemaBuilderService")
public class SchemaBuilderServiceImpl implements SchemaBuilderService {

	private Logger logger = LoggerFactory.getLogger(SchemaBuilderServiceImpl.class);

	@Autowired
	private ProjectDomainRepository projectDomainRepo;

	@Autowired
	private SchemaRepository schemaRepo;

	@Autowired
	private CassandraOperations cassandraTemplate;

	@Value("${cassandra.keyspace}")
	private String keySpaceName;

	/**
	 * Function to implement create schema method.
	 * 
	 * @return boolean
	 * @throws EntityFoundException
	 * 
	 */
	@Override
	@Transactional
	public boolean createSchemaForDomain(SchemaGenerator schemaBuilder) {
		Optional<ProjectDomain> project = projectDomainRepo.findById(schemaBuilder.getProjectId());
		try {
			if (project.isPresent()) {

				// Declaring Map to store fields and field data type.
				Map<String, DataType> fieldMap = new LinkedHashMap<>();

				logger.info("Constructing Query");

				// Constructing cql query to create table.
				Create createQuery = constructCqlQuery(project.get());

				// Implementation to persist record in schema mapping table and adding entries
				// to fieldMap.
				for (FieldWrapper field : schemaBuilder.getFields()) {
					
					//Writing entries on map.
					logger.info("Starting to write entries on map");
					fieldMap.put(field.getFieldName(), findFieldType(field));
					logger.info("Finished writing entries on map");
					
					 
					SchemaMapping schema = new SchemaMapping();
					//Setters
					schema.setProjectId(project.get().getId());
					schema.setColumnName(field.getFieldName());
					schema.setDataType(field.getFieldType());

					schemaRepo.save(schema);
					logger.info("Persisting schema mapping completed");
				}

				// Constructing query to add dynamic columns to existing create statement.
				Create cqlQuery = addDynamicColumns(createQuery, fieldMap);

				// Constructing index query to create index on cluster key.
				SchemaStatement indexQuery = createIndexOnClusterKey("id", project.get().getDomainName());

				// Executing query statements via cassandra operations.
				cassandraTemplate.getCqlOperations().execute(cqlQuery.toString());
				return cassandraTemplate.getCqlOperations().execute(indexQuery);

			}
		} catch (QueryValidationException e) {
			throw new RuntimeException(e.getMessage());
		}

		return false;

	}

	/**
	 * Function to return the cassandra data type for the given field type.
	 * 
	 * @param field
	 * @return fieldType
	 */
	private DataType findFieldType(FieldWrapper field) {
		DataType fieldType = null;
		switch (field.getFieldType().toLowerCase()) {
		case "text":
			fieldType = DataType.text();
			break;
		case "number":
			fieldType = DataType.cint();
			break;
		case "boolean":
			fieldType = DataType.cboolean();
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
	private Create constructCqlQuery(ProjectDomain project) {
		Create create = SchemaBuilder.createTable(keySpaceName, project.getDomainName().toLowerCase())
				.addPartitionKey("project_id", DataType.uuid()).addClusteringColumn("id", DataType.uuid())
				.ifNotExists();
		logger.info("Construction of create statement completed");
		return create;
	}

	/**
	 * Function to construct query to add dynamic columns.
	 * 
	 * @param query
	 * @param map
	 * @return
	 */
	private Create addDynamicColumns(Create query, Map<String, DataType> map) {
		logger.info("Starting construction of dynamic column query");
		for (Entry<String, DataType> entry : map.entrySet()) {
			query = query.addColumn(entry.getKey(), entry.getValue()).ifNotExists();
		}
		logger.info("Completed construction of dynamic column query");
		return query;
	}

	/**
	 * Function to create index query to create index on cluster key.
	 * 
	 * @param field
	 * @param tableName
	 * @return
	 */
	private SchemaStatement createIndexOnClusterKey(String field, String tableName) {
		logger.info("Starting construction of create index query");
		SchemaStatement indexQuery = SchemaBuilder.createIndex(tableName+"_"+"id_idx").onTable(keySpaceName, tableName)
				.andColumn(field);
		logger.info("Completed construction of create index query");
		return indexQuery;
	}

}
