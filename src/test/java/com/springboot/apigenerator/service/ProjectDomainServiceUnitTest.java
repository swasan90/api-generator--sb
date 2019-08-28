/**
 * Class to unit test ProjectDomain Service class methods.
 */
package com.springboot.apigenerator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.ProjectDomain;
import com.springboot.apigenerator.model.ProjectGroupBy;
import com.springboot.apigenerator.model.ServiceReponseMessage;
import com.springboot.apigenerator.repository.ApiEndPointsRepository;
import com.springboot.apigenerator.repository.ProjectDomainRepository;

/**
 * @author swathy
 *
 */
@ExtendWith(MockitoExtension.class) 
public class ProjectDomainServiceUnitTest {
	
	@Mock
	private ProjectDomainRepository projectRepo;
	
	@Mock
	private ApiEndPointsRepository apiRepo;
	
	@InjectMocks
	private ProjectDomainService projectDomainService = new ProjectDomainServiceImpl();
	
	private String projectName;
	private String domainName;
	private ProjectDomain project; 
	
	List<ProjectDomain> projects = new ArrayList<>();
	
	@BeforeEach
	void setUp() throws Exception{
		this.projectName = "geoscience";
		this.domainName = "employees";
		this.project = new ProjectDomain(this.projectName,this.domainName);	
		
		
		ProjectDomain p1 = new ProjectDomain("geoscience","employees");
		ProjectDomain p2 = new ProjectDomain("geoscience","field_density_report");	 
		
		this.projects.add(p1);
		this.projects.add(p2);
		 
	}
	
	@AfterEach
	void tearDown() throws Exception{
		this.projectName = null;
		this.domainName = null;
		this.project =null;	
		this.projects = null;
	}
	
	/**
	 * Function to test create project domain entity return success.
	 * @throws EntityFoundException
	 */
	@Test
	public void testcreateProjectDomain() throws EntityFoundException {
		Mockito.when(projectRepo.findByProjectNameAndDomainName(this.projectName,this.domainName)).thenReturn(null);		 
		ServiceReponseMessage expected = projectDomainService.createProjectDomain(this.project);
		assertEquals(expected.getStatus(),true);
	}
	
	/**
	 * Function to test create project domain entity fails.
	 * @throws EntityFoundException
	 */
	@Test
	public void testcreateProjectDomainFails() throws EntityFoundException {
		Mockito.when(projectRepo.findByProjectNameAndDomainName(this.projectName,this.domainName)).thenReturn(this.project);		 
		ServiceReponseMessage expected = projectDomainService.createProjectDomain(this.project);
		assertEquals(expected.getStatus(),false);
		assertEquals(expected.getResponseObj(),null);		
	}
	 
	/**
	 * Function to test get all domains for project.
	 */
	@Test
	public void testgetAllDomainsForProject() {	 
		Mockito.when(projectRepo.findByProjectName(this.projectName)).thenReturn(this.projects);
		List<ProjectDomain> expected = projectDomainService.getAllDomainsForproject(this.projectName);
		assertEquals(expected.get(0),projects.get(0));	 
		assertEquals(expected.size(),projects.size());
	}
	
	/**
	 * Function to test get all projects grouping by project name.
	 */
	@Test
	public void testgetAllProjects() {
		List<ProjectDomain> p1 = new ArrayList<>();
		 
		p1.add(this.projects.get(0));
		p1.add(this.projects.get(1));
 
		
		Mockito.when(projectRepo.findAll()).thenReturn(projects);
		Mockito.when(projectRepo.findByProjectName(this.projects.get(0).getProjectName())).thenReturn(p1);	 
		Set<ProjectGroupBy> expected = projectDomainService.getAllProjects();
		assertEquals(expected.size(),projects.size()-1);			 
		assertEquals(expected.iterator().next().getName(),"geoscience");
		assertEquals(expected.iterator().next().getChildren().get(0).getProjectName(),"geoscience");
		assertEquals(expected.iterator().next().getChildren().size(),p1.size());
	}
}
