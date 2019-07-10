package com.springboot.apigenerator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.ProjectDomain;
import com.springboot.apigenerator.model.ResponseMessage;
import com.springboot.apigenerator.service.ProjectDomainService;

@RestController
public class ProjectDomainController {
	
	private Logger logger = LoggerFactory.getLogger(ProjectDomainController.class);
	
	@Autowired
	private ProjectDomainService projectService;
	
	private ResponseMessage res;

	public ProjectDomainController() {
		this.res = new ResponseMessage();
	}

	
	@PostMapping(value="/createProject",consumes="application/json",produces="application/json")
	public ResponseEntity<ResponseMessage> createProject(@RequestBody ProjectDomain project) throws EntityFoundException{
		System.out.println("printing projec values "+project.getProjectName()+" "+project.getDomainName());
		if(projectService.createProjectDomain(project)) {
			logger.info("Successfully created project");
			return new ResponseEntity<ResponseMessage>(this.res.setMessage("Successfully created project",true), HttpStatus.CREATED);
			
		}else {
			logger.error("Unable to create user account");
			return new ResponseEntity<ResponseMessage>(
					this.res.setMessage("Project/domain already exists.Cannot create",false),
					HttpStatus.BAD_REQUEST);
		}
		
	}

}
