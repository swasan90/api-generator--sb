package com.springboot.apigenerator.service;

import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.ProjectDomain;

public interface ProjectDomainService {
	
	boolean createProjectDomain(ProjectDomain projectDomain) throws EntityFoundException;

}
