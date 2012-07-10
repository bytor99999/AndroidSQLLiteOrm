package com.perfectworldprogramming.mobile.orm.creator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.annotations.ColumnType;
import com.perfectworldprogramming.mobile.orm.annotations.ForeignKey;
import com.perfectworldprogramming.mobile.orm.annotations.PrimaryKey;
import com.perfectworldprogramming.mobile.orm.reflection.DomainClassAnalyzer;

/**
 * This class generates CREATE SQL statements for mapped Domain objects.
 *
 * There are two approaches to use this class.
 * <ul><li>Assign the classes property to an array of Class objects for each domain
 * object you want create statements for. Then call the getCreateStatements method to return
 * a List of Strings. Each string is a create statement for one table</li>
 * <li>just call the createCreateStatement method passing in a single Class which
 * returns a single Create String SQL statement</li>
 * </ul>
 * 
 * User: Mark Spritzler
 * Date: 3/10/11
 * Time: 8:55 PM
 */
public class SQLLiteCreateStatementGenerator implements CreateStatementGenerator {

    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";
    private static final String PRIMARY_KEY = "PRIMARY KEY";
    private static final String UNIQUE = "UNIQUE";
    private static final String NOT_NULL = "NOT NULL";
    private static final String FOREIGN_KEY_ELEMENT = "<field> INTEGER REFERENCES <foreign_table>(<fk>)"; //" FOREIGN KEY(<fk>) REFERENCES <foreign_table>(<field>)";
    private static final String SPACE = " ";

    private Class<? extends Object>[] classes;
    private DomainClassAnalyzer domainClassAnalyzer = new DomainClassAnalyzer();

    /**
     * Creates a SQLLiteCreateStatementGenerator without the Classes to make create SQL statements for
     * Call the setClasses method to set the classes so that we can create the create statements
     * or call the createCreateStatement
     */
    public SQLLiteCreateStatementGenerator() {}

    /**
     * Constructor that takes an array of domain {@link Class}es to create tables
     * for in SQLLite create statements
     * @param classes array of classes to create tables for
     */
    public SQLLiteCreateStatementGenerator(Class<? extends Object>[] classes) {
        this.classes = classes;
    }

    /**
     * Setter method for classes property
     *
     * @param classes Array of Class objects for all the Domain objects to create tables for
     */
    public void setClasses(Class<? extends Object>[] classes) {
        this.classes = classes;
    }

    @Override
    public List<String> getCreateStatements() {
        if (classes == null) {
            throw new IllegalArgumentException("You must assign the domain classes to the SQLLiteCreateStatementGenerator class before generating the create statements");
        }
        List<String> createStatements = new ArrayList<String>(classes.length);
        for (Class<? extends Object> clazz : classes) {
            createStatements.add(createCreateStatement(clazz));
        }
        return createStatements;
    }

    @Override
    public String createCreateStatement(Class<? extends Object> clazz) {
    	return createStatement(clazz, CREATE_TABLE);
    }
    
    public String createCreateIfNotExistsStatement(Class<? extends Object> clazz) {
    	return createStatement(clazz, CREATE_TABLE_IF_NOT_EXISTS);
    }
    
    /**
     * Always the same code to create a create table statement. Only differences is if it needs to include IF NOT EXISTS.
     */
    private String createStatement(Class<? extends Object> clazz, String create) {
    	StringBuilder createStatement = new StringBuilder(create);
        createStatement.append(clazz.getSimpleName());
        createStatement.append(" (");
        addAllToStatement(createStatement, clazz);
        return createStatement.toString();
    }
    
    private void addAllToStatement(StringBuilder createStatement, Class<? extends Object> clazz) {
    	Field[] fields =  getFieldsToAddToStatement(clazz);

        if (fields.length == 0) {
            return;
        }

        for (int i = 0; i< fields.length; i++ ) {
            Field field = fields[i];
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                addPrimaryKeyToStatement(createStatement, primaryKey);
            }
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                addColumnsToStatement(createStatement, column);
            }
            ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
            if (foreignKey != null) {
                addForeignKeyToStatement(createStatement, field, foreignKey);
            }

            if (i != (fields.length-1)) {
                createStatement.append(", ");
            } else {
                createStatement.append(")");
            }
        }
    }

    private Field[] getFieldsToAddToStatement(Class<? extends Object> clazz) {
        List<Field> fieldsToAdd = new ArrayList<Field>();
        Field[] fields =  clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class) ||
                    field.isAnnotationPresent(Column.class) ||
                    field.isAnnotationPresent(ForeignKey.class)) {
                fieldsToAdd.add(field);
            }
        }
        return fieldsToAdd.toArray(new Field[fieldsToAdd.size()]);
    }

    private void addPrimaryKeyToStatement(StringBuilder createStatement, PrimaryKey primaryKey) {
        String pkName = primaryKey.value();
        if (pkName.equals("")) {
            pkName = "ID";
        }
        createStatement.append(pkName);
        createStatement.append(SPACE);
        createStatement.append(ColumnType.INTEGER);
        createStatement.append(SPACE);
        createStatement.append(PRIMARY_KEY);
    }

    private void addForeignKeyToStatement(StringBuilder createStatement, Field field, ForeignKey foreignKey) {
        Class<? extends Object> clazz = field.getType();
        String fkField = domainClassAnalyzer.getPrimaryKeyFieldName(clazz);
        String foreignTable = field.getType().getSimpleName();
        String s = FOREIGN_KEY_ELEMENT.replaceAll("<foreign_table>", foreignTable)
                .replaceAll("<field>", foreignKey.value())
                .replaceAll("<fk>", fkField);
        createStatement.append(s);
    }

    private void addColumnsToStatement(StringBuilder createStatement, Column column) {    	
        createStatement.append(column.value());
    	createStatement.append(SPACE);
        createStatement.append(column.type().getMapper().getDatabaseColumnType());
        createStatement.append(SPACE);
        if (column.unique()) {
            createStatement.append(UNIQUE);
            createStatement.append(SPACE);
        }
        if (!column.nullable()){
            createStatement.append(NOT_NULL);
            createStatement.append(SPACE);
        }
    }
}
