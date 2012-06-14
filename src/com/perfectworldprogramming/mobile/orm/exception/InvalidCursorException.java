package com.perfectworldprogramming.mobile.orm.exception;

public class InvalidCursorException extends DataAccessException {
	
    private static final long serialVersionUID = 323422738996859681L;

    public InvalidCursorException(Class<? extends Object> clazz) {
		super("Invalid Cursor: "+ clazz.getName() + ". Possible reasons are 1) query does not contain all the fields, 2) query is using the wrong tables, 3) Not navigating through the Cursor correctly, either by the cursor is empty, or has past the end of the results.");
	}

}
