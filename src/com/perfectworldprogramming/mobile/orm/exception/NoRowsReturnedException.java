package com.perfectworldprogramming.mobile.orm.exception;

public class NoRowsReturnedException extends DataAccessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoRowsReturnedException(String msg) {
        super("No rows returned for query " + msg);
    }
}
