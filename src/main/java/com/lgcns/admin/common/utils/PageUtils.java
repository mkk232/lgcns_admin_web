package com.lgcns.admin.common.utils;

import com.lgcns.admin.common.vo.PagingVO;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PageUtils {
	private PageUtils() {}

	/**
	 * 페이징 정보를 PagingVO에 담는다.
	 */
	public static PagingVO getPaging(int pageNo, int pageSize, int totalCnt) {
		PagingVO pagingVO = new PagingVO();
		pagingVO.setPageNo(pageNo);
		pagingVO.setPageSize(pageSize);
		pagingVO.setTotalCnt(totalCnt);
		
		log.debug("pagingVO: {}", pagingVO);
		return pagingVO;
	}

	/**
	 * Limit, Offset 을 설정한다.
	 */
	public static Map<String, Object> getPageQueryParams(int pageNo, int pageSize) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("limit", pageSize);
		paramMap.put("offset", (pageNo - 1) * pageSize);
		
		log.debug("pageQueryParamMap: {}", paramMap);
		return paramMap;
	}
}
