package com.perfectworldprogramming.mobile.orm.exception;

public class EmptySQLStatementException extends DataAccessException {

	private static final long serialVersionUID = 1L;
	
	public EmptySQLStatementException() {
		super("Invalid SQL. Please supply an SQL query string.");
	}
}
