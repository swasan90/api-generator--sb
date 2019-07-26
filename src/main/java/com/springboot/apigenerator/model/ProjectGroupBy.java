/**
 * 
 */
package com.springboot.apigenerator.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author swathy
 *
 */
@Data
public class ProjectGroupBy {
	
	private String name;
	
	private List<ProjectDomain> children;
	
	public ProjectGroupBy() {};
	
	public ProjectGroupBy(String projectName,List<ProjectDomain> children) {
		super();
		this.name = projectName;
		this.children = children;		 
	}

}
