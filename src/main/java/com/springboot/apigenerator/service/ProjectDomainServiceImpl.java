package com.springboot.apigenerator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.ProjectDomain;
import com.springboot.apigenerator.repository.ProjectDomainRepository;

@Service("projectService")
public class ProjectDomainServiceImpl implements ProjectDomainService {
	
	private Logger logger = LoggerFactory.getLogger(ProjectDomainServiceImpl.class);
	
	@Autowired
	private ProjectDomainRepository projectRepo;
	
	@Override
	public boolean createProjectDomain(ProjectDomain projectDomain) throws EntityFoundException {
		ProjectDomain project = projectRepo.findByProjectNameAndDomainName(projectDomain.getProjectName(), projectDomain.getDomainName());
		try {
			if(project ==null) {
				ProjectDomain proj = new ProjectDomain(projectDomain.getProjectName(),projectDomain.getDomainName());
				projectRepo.save(proj);
				logger.info("Created Project and domain");
				return true;
			}else {
				logger.error("Unable to create project and domain with the given name");
				return false;
			}
		} catch (DataIntegrityViolationException e) {
			logger.error("Catching Exception " + e.getMessage());
			throw new EntityFoundException(e.getMessage());
		}
		 
	}

}
