package com.springboot.apigenerator.model;

import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;

@Table(value="api_endpoints")
@Data
public class ApiEndPoints {

	@PrimaryKeyColumn(name = "project_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID projectId;
	
	@Column(value="method_type")
	private String methodType;
	
	@Column(value="endpoint_url")
	private String endPointUrl;
	
	 
	@PrimaryKeyColumn(value="endpoint_name",ordinal = 1, type = PrimaryKeyType.CLUSTERED)	
	private String endPointName;

	public ApiEndPoints() {
	};

	public ApiEndPoints(UUID project_id, String methodType, String endPointUrl, String endPointName) {
		super();
		this.projectId = project_id;
		this.methodType = methodType;
		this.endPointName = endPointName;
		this.endPointUrl = endPointUrl;

	}

}
