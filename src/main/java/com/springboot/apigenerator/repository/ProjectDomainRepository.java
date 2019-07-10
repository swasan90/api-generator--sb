package com.springboot.apigenerator.repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.springboot.apigenerator.model.ProjectDomain;

@Repository
public interface ProjectDomainRepository extends CassandraRepository<ProjectDomain,UUID>{

}
