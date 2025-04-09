package com.lgcns.admin.common.builder;

import java.util.HashMap;
import java.util.Map;

public class AggsQueryBuilder implements IQueryBuilder {
	
	private Map<String, Object> aggregationMap = new HashMap<>();

	public AggsQueryBuilder terms(String field) {
        Map<String, Object> termsMap = new HashMap<>();
        termsMap.put("field", field);
        
        this.aggregationMap.put("terms", termsMap);
        return this;
    }
	
	public AggsQueryBuilder terms(String field, int count) {
        Map<String, Object> termsMap = new HashMap<>();
        termsMap.put("field", field);
        termsMap.put("size", count);
        
        this.aggregationMap.put("terms", termsMap);
        return this;
    }

    public AggsQueryBuilder order(Map<String, String> orderOption) {
        @SuppressWarnings("unchecked")
		Map<String, Object> termsMap = (Map<String, Object>) this.aggregationMap.get("terms");
        if (termsMap != null) {
            termsMap.put("order", orderOption);
        }
        return this;
    }

    @Override
    public Map<String, Object> build() {
        return this.aggregationMap;
    }
    
}
