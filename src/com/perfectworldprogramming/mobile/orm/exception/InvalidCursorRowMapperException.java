package com.perfectworldprogramming.mobile.orm.exception;

public class InvalidCursorRowMapperException extends InvalidCursorException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidCursorRowMapperException(Class<? extends Object> class1) {
	    super(class1);
		//super("Invalid CursorRowMapper: " + class1.getName() + ". Possible reasons are 1) query does not contain all the fields, 2) query is using the wrong tables, 3) Not navigating through the Cursor correctly, either by the cursor is empty, or has past the end of the results.");
	}
}