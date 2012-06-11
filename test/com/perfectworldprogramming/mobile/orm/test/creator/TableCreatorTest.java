package com.perfectworldprogramming.mobile.orm.test.creator;

import java.util.List;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.perfectworldprogramming.mobile.orm.creator.SQLLiteCreateStatementGenerator;
import com.perfectworldprogramming.mobile.orm.test.Main;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

/**
 * User: Mark Spritzler
 * Date: 3/14/11
 * Time: 2:40 PM
 */
public class TableCreatorTest extends ActivityInstrumentationTestCase2<Main> {
    
	// TODO need more tests
	private SQLLiteCreateStatementGenerator SQLLiteCreateStatementGenerator = new SQLLiteCreateStatementGenerator();

    public TableCreatorTest() {
    	super("org.springframework.mobile.orm.test", Main.class);
    }
    
    @SuppressWarnings("unchecked")
	public void testGenerateTables() {
        String statement = SQLLiteCreateStatementGenerator.createCreateStatement(Person.class);
        assertNotNull("Statement should not be null", statement);
        Log.d("ORM", statement);
        
        SQLLiteCreateStatementGenerator.setClasses(new Class[]{Person.class, Address.class});
        List<String> createStatements = SQLLiteCreateStatementGenerator.getCreateStatements();
        assertNotNull("Should return a list", createStatements);
        assertEquals("Should return two statements", 2, createStatements.size());
        for (String aStatement : createStatements) {
            Log.d("ORM", aStatement);
        }
    }

}
