package com.perfectworldprogramming.mobile.orm.creator;

import java.util.List;

/**
 * User: Mark Spritzler
 * Date: 3/19/11
 * Time: 2:53 PM
 */
public interface CreateStatementGenerator {

    /**
     * List of Create statements to run on the database to create tables
     *
     * @return List<String> List of Create statements to run on the database to create tables.
     */
    public List<String> getCreateStatements();

    /**
     * Creates a CREATE SQL Statement for the Class that is passed in as a parameter.
     * The Class is mapped with the Spring Mobile ORM annotations. Without those
     * annotations this method should return <null>
     *
     * The returned CREATE statement can be run by an Android SQLLiteDatabaseHelper class to create
     * the tables when the SQLLiteDatabaseHelper class' onCreate method is called.
     *
     * @param clazz domain class
     * @return String Create SQL Statement to run on the database to create a database table.
     */
    public String createCreateStatement(Class<? extends Object> clazz);
    
    /**
     * * Creates a CREATE SQL Statement with 'IF NOT EXISTS' for the Class that is passed in as a parameter.
     * The Class is mapped with the Spring Mobile ORM annotations. Without those
     * annotations this method should return <null>
     *
     * The returned CREATE statement can be run by an Android SQLLiteDatabaseHelper class to create
     * the tables when the SQLLiteDatabaseHelper class' onUpdate method is called.
     *
     * This will create new tables added in the update and also let us know what already exists to update the fields
     * 
     * @param clazz domain class
     * @return String Create SQL Statement to run on the database to create a database table.
     */
    public String createCreateIfNotExistsStatement(Class<? extends Object> clazz);
    
}
