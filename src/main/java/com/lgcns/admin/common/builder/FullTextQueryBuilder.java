package com.lgcns.admin.common.builder;


import com.lgcns.admin.exception.QueryBuilderException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FullTextQueryBuilder implements IQueryBuilder {

	private static final int defaultBoost = 2;
	private static final int defaultSlop = 3;
	
    private Map<String, Object> queryMap = new HashMap<>();

    public FullTextQueryBuilder multiMatch(String[] fields, String keyword, String operator, String type) throws QueryBuilderException {
        Map<String, Object> querySettingMap = new HashMap<>();

        querySettingMap.put("query", keyword);
        querySettingMap.put("fields", fields);
        
        if (operator != null) {
        	querySettingMap.put("operator", operator);
        }
        
        if (type != null) {
            querySettingMap.put("type", type);
        }

        this.queryMap.put("multi_match", querySettingMap);
        return this;
    }

    public FullTextQueryBuilder multiMatch(String[] fields, String keyword, String operator, String type, String matchType) throws QueryBuilderException {
        Map<String, Object> querySettingMap = new HashMap<>();

        querySettingMap.put("query", keyword);
        querySettingMap.put("fields", fields);

        if (operator != null) {
        	querySettingMap.put("operator", operator);
        }

        if (type != null) {
            querySettingMap.put("type", type);
        }

        if(matchType != null) {
            querySettingMap.put("_name", keyword + "_" + matchType);
        }

        this.queryMap.put("multi_match", querySettingMap);
        return this;
    }

    public FullTextQueryBuilder multiMatchPhrase(String[] fields, String keyword, int boost, int slop, String type) throws QueryBuilderException {
        Map<String, Object> querySettingMap = new HashMap<>();

        querySettingMap.put("query", keyword);
        querySettingMap.put("fields", fields);
        
        if (Objects.nonNull(boost)) {
        	querySettingMap.put("boost", boost);
        } else {
        	querySettingMap.put("boost", defaultBoost);
        }
        
        if (Objects.nonNull(slop)) {
        	querySettingMap.put("slop", slop);
        } else {
        	querySettingMap.put("slop", defaultSlop);
        }
        
        if (type != null) {
            querySettingMap.put("type", type);
        }

        this.queryMap.put("multi_match", querySettingMap);
        return this;
    }
    
    public FullTextQueryBuilder match(String field, String keyword, String operator) throws QueryBuilderException {
        Map<String, Object> fieldMap = new HashMap<>();
        Map<String, Object> querySettingMap = new HashMap<>();

        querySettingMap.put("query", keyword);
        querySettingMap.put("operator", operator);
        fieldMap.put(field, querySettingMap);

        this.queryMap.put("match", fieldMap);
        return this;
    }

    public FullTextQueryBuilder matchPhrase(String field, String keyword) throws QueryBuilderException {
        Map<String, Object> querySettingMap = new HashMap<>();
        querySettingMap.put(field, keyword);

        this.queryMap.put("match_phrase", querySettingMap);
        return this;
    }

	@Override
	public Map<String, Object> build() {
		return this.queryMap;
	}
}
