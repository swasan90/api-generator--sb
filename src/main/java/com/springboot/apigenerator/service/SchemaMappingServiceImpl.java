/**
 * 
 */
package com.springboot.apigenerator.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.springboot.apigenerator.model.FieldWrapper;

/**
 * @author swathy
 *
 */
@Service("schemaService")
public class SchemaMappingServiceImpl implements SchemaMappingService {

	@Autowired
	private CassandraOperations cassandraTemplate;

	@Override
	public Set<String> getSchemaColumns(UUID id) {
		Set<String> columns = new HashSet<>();
		Where where = QueryBuilder.select("column_name").from("schema_mapping")
				.where(QueryBuilder.eq("project_id", id));
		List<Map<String, Object>> output = cassandraTemplate.getCqlOperations().queryForList(where);
		for (Map<String, Object> map : output) {
			for (Object entry : map.values()) {
				columns.add(entry.toString());
			}
		}
		return columns;
	}

	@Override
	public List<FieldWrapper> getSchemaColumnsWithType(UUID id) {
		Where where = QueryBuilder.select().from("schema_mapping").where(QueryBuilder.eq("project_id", id));
		List<Map<String, Object>> output = cassandraTemplate.getCqlOperations().queryForList(where);
		List<FieldWrapper> schema = new ArrayList<>();
		for (Map<String, Object> map : output) {
			FieldWrapper fieldObj = new FieldWrapper();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getKey().compareTo("column_name") == 0) {
					fieldObj.setFieldName(entry.getValue().toString());
				}
				if (entry.getKey().compareTo("data_type") == 0) {
					fieldObj.setFieldType(entry.getValue().toString());
				}
			}
			schema.add(fieldObj);
		}

		return schema;
	}

}
