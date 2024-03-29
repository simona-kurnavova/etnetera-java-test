package com.etnetera.hr.rest;

import java.util.List;

/**
 * 
 * Envelope for the validation errors. Represents JSON response.
 * 
 * @author Etnetera
 *
 */
public class Errors {

	private List<ValidationError> errors;

	public Errors() {

	}

	public Errors(List<ValidationError> errors) {
		setErrors(errors);
	}

	public List<ValidationError> getErrors() {
		return errors;
	}

	public void setErrors(List<ValidationError> errors) {
		this.errors = errors;
	}

}
