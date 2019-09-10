package com.springboot.apigenerator.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springboot.apigenerator.model.ProjectDomain;
import com.springboot.apigenerator.model.RequestPayload;
import com.springboot.apigenerator.model.SchemaMapping;
import com.springboot.apigenerator.repository.ProjectDomainRepository;
import com.springboot.apigenerator.repository.SchemaRepository;

@ExtendWith(MockitoExtension.class) 
public class ValidPayloadImplUnitTest {
	
	@Mock
	private SchemaRepository schemaRepo;
	
	@Mock
	private ProjectDomainRepository projectRepo;
	
	@InjectMocks
	private ValidPayloadImpl valiPayloadImpl;
	
	private String projectName;
	private String domainName;	 
	private RequestPayload payload;
	private ProjectDomain project; 
	private List<SchemaMapping> schemas = new ArrayList<>();
	
	
	@BeforeEach
	void setUp() throws Exception{
		this.projectName = "geoscience";
		this.domainName = "employees";
		this.project = new ProjectDomain(this.projectName,this.domainName);	
		//System.out.println(this.project.getId());
		
		//Creating payload
		Map<String,Object> fields = new HashMap<>();
		fields.put("firstname", "Jim");
		fields.put("lastname", "Halpert");
		fields.put("is_active", "true");
		this.payload = new RequestPayload(fields);
		
		//creating schemamapping list
		this.schemas.add(new SchemaMapping(this.project.getId(),"firstname","text"));
		this.schemas.add(new SchemaMapping(this.project.getId(),"lastname","text"));
		this.schemas.add(new SchemaMapping(this.project.getId(),"is_active","boolean"));
		
		//System.out.println(this.payload);
	}
	
	@AfterEach
	void tearDown() throws Exception{
		this.projectName = null;
		this.domainName = null;		
		this.payload = null;
		this.project = null; 
		this.schemas = null;
		 
	}
	
	/**
	 * Function to test if the request payload is valid.
	 */
	@Test
	public void testisValid() {
		Mockito.when(projectRepo.findByProjectNameAndDomainName(this.projectName, this.domainName )).thenReturn(this.project);	 
		Mockito.when(schemaRepo.findByProjectId(this.project.getId())).thenReturn(this.schemas);
		boolean expected = valiPayloadImpl.isValid(this.payload, this.projectName, this.domainName);		 
		assertEquals(expected,true);
		
	}
	
	/**
	 * Function to test returns false when request payload is invalid.
	 */
	@Test
	public void testisValidReturnsFalse() {
		Mockito.when(projectRepo.findByProjectNameAndDomainName(this.projectName, this.domainName )).thenReturn(this.project);	 
		Mockito.when(schemaRepo.findByProjectId(this.project.getId())).thenReturn(new ArrayList<>());
		boolean expected = valiPayloadImpl.isValid(this.payload, this.projectName, this.domainName);		 
		assertEquals(expected,false);
		
	}

}
