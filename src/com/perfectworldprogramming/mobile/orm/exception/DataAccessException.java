package com.perfectworldprogramming.mobile.orm.exception;

/**
 * User: Mark Spritzler
 * Date: 4/6/11
 * Time: 11:26 AM
 */
public class DataAccessException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataAccessException(String msg) {
        super(msg);
    }
}
