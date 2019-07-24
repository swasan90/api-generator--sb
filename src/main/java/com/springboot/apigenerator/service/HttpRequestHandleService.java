package com.springboot.apigenerator.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.springboot.apigenerator.exceptions.EntityFoundException;
import com.springboot.apigenerator.model.RequestPayload;

public interface HttpRequestHandleService  {
	
	//List all records.
	List<Map<String, Object>> listAll(String project_name,String domain_name);
	
	//Persist record.
	boolean insertRecord(RequestPayload payload,String projectName,String domainName) throws Exception;
	
	//Get a record.
	List<Map<String,Object>> getRecordByID(String project_name,String domain_name,UUID id);
	
	//Update record.
	boolean updateRecord(RequestPayload payload,String projectName,String domainName,UUID domainId) throws EntityFoundException;
	
	//Delete a record.
	boolean deleteRecord(String projectName,String domainName,UUID domainId) throws EntityFoundException;
	

}
