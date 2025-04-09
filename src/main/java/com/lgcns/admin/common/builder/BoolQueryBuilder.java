package com.lgcns.admin.common.builder;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoolQueryBuilder implements IQueryBuilder {

	private int minimumShouldMatch;
    private List<Map<String, Object>> mustList = null;
    private List<Map<String, Object>> shouldList = null;
    private List<Map<String, Object>> filterList = null;
    private List<Map<String, Object>> mustNotList = null;
    
    public BoolQueryBuilder builder() {
		this.minimumShouldMatch = 0;
    	this.mustList = new ArrayList<>();
    	this.shouldList = new ArrayList<>();
    	this.filterList = new ArrayList<>();
    	this.mustNotList = new ArrayList<>();
    	
    	return this;
    }

    public BoolQueryBuilder minimumShouldMatch(int minimumShouldMatch) {
		this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    public BoolQueryBuilder must(IQueryBuilder queryBuilder) {
        this.mustList.add(queryBuilder.build());
        return this;
    }

    public BoolQueryBuilder should(IQueryBuilder queryBuilder) {
    	this.shouldList.add(queryBuilder.build());
        return this;
    }

    public BoolQueryBuilder filter(IQueryBuilder queryBuilder) {
    	this.filterList.add(queryBuilder.build());
        return this;
    }

    public BoolQueryBuilder mustNot(IQueryBuilder queryBuilder) {
    	this.mustNotList.add(queryBuilder.build());
        return this;
    }

	@Override
	public Map<String, Object> build() {
		Map<String, Object> boolMap = new HashMap<>();
		Map<String, Object> boolMemberMap = new HashMap<>();
		
		if(this.mustList != null && this.mustList.size() > 0)
			boolMemberMap.put("must", this.mustList);
		
		if(this.shouldList != null && this.shouldList.size() > 0)
			boolMemberMap.put("should", this.shouldList);
		
		if(this.filterList != null && this.filterList.size() > 0)
			boolMemberMap.put("filter", this.filterList);
		
		if(this.mustNotList != null && this.mustNotList.size() > 0)
			boolMemberMap.put("must_not", this.mustNotList);

		if(this.minimumShouldMatch > 0)
			boolMemberMap.put("minimum_should_match", this.minimumShouldMatch);

		if(boolMemberMap != null && boolMemberMap.size() > 0)
			boolMap.put("bool", boolMemberMap);


		
		return boolMap;
	}
}
