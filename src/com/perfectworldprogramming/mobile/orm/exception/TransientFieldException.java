package com.perfectworldprogramming.mobile.orm.exception;

import java.lang.reflect.Field;

public class TransientFieldException  extends DataAccessException {

    private static final long serialVersionUID = -442347181326281845L;

    public TransientFieldException(Field field) {
        super("Cannot persist transient field " + field.getName());
    }
}