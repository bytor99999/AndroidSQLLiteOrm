package com.perfectworldprogramming.mobile.orm.exception;

public class NoPrimaryKeyFieldException  extends DataAccessException {

    private static final long serialVersionUID = -442347181326281845L;

    public NoPrimaryKeyFieldException(Class<? extends Object> clazz) {
        super("No primary key field found in class " + clazz.getSimpleName());
    }
}