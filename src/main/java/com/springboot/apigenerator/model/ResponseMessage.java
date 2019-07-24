
/**
 * Class to define the response message
 */
package com.springboot.apigenerator.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author swathy
 * @param <T>
 *
 */
@JsonInclude(Include.NON_NULL)
public class ResponseMessage {

	private String message;

	private boolean status;
	
	private List<?> data;
	
	private Object resObj;

	public ResponseMessage() {
	}

	public ResponseMessage setMessage(String message, boolean status,Object resObj) {
		this.message = message;
		this.status = status;		
		this.resObj = resObj;
		return this;
	}

	public ResponseMessage setStatus(boolean status) {
		this.status = status;
		return this;
	}
	
	public ResponseMessage setData(List<?> data,boolean status) {		 
		this.data = data;
		this.status = status;
		return this;
	}
	
	public List<?> getData() {
		return data;
	}

	public boolean getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
	
	public Object getResObj() {
		return resObj;
	}
	
	 

}
