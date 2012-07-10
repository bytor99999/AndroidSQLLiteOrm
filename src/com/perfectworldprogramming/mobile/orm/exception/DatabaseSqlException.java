package com.perfectworldprogramming.mobile.orm.exception;

import android.database.SQLException;

public class DatabaseSqlException extends DataAccessException
{

    private static final long serialVersionUID = 6224888640967229700L;

    public DatabaseSqlException(String msg, SQLException sqle)
    {
        super(msg, sqle);
    }

}
