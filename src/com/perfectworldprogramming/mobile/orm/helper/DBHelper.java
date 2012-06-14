package com.perfectworldprogramming.mobile.orm.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * This class might be deprecated if not needed
 * 
 * User: Mark Spritzler
 * Date: 3/10/11
 * Time: 8:23 PM
 */
public class DBHelper {
    private SQLiteDatabase sqlLiteDatabase;
    private final AndroidSQLLiteOpenHelper openHelper;

    public DBHelper(Context context, Class<? extends Object>[] classes, String dataBaseName, int dataBaseVersion) {
        if(!dataBaseName.endsWith(".db"))
        {
            dataBaseName+=".db";
        }
    	Log.d("ORM", " " + context + " " + classes + " " + dataBaseName + " " + dataBaseVersion);
        openHelper = new AndroidSQLLiteOpenHelper(context, classes, dataBaseName, dataBaseVersion);
        establishDb();
    }

    private void establishDb() {
        if (sqlLiteDatabase == null) {
            sqlLiteDatabase = openHelper.getWritableDatabase();
        }
}
    public void cleanup() {
        if (sqlLiteDatabase != null) {
            sqlLiteDatabase.close();
            sqlLiteDatabase = null;
        }
    }

    public SQLiteDatabase getSqlLiteDatabase() {
        return sqlLiteDatabase;
    }
}
