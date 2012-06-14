package com.perfectworldprogramming.mobile.orm.exception;

public class NoRowsReturnedException extends DataAccessException {

    private static final long serialVersionUID = -5416130249258941267L;

    public NoRowsReturnedException(String msg) {
        super("No rows returned for query " + msg);
    }
}
