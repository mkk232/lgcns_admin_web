package com.lgcns.admin.common.builder;


import com.lgcns.admin.exception.QueryBuilderException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TermQueryBuilder implements IQueryBuilder {

    private Map<String, Object> termMap = new HashMap<>();

    public TermQueryBuilder term(String field, String keyword) throws QueryBuilderException {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(field, keyword);
        
        this.termMap.put("term", valueMap);
        return this;
    }

    public TermQueryBuilder terms(String field, Object[] keywords) throws QueryBuilderException {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(field, keywords);

        this.termMap.put("terms", valueMap);
        return this;
    }
    
    public TermQueryBuilder terms(String field, List<String> keywords) throws QueryBuilderException {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(field, keywords);

        this.termMap.put("terms", valueMap);
        return this;
    }

	@Override
	public Map<String, Object> build() {
		return this.termMap;
	}

}
