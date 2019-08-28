package com.springboot.apigenerator.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import com.datastax.driver.core.utils.UUIDs;

import lombok.Data;

@Data
public class RequestPayload {

	@PrimaryKeyColumn(name = "project_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID projectId;

	@PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.CLUSTERED)
	private UUID id;

	public Map<String, Object> attributes = new LinkedHashMap<>();

	public RequestPayload() {
	};

	public RequestPayload(UUID project_id, UUID id, Map<String, Object> fields) {
		this.attributes = fields;
		this.id = UUIDs.timeBased();
		this.projectId = project_id;
	}

	public RequestPayload(Map<String, Object> fields) {
		this.attributes = fields;
	}

	public List<String> getFieldNames() {
		List<String> fields = new ArrayList<>();
		fields.add("project_id");
		fields.add("id");
		for (String entry : attributes.keySet()) {
			fields.add(entry);
		}

		return fields;
	}

	public List<Object> getFieldValues() {
		List<Object> data = new ArrayList<>();
		data.add(this.getProjectId());
		data.add(UUIDs.timeBased());
		for (Object entryVal : attributes.values()) {
			data.add(entryVal);
		}
		return data;

	}

}
