package com.perfectworldprogramming.mobile.orm.exception;

public class InvalidQueryTypeException extends DataAccessException {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidQueryTypeException(String expectedType, String givenType) {
        super("Invalid Query type. Was expecting a " + expectedType + " but received a " + givenType);
    }

}
