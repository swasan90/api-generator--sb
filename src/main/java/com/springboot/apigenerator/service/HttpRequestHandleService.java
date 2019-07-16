package com.springboot.apigenerator.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.springboot.apigenerator.model.RequestPayload;

public interface HttpRequestHandleService  {
	
	<T> List<Map<String, Object>> listAll(String project_name,String domain_name);
	
	boolean insertRecord(RequestPayload payload,String projectName,String domainName) throws Exception;
	
	List<Map<String,Object>> getRecordByID(String project_name,String domain_name,UUID id);
	
	boolean updateRecord(RequestPayload payload,String projectName,String domainName,UUID domainId);

}
