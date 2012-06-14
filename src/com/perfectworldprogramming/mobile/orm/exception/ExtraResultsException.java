package com.perfectworldprogramming.mobile.orm.exception;

/**
 * User: Mark Spritzler
 * Date: 4/6/11
 * Time: 11:27 AM
 */
public class ExtraResultsException extends DataAccessException {

    private static final long serialVersionUID = 4713562069583405202L;

    public ExtraResultsException(int numberOfResults) {
        super("Expected one row returned by query but received " + numberOfResults);
    }
}
