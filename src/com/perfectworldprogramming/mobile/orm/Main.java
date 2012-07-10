package com.perfectworldprogramming.mobile.orm;

import com.perfectworldprogramming.mobile.orm.R;

import com.perfectworldprogramming.mobile.orm.helper.AndroidSQLLiteOpenHelper;

import android.app.Activity;
import android.os.Bundle;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        @SuppressWarnings("unchecked")
        AndroidSQLLiteOpenHelper dbHelper = new AndroidSQLLiteOpenHelper(this.getApplicationContext(), new Class[]{}, "ormtest", 1);
        dbHelper.getWritableDatabase();
    }
}