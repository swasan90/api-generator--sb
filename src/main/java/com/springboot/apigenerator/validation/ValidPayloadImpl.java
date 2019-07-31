/**
 * 
 */
package com.springboot.apigenerator.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springboot.apigenerator.model.ProjectDomain;
import com.springboot.apigenerator.model.RequestPayload;
import com.springboot.apigenerator.model.SchemaMapping;
import com.springboot.apigenerator.repository.ProjectDomainRepository;
import com.springboot.apigenerator.repository.SchemaRepository;

/**
 * @author swathy
 *
 */
@Service("validPayload")
public class ValidPayloadImpl {

	@Autowired
	private SchemaRepository schemaRepo;

	@Autowired
	private ProjectDomainRepository projectRepo;

	public boolean isValid(RequestPayload payload, String projectName, String domainName) {

		int flag = 0;
		Map<String, Object> attributes = payload.getAttributes();
		ProjectDomain project = projectRepo.findByProjectNameAndDomainName(projectName, domainName);
		List<SchemaMapping> schema = schemaRepo.findByProjectId(project.getId());
		for (Entry<String, Object> entry : attributes.entrySet()) {
			for (SchemaMapping item : schema) {
				int status = 0;				 
				if (item.getColumnName().trim().contains(entry.getKey())) {					 
					status = 1;
					if (status == 1 && item.getDataType().compareTo("number") == 0) {
						attributes.put(item.getColumnName(),
								Integer.parseInt((String) attributes.get(item.getColumnName())));
					}					
					flag = status;
				} else {
					flag = 0;
				}
			}
		}		 
		return flag == 1 ? true : false;
	}

}
