/**
 * 
 */
package com.springboot.apigenerator.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * @author swathy
 *
 */
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@SpringBootTest
@ContextConfiguration  
public class ApiEndpointsControllerIntegrationTest {

	public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS testapigenerator "
			+ "WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };";

	public static final String KEYSPACE_ACTIVATE_QUERY = "USE testapigenerator;";

	public static final String DATA_TABLE_NAME = "book";

	@Autowired
	private WebApplicationContext wac;

	@BeforeAll
	public static void startCassandraEmbedded() throws ConfigurationException, TTransportException, IOException, InterruptedException
			  {
		EmbeddedCassandraServerHelper.startEmbeddedCassandra();
		Cluster cluster = Cluster.builder().withoutMetrics().withoutJMXReporting()
				.addContactPoints("127.0.0.1").withPort(9142).build();		 
		Session session = cluster.connect();
		session.execute(KEYSPACE_CREATION_QUERY);
		session.execute(KEYSPACE_ACTIVATE_QUERY);		 
	}

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

	}

	@AfterEach
	void tearDown() throws Exception {
		this.mockMvc = null;

	}

	@Test
	public void testgetApiEndpoints() throws Exception {
		String project_id = "be929010-aec4-11e9-a112-65c7562a5121";
//		UUID id = UUID.fromString(project_id);
//		List<Map<String,Object>> endpoints = new ArrayList<>();
//		
//		
//		List<ApiEndPoints> apiData = new ArrayList<>();
//		apiData.add(new ApiEndPoints(id,"DELETE","apigenerator/geoscience/employees/be929010-aec4-11e9-a112-65c7562a5121","employees.delete"));
//		apiData.add(new ApiEndPoints(id,"GET","apigenerator/geoscience/employees","employees.list"));
//		apiData.add(new ApiEndPoints(id,"POST","apigenerator/geoscience/employees","employees.post"));
//		apiData.add(new ApiEndPoints(id,"GET","apigenerator/geoscience/employees/be929010-aec4-11e9-a112-65c7562a5121","employees.show"));
//		apiData.add(new ApiEndPoints(id,"POST","apigenerator/geoscience/employees/be929010-aec4-11e9-a112-65c7562a5121","employees.update"));
//		
//		for(int i =0; i<5;i++) {
//			Map<String, Object> map = new LinkedHashMap<String, Object>();
//			map.put("project_id",apiData.get(i).getProjectId());
//			map.put("methodType",apiData.get(i).getMethodType());
//			map.put("endPointUrl", apiData.get(i).getEndPointUrl());
//			map.put("endPointName", apiData.get(i).getEndPointName());
//			endpoints.add(map);
//		}
		
		 
		
		this.mockMvc.perform(get("/getEndPoints/{project_id}", project_id)) 
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status").value(true))
		.andExpect(jsonPath("$.data[0].project_id",is(project_id)))
		.andExpect(jsonPath("$.data[0].endpoint_name",is("employees.delete")))
		.andExpect(jsonPath("$.data[0].endpoint_url",is("apigenerator/geoscience/employees/be929010-aec4-11e9-a112-65c7562a5121")))
		.andExpect(jsonPath("$.data[0].method_type",is("DELETE")))	 
		.andExpect(jsonPath("$.data[1].endpoint_name",is("employees.list")))
		.andReturn();
		
		 

	}

	@AfterAll
	public static void stopCassandraEmbedded() {
		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
	}

}
