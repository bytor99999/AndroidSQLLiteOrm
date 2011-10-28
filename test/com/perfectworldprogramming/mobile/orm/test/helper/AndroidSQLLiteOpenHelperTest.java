package com.perfectworldprogramming.mobile.orm.test.helper;


import com.perfectworldprogramming.mobile.orm.helper.AndroidSQLLiteOpenHelper;
import com.perfectworldprogramming.mobile.orm.test.Main;
import com.perfectworldprogramming.mobile.orm.test.domain.Account;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;

/**
 * User: Mark Spritzler
 * Date: 5/4/11
 * Time: 6:22 PM
 */
public class AndroidSQLLiteOpenHelperTest extends ActivityInstrumentationTestCase2<Main> {
	AndroidSQLLiteOpenHelper helper;

    public AndroidSQLLiteOpenHelperTest() {
    	super("org.springframework.mobile.orm.test", Main.class);
    }
    
    @SuppressWarnings("unchecked")
	public void setUp() {
    	try {
			super.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Context ctx = this.getInstrumentation().getContext();
		helper = new AndroidSQLLiteOpenHelper(ctx, new Class[]{Person.class, Address.class, Account.class}, "ormtest", 3);
    }

	public void testGetWritableDatabase() {
		SQLiteDatabase db = helper.getWritableDatabase();
        assertNotNull(db);
        int version = db.getVersion();
        assertEquals("", 3, version);
	}
	
	public void tearDown() {
		helper.close();
	}

}
