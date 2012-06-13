package com.perfectworldprogramming.mobile.orm.reflection;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.annotations.ForeignKey;
import com.perfectworldprogramming.mobile.orm.annotations.PrimaryKey;
import com.perfectworldprogramming.mobile.orm.exception.DataAccessException;
import com.perfectworldprogramming.mobile.orm.exception.FieldNotFoundException;
/**
 * Allows SQL to refer to domain classes and fields, which are then converted to SQL.
 * Queries place matchable tokens in square brackets and refer to either a direct field or a qualified field
 * using the (case insensitive) {@code class.getSimpleName()}. Duplicate fields will return the first match.
 * In the special case where only the simple class name is entered, the primary key field will be substituted.
 * This is to simplify queries using foreign keys.
 * eg [fieldName] or [MyClass.myField] <br/>
 * {@code "select * from Person where [age] = ?"}<br />
 * {@code "select * from Person where [dateOfBirth] < ?"}<br />
 * {@code "select * from Person where [Person.height] > ?"}<br />
 * {@code "select * from Person p, Address a where p.[Person.height] > ? and p.[Person] = a.[Address.person]"}<br />
 * @author David O'Meara <david.omeara@gmail.com>
 * @since 13/06/2012
 *
 */
public class QueryParser
{
    private QueryParser()
    {
        // prevent external instantiation
    }

    public static String parse(String input, List<Class<?>> domainClasses)
    {
        StringBuilder result = new StringBuilder();
        int start = 0;
        Matcher m = p.matcher(input);
        while (m.find())
        {
            result.append(input.substring(start, m.start()));

            if (m.groupCount() == 2 && m.group(2).length()>0)
            {
                for (Class<?> clazz : domainClasses)
                {
                    final String className = m.group(1);
                    final String fieldName = m.group(2);
                    if(clazz.getSimpleName().equalsIgnoreCase(className))
                    {
                        String replace = getColumnName(clazz, fieldName);
                        if(replace==null || replace.length()==0)
                        {
                            throw new FieldNotFoundException("Could not find field '"+fieldName+"' in class "+clazz.getName());
                        }
                        result.append(replace);
                        break;
                    }
                }
            }
            else 
            {
                final String matchName = m.group(1);
                String replace = null;
                for (Class<?> clazz : domainClasses)
                {
                    if(clazz.getSimpleName().equalsIgnoreCase(matchName))
                    {
                        PrimaryKey pk = new DomainClassAnalyzer().getPrimaryKey(clazz);
                        replace = pk.value();
                        break;
                    }
                }
                if(replace==null)
                {
                    for (Class<?> clazz : domainClasses)
                    {
                        replace = getColumnName(clazz, matchName);
                        if(replace.length()>0)
                        {
                            break;
                        }
                    }
                }
                if(replace==null || replace.length()==0)
                {
                    throw new FieldNotFoundException("Could not find field '"+matchName+"' in domain class(es)");
                }
                result.append(replace);
            }
            start = m.end();
        }
        result.append(input.substring(start));
        return result.toString();
    }

    private static String getColumnName(Class<?> clazz, String fieldName)
    {
        Field field;
        try
        {
            field = clazz.getDeclaredField(fieldName);
        }
        catch (SecurityException e)
        {
            throw new DataAccessException("Failed to view field "+fieldName+" in class "+clazz.getName(), e);
        }
        catch (NoSuchFieldException e)
        {
            return "";
        }
        if (field.isAnnotationPresent(Column.class))
        {
            Column setter = field.getAnnotation(Column.class);
            return setter.value();
        }
        else if (field.isAnnotationPresent(PrimaryKey.class))
        {
            PrimaryKey setter = field.getAnnotation(PrimaryKey.class);
            return setter.value();
        }
        else if (field.isAnnotationPresent(ForeignKey.class))
        {
            ForeignKey setter = field.getAnnotation(ForeignKey.class);
            return setter.value();
        }
        return "";
    }
    
    private static final String REGEX_SUBST = "\\[([a-zA-Z0-9]+)\\.?([a-zA-Z0-9]*)\\]";
    private static final Pattern p = Pattern.compile(REGEX_SUBST);
}
