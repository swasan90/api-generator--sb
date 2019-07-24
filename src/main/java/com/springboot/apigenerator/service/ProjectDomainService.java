package com.springboot.apigenerator.service;

import java.util.List;

import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.ProjectDomain;
import com.springboot.apigenerator.model.ServiceReponseMessage;

public interface ProjectDomainService {

	ServiceReponseMessage createProjectDomain(ProjectDomain projectDomain) throws EntityFoundException;

	List<ProjectDomain> getAllDomainsForproject(String projectNameO);

}
