package com.springboot.apigenerator.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	
	/**
	 * Function to handle create project and domain request.
	 * @param project
	 * @return
	 * @throws EntityFoundException
	 */
	@PostMapping(value = "/createProject", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> createProject(@Valid @RequestBody ProjectDomain project)
			throws EntityFoundException {
		if (projectService.createProjectDomain(project)) {
			logger.info("Successfully created project");
			return new ResponseEntity<ResponseMessage>(this.res.setMessage("Successfully created project", true),
					HttpStatus.CREATED);

		} else {
			logger.error("Unable to create user account");
			return new ResponseEntity<ResponseMessage>(
					this.res.setMessage("Project/domain already exists.Cannot create", false), HttpStatus.BAD_REQUEST);
		}

	}
	
	@GetMapping(value="/listDomains/{projectName}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> listDomains(@PathVariable String projectName) throws JsonProcessingException{
		List<ProjectDomain> result = projectService.getAllDomainsForproject(projectName);				 
		return new ResponseEntity<ResponseMessage>(this.res.setData(result, true),HttpStatus.OK);
	}

}
