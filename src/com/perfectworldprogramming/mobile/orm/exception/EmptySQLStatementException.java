package com.perfectworldprogramming.mobile.orm.exception;

public class EmptySQLStatementException extends DataAccessException {

    private static final long serialVersionUID = 2355823153622503357L;

    public EmptySQLStatementException() {
		super("Invalid SQL. Please supply an SQL query string.");
	}
}
