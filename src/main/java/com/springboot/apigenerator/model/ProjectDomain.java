/**
 * Class to define project domain model
 */
package com.springboot.apigenerator.model;

 

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.utils.UUIDs;

import lombok.Data;

/**
 * @author swathy
 *
 */
@Table(value="project_domain")
@Data
public class ProjectDomain {
	
	@PrimaryKey	 
	private UUID id;
	
	@Column(value="project_name")
	@NotBlank(message="Project name cannot be empty")
	private String projectName;	
	
	@Column(value="domain_name")
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
