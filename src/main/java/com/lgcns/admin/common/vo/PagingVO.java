package com.lgcns.admin.common.vo;

import lombok.Data;

@Data
public class PagingVO {
	private int pageNo;
	private int pageSize;
	private int totalCnt;
}
