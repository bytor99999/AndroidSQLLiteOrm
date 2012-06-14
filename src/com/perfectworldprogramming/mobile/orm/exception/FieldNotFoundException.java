package com.perfectworldprogramming.mobile.orm.exception;

public class FieldNotFoundException extends DataAccessException
{

    private static final long serialVersionUID = 4572576123276903345L;

    public FieldNotFoundException(String msg)
    {
        super(msg);
    }

}
