package com.springboot.apigenerator.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class FieldWrapper {
	
	@NotBlank(message = "Field Name cannot be blank")
	private String fieldName;
	
	@NotBlank(message = "Field Type cannot be blank")
	private String fieldType; 

}
