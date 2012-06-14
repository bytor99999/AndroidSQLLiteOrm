package com.perfectworldprogramming.mobile.orm.oql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.database.sqlite.SQLiteDatabase;

import com.perfectworldprogramming.mobile.orm.AndroidSQLiteTemplate;
import com.perfectworldprogramming.mobile.orm.interfaces.CursorExtractor;
import com.perfectworldprogramming.mobile.orm.interfaces.CursorRowMapper;

/**
 * Decorates the {@link AndroidSQLiteTemplate} with operations which refer to the domain
 * object rather than database, allowing data access to be persistence agnostic.
 * @author David O'Meara <david.omeara@gmail.com>
 * @since 14/06/2012
 *
 */
public class AndroidOqlTemplate extends AndroidSQLiteTemplate
{
    public AndroidOqlTemplate(SQLiteDatabase sqLiteDatabase) {
        super(sqLiteDatabase);
    }

    public long insert(String oql, OqlParameter... args)
    {
        return super.insert(QueryParser.parse(oql, getTypes(args)), convertParams(args));
    }

    public void update(String oql, OqlParameter... args)
    {
        //super.update(oql, args);
        super.update(QueryParser.parse(oql, getTypes(args)), convertParams(args));
    }

    public void delete(String oql, OqlParameter... args)
    {
        //super.delete(oql, args);
        super.delete(QueryParser.parse(oql, getTypes(args)), convertParams(args));
    }

    public int queryForInt(String oql, OqlParameter... args)
    {
        //return super.queryForInt(oql, args);
        return super.queryForInt(QueryParser.parse(oql, getTypes(args)), convertParams(args));
    }

    public long queryForLong(String oql, OqlParameter... args)
    {
        //return super.queryForLong(oql, args);
        return super.queryForLong(QueryParser.parse(oql, getTypes(args)), convertParams(args));
    }

    public String queryForString(String oql, OqlParameter... args)
    {
        //return super.queryForString(oql, args);
        return super.queryForString(QueryParser.parse(oql, getTypes(args)), convertParams(args));
    }

    public <T> T queryForObject(String oql, CursorRowMapper<T> cursorRowMapper, OqlParameter... args)
    {
        //return super.queryForObject(oql, cursorRowMapper, args);
        return super.queryForObject(QueryParser.parse(oql, getTypes(args)), cursorRowMapper, convertParams(args));
    }

    public <T> T queryForObject(String oql, Class<T> clazz, OqlParameter... args)
    {
        //return super.queryForObject(oql, clazz, args);
        return super.queryForObject(QueryParser.parse(oql, getTypes(args)), clazz, convertParams(args));
    }

    public <T> T queryForObject(String oql, CursorExtractor<T> cursorExtractor, OqlParameter... args)
    {
        //return super.queryForObject(oql, cursorExtractor, args);
        return super.queryForObject(QueryParser.parse(oql, getTypes(args)), cursorExtractor, convertParams(args));
    }

    public <T> List<T> query(String oql, Class<T> clazz, OqlParameter... args)
    {
        //return super.query(oql, clazz, args);
        return super.query(QueryParser.parse(oql, getTypes(args)), clazz, convertParams(args));
    }

    public <T> List<T> query(String oql, CursorRowMapper<T> cursorRowMapper, OqlParameter... args)
    {
        //return super.query(oql, cursorRowMapper, args);
        return super.query(QueryParser.parse(oql, getTypes(args)), cursorRowMapper, convertParams(args));
    }
    
    @Override
    public Object mapQueryParameter(Object value, Class<?> clazz, String fieldName)
    {
        return this.domainClassAnalyzer.mapQueryParameterByFieldName(value, clazz, fieldName);
    }

    private List<Class<?>> getTypes(OqlParameter... args)
    {
        Set<Class<?>> types = new HashSet<Class<?>>();
        for(OqlParameter arg:args)
        {
            types.add(arg.getDomainClass());
        }
        List<Class<?>> result = new ArrayList<Class<?>>(types.size());
        result.addAll(types);
        return result;
    }
    private Object[] convertParams(OqlParameter... args)
    {
        Object[] result = new Object[args.length];
        for(int i=0;i<args.length;i++)
        {
            result[i] = this.mapQueryParameter(args[i].getDomainValue(), args[i].getDomainClass(), args[i].getFieldName());
        }
        return result;
    }
    
}
