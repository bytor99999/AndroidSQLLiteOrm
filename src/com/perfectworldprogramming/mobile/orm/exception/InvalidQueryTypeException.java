package com.perfectworldprogramming.mobile.orm.exception;

public class InvalidQueryTypeException extends DataAccessException {

    private static final long serialVersionUID = 5051391018559950899L;

    public InvalidQueryTypeException(String expectedType, String givenType) {
        super("Invalid Query type. Was expecting a " + expectedType + " but received a " + givenType);
    }

}
