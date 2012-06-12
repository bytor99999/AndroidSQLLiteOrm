package com.perfectworldprogramming.mobile.orm.annotations;

import com.perfectworldprogramming.mobile.orm.interfaces.ColumnTypeMapper;
import com.perfectworldprogramming.mobile.orm.reflection.BlobMapper;
import com.perfectworldprogramming.mobile.orm.reflection.BooleanMapper;
import com.perfectworldprogramming.mobile.orm.reflection.DateMapper;
import com.perfectworldprogramming.mobile.orm.reflection.DoubleMapper;
import com.perfectworldprogramming.mobile.orm.reflection.FloatMapper;
import com.perfectworldprogramming.mobile.orm.reflection.IntegerMapper;
import com.perfectworldprogramming.mobile.orm.reflection.LongMapper;
import com.perfectworldprogramming.mobile.orm.reflection.StringMapper;

/**
 * User: Mark Spritzler
 * Date: 3/10/11
 * Time: 8:42 PM
 */
public enum ColumnType {
    STRING(StringMapper.INSTANCE),
    INTEGER(IntegerMapper.INSTANCE),
    LONG(LongMapper.INSTANCE),
    FLOAT(FloatMapper.INSTANCE),
    DOUBLE(DoubleMapper.INSTANCE),
    BLOB(BlobMapper.INSTANCE),
    BOOLEAN(BooleanMapper.INSTANCE),
    DATE(DateMapper.INSTANCE);

    private <T> ColumnType(ColumnTypeMapper<T> mapper)
    {
        this.mapper = mapper;
    }
    
    @SuppressWarnings("unchecked")
	public <T> ColumnTypeMapper<T> getMapper()
    {
        return this.mapper;
    }

    @SuppressWarnings("rawtypes")
	private final ColumnTypeMapper mapper;
}
