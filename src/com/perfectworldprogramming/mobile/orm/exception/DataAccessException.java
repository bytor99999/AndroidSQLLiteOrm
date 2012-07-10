package com.perfectworldprogramming.mobile.orm.exception;

/**
 * Base class for packaged exceptions.
 * User: Mark Spritzler
 * Date: 4/6/11
 * Time: 11:26 AM
 */
public class DataAccessException extends RuntimeException {

    private static final long serialVersionUID = 4199875431810347897L;

    public DataAccessException(String msg) {
        super(msg);
    }

    public DataAccessException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

}
