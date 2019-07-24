/**
 * 
 */
package com.springboot.apigenerator.model;

import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;

/**
 * @author swathy
 *
 */
@Table(value="schema_mapping")
@Data
public class SchemaMapping {

	@PrimaryKeyColumn(name="project_id",ordinal =0,type=PrimaryKeyType.PARTITIONED)	 
	private UUID projectId;
	
	@PrimaryKeyColumn(name="column_name",ordinal =1,type=PrimaryKeyType.CLUSTERED)		 
	private String columnName;
	
	@Column(value = "data_type")
	private String dataType;
	
	public SchemaMapping() {};
	
	public SchemaMapping(UUID project_id,String colName,String colType) {
		super();
		this.projectId = project_id;
		this.columnName = colName;
		this.dataType = colType;
	}
}
