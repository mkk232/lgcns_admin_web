package com.lgcns.admin.dictionary.vo;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class KeywordVO {
    private int keywordId;
    private List<Integer> keywordIdList;
    private String keyword;
    private int regUser = 1;
    private int updUser = 1;
    private Timestamp regDt;
    private Timestamp updDt;
}
