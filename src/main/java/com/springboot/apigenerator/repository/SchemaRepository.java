/**
 * 
 */
package com.springboot.apigenerator.repository;

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

}
