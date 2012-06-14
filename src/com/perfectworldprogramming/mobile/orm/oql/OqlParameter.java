package com.perfectworldprogramming.mobile.orm.oql;

/**
 * Immutable class containing the Domain layer representation of a value to be persisted.
 * @author David O'Meara <david.omeara@gmail.com>
 * @since 14/06/2012
 *
 */
public final class OqlParameter
{
    public OqlParameter(Class<?> domainClass, String fieldName, Object domainValue)
    {
        this.domainClass = domainClass;
        this.fieldName = fieldName;
        this.domainValue = domainValue;
    }

    public Class<?> getDomainClass()
    {
        return domainClass;
    }
    public String getFieldName()
    {
        return fieldName;
    }
    public Object getDomainValue()
    {
        return domainValue;
    }

    private final Class<?> domainClass;
    private final String fieldName;
    private final Object domainValue;
}
