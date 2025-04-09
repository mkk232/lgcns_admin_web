package com.lgcns.admin.common.builder;

import com.lgcns.admin.common.utils.GsonUtils;
import com.lgcns.admin.common.utils.SearchUtil;

import java.util.*;

public class DSLQueryBuilder {
	
	private Map<String, Object> dslMap; //전체 DSL Map
    private boolean trackTotalHits; // 전체 검색결과 10000건 제한 해제
    private int size; // 요청 검색 count
    private int from; // 요청 페이지
    private Map<String, Object> queryMap; // 쿼리 Map
    private List<Map<String, Map<String, String>>> sortList; // 정렬
    private Map<String, Object> highlightMap = new HashMap<>(); // 하이라이트 Map
    private Map<String, Map<String, Object>> aggregationsMap = new HashMap<>(); // 어그리게이션 Map
    private Map<String, Object> sourceMap = new HashMap<>(); // 소스 Map(include 대상)
    
    public DSLQueryBuilder builder() {
    	dslMap = new LinkedHashMap<>();
        return this;
    }

    public DSLQueryBuilder trackTotalHits(boolean trackTotalHits) {
        this.trackTotalHits = trackTotalHits;
        return this;
    }

    public DSLQueryBuilder from(int from) {
        this.from = from;
        return this;
    }

    public DSLQueryBuilder size(int size) {
        this.size = size;
        return this;
    }

    public DSLQueryBuilder sortList(List<Map<String, Map<String, String>>> sortList) {
        if (!sortList.isEmpty()) {
            this.sortList = sortList;
        }
        return this;
    }
    
    public DSLQueryBuilder sort(String fields, String sep1, String sep2) {
        
        List<Map<String, Map<String, String>>> resultList = new ArrayList<>();
	    
        if (fields != null && fields.length() > 0) {
	        List<String> sortList = SearchUtil.getStringToList(fields, sep1);

	        for (String item : sortList) {
	            if (item.indexOf(sep2) > -1) {
	                String fieldName = item.split(sep2)[0];
	                String direction = item.split(sep2)[1];

	                Map<String, String> orderMap = new HashMap<>();
	                orderMap.put("order", direction);

	                Map<String, Map<String, String>> sortMap = new HashMap<>();
	                sortMap.put(fieldName, orderMap);

	                resultList.add(sortMap);
	            }
	        }
	    }
        
	    this.sortList = resultList;
        return this;
    }
    
    public DSLQueryBuilder highlight(String[] fields, Map<String, Object> highlightOptionMap) {
        if (fields != null && fields.length > 0) {
            Map<String, Object> fieldMap = new HashMap<>();
            for (String field : fields) {
                fieldMap.put(field, new HashMap<>());
            }
            this.highlightMap.put("fields", fieldMap);
        }
        
        if (highlightOptionMap!= null && highlightOptionMap.size() > 0) {
            this.highlightMap.putAll(highlightOptionMap);
        }
        return this;
    }
    
    public DSLQueryBuilder aggregation(Map<String, Map<String, Object>> arrMap) {
        if (!arrMap.isEmpty()) {
            this.aggregationsMap = arrMap;
        }
        return this;
    }
    
    public DSLQueryBuilder source(String[] fields) {
        if (fields != null && fields.length > 0) {
            this.sourceMap.put("includes", fields);
        }
        return this;
    }

    public DSLQueryBuilder collapse(String field, Map<String, Object> innerHits) {
        if (field != null && !field.isEmpty()) {
            Map<String, Object> collapseMap = new HashMap<>();
            collapseMap.put("field", field);

            if (innerHits != null && !innerHits.isEmpty()) {
                collapseMap.put("inner_hits", innerHits);
            }

            dslMap.put("collapse", collapseMap);
        }

        return this;
    }

    public DSLQueryBuilder query(IQueryBuilder queryBuilder) {
        this.queryMap = queryBuilder.build();
        return this;
    }

    public DSLQueryBuilder build() {
    	if (this.trackTotalHits) {
        	dslMap.put("track_total_hits", true);
        }
    	
        if (this.size >= 0) {
        	dslMap.put("size", this.size);
        }

        if (this.from >= 0) {
        	dslMap.put("from", this.from);
        }
        
        if (this.queryMap != null && !this.queryMap.isEmpty()) {
            dslMap.put("query", this.queryMap);
        }

        if (this.sortList != null && !this.sortList.isEmpty()) {
        	dslMap.put("sort", this.sortList);
        }
        
        if (this.highlightMap != null && !this.highlightMap.isEmpty()) {
        	dslMap.put("highlight", this.highlightMap);
        }
        
        if (this.aggregationsMap != null && !this.aggregationsMap.isEmpty()) {
        	dslMap.put("aggs", this.aggregationsMap);
        }
        
        if (this.sourceMap != null && !this.sourceMap.isEmpty()) {
        	dslMap.put("_source", this.sourceMap);
        }
        
        return this;
    }

    public String buildJson() {
        return GsonUtils.toJson(this.dslMap);
    }
	
}
