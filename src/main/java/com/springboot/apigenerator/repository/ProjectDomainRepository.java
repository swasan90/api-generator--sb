package com.springboot.apigenerator.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.springboot.apigenerator.model.ProjectDomain;

@Repository
public interface ProjectDomainRepository extends CassandraRepository<ProjectDomain,UUID>{
	
	@AllowFiltering
	ProjectDomain findByProjectNameAndDomainName(String projectName,String domainName);
	
	@AllowFiltering
	List<ProjectDomain> findByProjectName(String projName);
}
