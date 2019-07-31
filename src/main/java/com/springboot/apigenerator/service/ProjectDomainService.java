package com.springboot.apigenerator.service;

import java.util.List;
import java.util.Set;

import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.ProjectDomain;
import com.springboot.apigenerator.model.ProjectGroupBy;
import com.springboot.apigenerator.model.ServiceReponseMessage;

public interface ProjectDomainService {

	ServiceReponseMessage createProjectDomain(ProjectDomain projectDomain) throws EntityFoundException;

	List<ProjectDomain> getAllDomainsForproject(String projectName);
	
	Set<ProjectGroupBy> getAllProjects();

}
