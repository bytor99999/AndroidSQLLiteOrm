package com.perfectworldprogramming.mobile.orm.test;

import junit.framework.TestSuite;

import com.perfectworldprogramming.mobile.orm.test.creator.TableCreatorTest;
import com.perfectworldprogramming.mobile.orm.test.helper.DBHelperTests;
import com.perfectworldprogramming.mobile.orm.test.interfaces.CursorExtractorTests;
import com.perfectworldprogramming.mobile.orm.test.interfaces.CursorRowMapperTests;
import com.perfectworldprogramming.mobile.orm.test.reflection.DomainClassAnalyzerTests;

import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

/**
 * User: Mark Spritzler
 * Date: 3/30/11
 * Time: 5:03 PM
 */
public class MyInstrumentationTestRunner extends InstrumentationTestRunner {
    @Override
	public TestSuite getAllTests() {
        InstrumentationTestSuite suite = new InstrumentationTestSuite(this);
        suite.addTestSuite(DomainClassAnalyzerTests.class);
        suite.addTestSuite(DBHelperTests.class);
        //suite.addTestSuite(AndroidSQLiteTemplateTests.class);
        suite.addTestSuite(TableCreatorTest.class);
        suite.addTestSuite(CursorExtractorTests.class);
        suite.addTestSuite(CursorRowMapperTests.class);

        return suite;
    }

	@Override
	public ClassLoader getLoader() {
		return MyInstrumentationTestRunner.class.getClassLoader();
	}
   
}
