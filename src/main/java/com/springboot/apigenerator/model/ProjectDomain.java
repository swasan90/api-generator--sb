/**
 * Class to define project domain model
 */
package com.springboot.apigenerator.model;

 

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.utils.UUIDs;

import lombok.Data;

/**
 * @author swathy
 *
 */
@Table
@Data
public class ProjectDomain {
	
	@PrimaryKey	 
	private UUID id;
	
//	@PrimaryKeyColumn(name="project_name",ordinal=0,type=PrimaryKeyType.PARTITIONED)
	@NotBlank(message="Project name cannot be empty")
	private String projectName;	
	
//	@PrimaryKeyColumn(name="domain_name",ordinal=0,type=PrimaryKeyType.CLUSTERED)
	@NotBlank(message="Domain name cannot be empty")
	private String domainName;
	
	public ProjectDomain() {};
	
	public ProjectDomain(String project_name,String domain_name) {
		super();		 
		this.projectName = project_name;
		this.domainName = domain_name;
		this.id = UUIDs.timeBased();	
	};
}
