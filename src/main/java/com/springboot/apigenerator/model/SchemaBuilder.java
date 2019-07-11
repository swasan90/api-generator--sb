/**
 * 
 */
package com.springboot.apigenerator.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import com.datastax.driver.core.utils.UUIDs;

import lombok.Data;

/**
 * @author swathy
 *
 */
@PrimaryKeyClass
@Data
public class SchemaBuilder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PrimaryKeyColumn(name="project_id",ordinal=0,type = PrimaryKeyType.PARTITIONED)
	private UUID projectId;
	
	@PrimaryKeyColumn(ordinal=0,type=PrimaryKeyType.CLUSTERED)
	private UUID id;	
	
	 
	private List<FieldWrapper> fields;
	
	public SchemaBuilder(List<FieldWrapper> fields,UUID projectId) {
		super();
		this.fields = fields;		
		this.projectId = projectId;
		this.id = UUIDs.timeBased();
	}
	
	@Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
	    return result;
	  }

	  @Override
	  public boolean equals(Object obj) {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (getClass() != obj.getClass())
	      return false;
	    SchemaBuilder other = (SchemaBuilder) obj;
	    if (id == null) {
	      if (other.id != null)
	        return false;
	    } else if (!id.equals(other.id))
	      return false;
	    if (projectId == null) {
	      if (other.projectId != null)
	        return false;
	    } else if (!projectId.equals(other.projectId))
	      return false;
	    return true;
	  }
	
}
