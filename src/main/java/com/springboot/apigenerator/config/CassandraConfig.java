package com.springboot.apigenerator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration{

	@Value("${cassandra.keyspace}") private String keySpace;
	@Value("${cassandra.port}") private int port;
	@Value("${cassandra.contactpoints}") private String contactPoints;
	@Override
	protected String getKeyspaceName() {		 
		return keySpace;
	}
	
	@Bean
	public CassandraClusterFactoryBean cluster() {
		CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
		cluster.setContactPoints(contactPoints);
		cluster.setPort(9142);
		return cluster;
	}
	
	@Bean
	public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
		CassandraMappingContext mappingContext= new CassandraMappingContext();
	    mappingContext.setInitialEntitySet(getInitialEntitySet());
	    return mappingContext;
	}

}
