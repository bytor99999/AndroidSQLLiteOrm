package com.perfectworldprogramming.mobile.orm.test;

import com.perfectworldprogramming.mobile.orm.R;

import android.app.Activity;
import android.os.Bundle;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //AndroidSQLLiteOpenHelper dbHelper = new AndroidSQLLiteOpenHelper(this.getApplicationContext(), new Class[]{Person.class, Address.class}, "ormtest", 1);
        //dbHelper.getWritableDatabase();
    }
}
