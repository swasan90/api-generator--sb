package com.springboot.apigenerator.config;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages="com.springboot.apigenerator.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

	private static final Log LOGGER = LogFactory.getLog(CassandraConfig.class);

	@Value("${cassandra.keyspace}")
	private String keySpace;
	@Value("${cassandra.port}")
	private int port;
	@Value("${cassandra.contactpoints}")
	private String contactPoints;
	
	@Override
	protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
		   CreateKeyspaceSpecification specification = CreateKeyspaceSpecification
	                .createKeyspace(getKeyspaceName()).ifNotExists()
	                .with(KeyspaceOption.DURABLE_WRITES, true).withSimpleReplication();
	        return Arrays.asList(specification);		 
	} 

	@Override
	protected String getKeyspaceName() {
		return keySpace;
	}

	@Bean
	public CassandraClusterFactoryBean cluster() {
		CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
		cluster.setContactPoints(getContactPoints());
		cluster.setPort(getPort());	
		cluster.setClusterName("apiGeneratorCluster");
		cluster.setKeyspaceCreations(getKeyspaceCreations());
		LOGGER.info("Cluster created with contact points [" + contactPoints + "] " + "& port [" + port + "].");
		return cluster;
	}

	@Override
	protected String getContactPoints() {
		return contactPoints;
	}

	@Override
	protected int getPort() {
		return port;
	}

	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.CREATE_IF_NOT_EXISTS;
		//return SchemaAction.RECREATE_DROP_UNUSED;
	}
	
	@Bean
	public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
		CassandraMappingContext mappingContext = new CassandraMappingContext();
		mappingContext.setInitialEntitySet(getInitialEntitySet());
		return mappingContext;
	}
	
	 /*
     * Get the entity package (where the entity class has the @Table annotation)
     */
    @Override
    public String[] getEntityBasePackages() {
        return new String[] { "com.springboot.apigenerator.model" };
    } 
}
