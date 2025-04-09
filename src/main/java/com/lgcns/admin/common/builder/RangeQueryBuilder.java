package com.lgcns.admin.common.builder;

import com.lgcns.admin.exception.QueryBuilderException;

import java.util.HashMap;
import java.util.Map;

public class RangeQueryBuilder implements IQueryBuilder {

    private Map<String, Object> rangeMap = new HashMap<>();

    public RangeQueryBuilder rangeStr(String field, String startStr, String endStr, String format) throws QueryBuilderException {
    	
    	Map<String, Object> rangeMap = new HashMap<String, Object>();
		Map<String, Object> fieldMap = new HashMap<>();
        
		if (startStr != null && startStr.length() > 0) {
            fieldMap.put("gte", startStr);
        }
        
        if (endStr != null && endStr.length() > 0) {
            fieldMap.put("lte", endStr);
        }
        
        if (format != null && format.length() > 0) {
        	fieldMap.put("format", format);
        }
        
        rangeMap.put(field, fieldMap);
        
        this.rangeMap.put("range", rangeMap);
        return this;
    }
    
    public RangeQueryBuilder rangeInt(String field, int startInt, int endInt) throws QueryBuilderException {
    	
    	Map<String, Object> rangeMap = new HashMap<String, Object>();
		Map<String, Object> fieldMap = new HashMap<>();
        
		if (startInt >= 0) {
            fieldMap.put("gte", startInt);
        }
		
		if (endInt >= 0) {
			fieldMap.put("lte", endInt);
        }
        
        rangeMap.put(field, fieldMap);
        
        this.rangeMap.put("range", rangeMap);
        return this;
    }

	@Override
	public Map<String, Object> build() {
		return this.rangeMap;
	}

}
