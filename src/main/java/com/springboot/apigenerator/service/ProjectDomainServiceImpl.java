package com.springboot.apigenerator.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.ApiEndPoints;
import com.springboot.apigenerator.model.ProjectDomain;
import com.springboot.apigenerator.model.ProjectGroupBy;
import com.springboot.apigenerator.model.ServiceReponseMessage;
import com.springboot.apigenerator.repository.ApiEndPointsRepository;
import com.springboot.apigenerator.repository.ProjectDomainRepository;

@Service("projectService")
public class ProjectDomainServiceImpl implements ProjectDomainService {

	private Logger logger = LoggerFactory.getLogger(ProjectDomainServiceImpl.class);

	@Autowired
	private ProjectDomainRepository projectRepo;
	
	@Autowired
	private ApiEndPointsRepository apiRepo;

	@Override
	@Transactional
	public ServiceReponseMessage createProjectDomain(ProjectDomain projectDomain) throws EntityFoundException {
		ProjectDomain project = projectRepo.findByProjectNameAndDomainName(projectDomain.getProjectName(),
				projectDomain.getDomainName());
		ServiceReponseMessage res = new ServiceReponseMessage();
		try {
			if (project == null) {
				ProjectDomain proj = new ProjectDomain(projectDomain.getProjectName(), projectDomain.getDomainName());
				ProjectDomain data = projectRepo.save(proj);
				logger.info("Created Project and domain");
				
				constructEndPointsMap(proj);
				return res.setResponseObj(data, true);
				
			} else {
				logger.error("Unable to create project and domain with the given name");
				return res.setResponseObj(null, false);
			}
		} catch (DataIntegrityViolationException e) {
			logger.error("Catching Exception " + e.getMessage());
			throw new EntityFoundException(e.getMessage());
		}

	}

	/**
	 * Function to construct endpoints in multivaluedmap.
	 * 
	 * @param proj
	 */
	private void constructEndPointsMap(ProjectDomain proj) {
		logger.info("creating map for http methods and endpoints");
		MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
		map.put("GET", "apigenerator/" + proj.getProjectName() + "/" + proj.getDomainName());
		map.put("GET", "apigenerator/" + proj.getProjectName() + "/" + proj.getDomainName() + "/" + proj.getId());
		map.put("POST", "apigenerator/" + proj.getProjectName() + "/" + proj.getDomainName());
		map.put("POST", "apigenerator/" + proj.getProjectName() + "/" + proj.getDomainName() + "/" + proj.getId());
		map.put("DELETE", "apigenerator/" + proj.getProjectName() + "/" + proj.getDomainName() + "/" + proj.getId());
		logger.info("Created map. Rerouting call to save entity");
		processRecord(map, proj);
	}

	/**
	 * Function to create record in project domain table.
	 * 
	 * @param map
	 * @param proj
	 */
	private void processRecord(MultiValuedMap<String, String> map, ProjectDomain proj) {
		logger.info("Entering map iteration..Persisting data about to start");
		for (Map.Entry<String, String> entry : map.entries()) {
			//Creating new object for every loop.
			ApiEndPoints endPoints = new ApiEndPoints();
			
			//Setters
			endPoints.setProjectId(proj.getId());			
			endPoints.setMethodType(entry.getKey());
			endPoints.setEndPointUrl(entry.getValue());		
			endPoints.setEndPointName(getEndPointName(entry,proj));
			
			//Persisting data
			apiRepo.save(endPoints);
		}
		logger.info("Persisting data completed..");
	}
	
	/**
	 * Function to get endpoint name based on the method type and endpoint.
	 * @param entry
	 * @param proj
	 * @return
	 */
	private String getEndPointName(Map.Entry<String, String> entry, ProjectDomain proj) {
		switch (entry.getKey()) {
		case "DELETE":
			return proj.getDomainName().concat(".delete");
		case "GET":
			if (entry.getValue().contains("-")) {
				return proj.getDomainName().concat(".show");
			}
			return proj.getDomainName().concat(".list");
		case "POST":
			if (entry.getValue().contains("-")) {
				return proj.getDomainName().concat(".update");
			}
			return proj.getDomainName().concat(".post");
		default:
			return "Invalid method type";
		}
	}
	
	/**
	 * Function to get all domains for project
	 * @param projName
	 * @return List
	 */
	@Override
	public List<ProjectDomain> getAllDomainsForproject(String projName) {
		return projectRepo.findByProjectName(projName);		 
	}
	
	/**
	 * Function to list all projects and group by project name
	 * @return Map
	 */
	@Override
	public Set<ProjectGroupBy> getAllProjects() {
		//Map<String, List<ProjectDomain>> result = projectRepo.findAll().stream().collect(Collectors.groupingBy(ProjectDomain::getProjectName));
		Set<ProjectGroupBy> projects = new HashSet<ProjectGroupBy>();		
		for(ProjectDomain project: projectRepo.findAll()) {	
			ProjectGroupBy projObj = new ProjectGroupBy(project.getProjectName(),this.getAllDomainsForproject(project.getProjectName()));			
			projects.add(projObj);
		}
		 
		return projects;
	}

}
