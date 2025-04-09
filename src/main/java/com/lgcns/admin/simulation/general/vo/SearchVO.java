package com.lgcns.admin.simulation.general.vo;

import lombok.Data;

@Data
public class SearchVO {
    // request parameter
    private String searchKeyword;
    private String searchType;
    private int pageNo;
    private int pageSize;

    // internal parameter
    private String[] searchFields;
    private int from;
    private int totalCnt;
}
