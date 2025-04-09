package com.lgcns.admin.common.vo;

import lombok.Data;

import java.util.Map;

@Data
public class SearchResultVO {
	
	private String keyword;
	private String collapseCd;
	private int took;
	private int totalCnt;
	private Map<String, Object> result;
	
	@Override
	public String toString() {
		return "collapseCd=" + collapseCd
				+ ", keyword=" + keyword + ", took=" + took + ", totalCnt=" + totalCnt + ", result=" + result;
	}
}
