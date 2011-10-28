package com.perfectworldprogramming.mobile.orm.test.helper;


import com.perfectworldprogramming.mobile.orm.helper.DBHelper;
import com.perfectworldprogramming.mobile.orm.test.Main;
import com.perfectworldprogramming.mobile.orm.test.domain.Account;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;

/**
 * User: Mark Spritzler
 * Date: 3/14/11
 * Time: 9:24 PM
 */
public class DBHelperTests extends ActivityInstrumentationTestCase2<Main> {
    DBHelper helper;

    public DBHelperTests() {
    	super("org.springframework.mobile.orm.test", Main.class);
    }
    
    @SuppressWarnings("unchecked")
	public void setUp() {
    	try {
			super.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		helper = new DBHelper(this.getInstrumentation().getContext(), new Class[]{Person.class, Address.class, Account.class}, "ormtest", 3);
    }
    
    //@Test
    public void testStartDBTests() {
        SQLiteDatabase db = helper.getSqlLiteDatabase();
        assertNotNull(db);
        int version = db.getVersion();
        assertEquals("", 3, version);
    }
}
