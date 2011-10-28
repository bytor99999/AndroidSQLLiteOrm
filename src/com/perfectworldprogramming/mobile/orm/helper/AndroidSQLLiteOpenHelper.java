package com.perfectworldprogramming.mobile.orm.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.perfectworldprogramming.mobile.orm.creator.SQLLiteCreateStatementGenerator;
import com.perfectworldprogramming.mobile.orm.exception.DataAccessException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * User: Mark Spritzler
 * Date: 3/10/11
 * Time: 7:51 PM
 */
public class AndroidSQLLiteOpenHelper extends SQLiteOpenHelper {

    protected Class<? extends Object>[] classes;

    public AndroidSQLLiteOpenHelper(Context context, Class<? extends Object>[] classes, String dataBaseName, int dataBaseVersion) {
        super(context, dataBaseName, null, dataBaseVersion);
        this.classes = classes;
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        SQLLiteCreateStatementGenerator sqlLiteCreateStatementGenerator = new SQLLiteCreateStatementGenerator();
        for (Class<? extends Object> clazz : classes) {
            sqLiteDatabase.execSQL(sqlLiteCreateStatementGenerator.createCreateStatement(clazz));
        }
    }

    /*
     * beginTransaction
		run a table creation with if not exists (we are doing an upgrade, so the table might not exists yet, it will fail alter and drop)
		put in a list the existing columns List<String> columns = DBUtils.GetColumns(db, TableName);
		backup table (ALTER table " + TableName + " RENAME TO 'temp_" + TableName)
		create new table (the newest table creation schema)
		get the intersection with the new columns, this time columns taken from the upgraded table (columns.retainAll(DBUtils.GetColumns(db, TableName));)
		restore data (String cols = StringUtils.join(columns, ","); db.execSQL(String.format( "INSERT INTO %s (%s) SELECT %s from temp_%s", TableName, cols, cols, TableName)); )
		remove backup table (DROP table 'temp_" + TableName)
		setTransactionSuccessful
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        SQLLiteCreateStatementGenerator sqlLiteCreateStatementGenerator = new SQLLiteCreateStatementGenerator();   
        sqLiteDatabase.beginTransaction();
        for (Class<? extends Object> clazz : classes) {
        	// Thanks to Pentium10 at Stack Overflow for this solution.
        	String createStatementIfNotExists = sqlLiteCreateStatementGenerator.createCreateIfNotExistsStatement(clazz);
        	String tableName = clazz.getSimpleName();
        	try {
        		sqLiteDatabase.execSQL(createStatementIfNotExists);         		
        		List<String> columns = getColumns(sqLiteDatabase, tableName);
        		sqLiteDatabase.execSQL("ALTER table " + tableName + " RENAME TO 'temp_" + tableName + "'");
        		sqLiteDatabase.execSQL(sqlLiteCreateStatementGenerator.createCreateStatement(clazz));
        		columns.retainAll(getColumns(sqLiteDatabase, tableName));
        		String cols = join(columns, ",");
        		sqLiteDatabase.execSQL(String.format( "INSERT INTO %s (%s) SELECT %s from temp_%s", tableName, cols, cols, tableName));
        		sqLiteDatabase.execSQL("DROP table 'temp_" + tableName +"'");
        	} catch (SQLException e) {
        		Log.v(tableName, e.getMessage(), e);
        		throw new DataAccessException(e.getMessage());
        	}
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        
    }
    
    private static List<String> getColumns(SQLiteDatabase db, String tableName) {
        List<String> ar = null;
        Cursor c = null;
        try {
            c = db.rawQuery("select * from " + tableName + " limit 1", null);
            if (c != null) {
                ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
            }
        } catch (Exception e) {
            Log.v(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return ar;
    }

    private static String join(List<String> list, String delim) {
        StringBuilder buf = new StringBuilder();
        int num = list.size();
        for (int i = 0; i < num; i++) {
            if (i != 0)
                buf.append(delim);
            buf.append((String) list.get(i));
        }
        return buf.toString();
    }
}
