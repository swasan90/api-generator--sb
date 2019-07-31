/**
 * 
 */
package com.springboot.apigenerator.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.springboot.apigenerator.model.SchemaMapping;

/**
 * @author swathy
 *
 */
@Repository
public interface SchemaRepository extends CassandraRepository<SchemaMapping, UUID>{
	
	List<SchemaMapping> findByProjectId(UUID id);
}
