
/**
 * Class to define the response message
 */
package com.springboot.apigenerator.model;

import java.util.List;

/**
 * @author swathy
 * @param <T>
 *
 */

public class ResponseMessage {

	private String message;

	private boolean status;
	
	private List<?> data;

	public ResponseMessage() {
	}

	public ResponseMessage setMessage(String message, boolean status) {
		this.message = message;
		this.status = status;
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

}
