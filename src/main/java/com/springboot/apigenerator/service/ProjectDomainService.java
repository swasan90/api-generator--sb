package com.springboot.apigenerator.service;

import java.util.List;

import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.ProjectDomain;

public interface ProjectDomainService {

	boolean createProjectDomain(ProjectDomain projectDomain) throws EntityFoundException;

	List<ProjectDomain> getAllDomainsForproject(String projectNameO);

}
