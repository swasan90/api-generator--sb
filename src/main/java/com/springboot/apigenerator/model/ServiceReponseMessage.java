/**
 * 
 */
package com.springboot.apigenerator.model;

/**
 * @author swathy
 *
 */
public class ServiceReponseMessage {
	
	private Object responseObj;
	
	private boolean status;

	public Object getResponseObj() {
		return responseObj;
	}

	public ServiceReponseMessage setResponseObj(Object responseObj,boolean status) {
		this.responseObj = responseObj;
		this.status = status;
		return this;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
