package com.lgcns.admin.simulation.filtering.service;

import com.lgcns.admin.common.builder.BoolQueryBuilder;
import com.lgcns.admin.common.builder.DSLQueryBuilder;
import com.lgcns.admin.common.builder.FullTextQueryBuilder;
import com.lgcns.admin.common.builder.TermQueryBuilder;
import com.lgcns.admin.common.service.WebService;
import com.lgcns.admin.common.utils.ConvertUtils;
import com.lgcns.admin.common.utils.GsonUtils;
import com.lgcns.admin.dictionary.service.KeywordService;
import com.lgcns.admin.exception.QueryBuilderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FilteringSearchService {

    private final KeywordService keywordService;

    private final WebService webService;

    public FilteringSearchService(KeywordService keywordService, WebService webService) {
        this.keywordService = keywordService;
        this.webService = webService;
    }

    public Map<String, Object> search(Map<String, Object> paramMap) throws QueryBuilderException {
        List<String> keywordList = this.keywordService.getAllKeywordList();
        paramMap.put("searchKeyword", keywordList);

        String query = this.buildSearchQuery(paramMap);
        Map<String, Object> resultMap = this.webService.requestSearch(query);

        ConvertUtils.convert(resultMap, null);

        log.debug(GsonUtils.toJson(resultMap));
        return resultMap;

    }

    public Map<String, Object> searchDetail(Map<String, Object> paramMap) throws QueryBuilderException {
        List<String> keywordList = this.keywordService.getAllKeywordList();
        paramMap.put("searchKeyword", keywordList);
//        paramMap.put("searchKeyword", String.join(" ", keywordList));

        String query = this.buildDetailSearchQuery(paramMap);
        Map<String, Object> resultMap = this.webService.requestSearch(query);

        ConvertUtils.convert(resultMap, keywordList);

        log.debug(GsonUtils.toJson(resultMap));
        return resultMap;

    }

    /**
     * 필터링 검색 쿼리 생성
     */
    private String buildSearchQuery(Map<String, Object> paramMap) throws QueryBuilderException {
        // 검색 파라미터 추출
        List<String> searchKeywordList = (List<String>) paramMap.get("searchKeyword");
        String[] searchFields = getSearchFields(paramMap);
        int from = getIntValue(paramMap, "from", 0);
        int pageSize = getIntValue(paramMap, "pageSize", 10);

        // 메인 bool 쿼리 생성
        BoolQueryBuilder mainBoolQuery = new BoolQueryBuilder().builder();

        // 첨부파일 검색 쿼리 생성
        BoolQueryBuilder attachBoolQuery = createAttachSearchQuery(searchKeywordList);

        // 메일 본문 검색 쿼리 생성
        BoolQueryBuilder mailBoolQuery = createMailSearchQuery(searchKeywordList);

        // Should 조건 추가
        mainBoolQuery.should(attachBoolQuery);
        mainBoolQuery.should(mailBoolQuery);
        mainBoolQuery.minimumShouldMatch(1);

        // collapse 설정
        Map<String, Object> innerHits = new HashMap<>();
        innerHits.put("name", "attachList");
        innerHits.put("size", 10);
        innerHits.put("_source", Map.of("excludes", "*"));

        // 전체 쿼리 구성
        DSLQueryBuilder dslQueryBuilder = new DSLQueryBuilder()
                .builder()
                .trackTotalHits(true)
                .from(from)
                .size(pageSize)
                .query(mainBoolQuery)
                .highlight(searchFields, getHighlightOptions(false))
                .collapse("em_id.keyword", innerHits)
                .aggregation(getAggregationOptions())
                .source(new String[]{"em_id", "subject", "sender", "senddtm"})
                .build();

        return dslQueryBuilder.buildJson();
    }

    /**
     * 필터링 검색 상세 쿼리 생성
     */
    private String buildDetailSearchQuery(Map<String, Object> paramMap) throws QueryBuilderException {
        // 검색 파라미터 추출
        List<String> searchKeywordList = (List<String>) paramMap.get("searchKeyword");
        String[] searchFields = getSearchFields(paramMap);
        boolean isDetailSearch = paramMap.containsKey("mailId") || paramMap.containsKey("attachId");

        // 메인 bool 쿼리 생성
        BoolQueryBuilder mainBoolQuery = new BoolQueryBuilder().builder();

        // 상세 검색 조건 추가
        addDetailSearchConditions(paramMap, mainBoolQuery);

        // 첨부파일 검색 쿼리 생성
        BoolQueryBuilder attachBoolQuery = createAttachSearchQuery(searchKeywordList);

        // 메일 본문 검색 쿼리 생성
        BoolQueryBuilder mailBoolQuery = createMailSearchQuery(searchKeywordList);

        // Should 조건 추가
        mainBoolQuery.should(attachBoolQuery);
        mainBoolQuery.should(mailBoolQuery);
        mainBoolQuery.minimumShouldMatch(1);

        // 전체 쿼리 구성
        DSLQueryBuilder dslQueryBuilder = new DSLQueryBuilder()
                .builder()
                .trackTotalHits(true)
                .from(-1)
                .size(-1)
                .query(mainBoolQuery)
                .highlight(searchFields, getHighlightOptions(isDetailSearch))
                .source(getSourceIncludes(paramMap))
                .build();

        return dslQueryBuilder.buildJson();
    }

    private void addDetailSearchConditions(Map<String, Object> paramMap, BoolQueryBuilder mainBoolQuery) throws QueryBuilderException {
        if (paramMap.containsKey("mailId")) {
            TermQueryBuilder termQuery = new TermQueryBuilder()
                    .term("em_id.keyword", (String) paramMap.get("mailId"));
            mainBoolQuery.must(termQuery);
        }

        if (paramMap.containsKey("attachId")) {
            TermQueryBuilder termQuery = new TermQueryBuilder()
                    .term("attach_id.keyword", (String) paramMap.get("attachId"));
            mainBoolQuery.must(termQuery);
        }
    }

    private BoolQueryBuilder createAttachSearchQuery(List<String> searchKeywordList) throws QueryBuilderException {
        BoolQueryBuilder attachBoolQuery = new BoolQueryBuilder().builder();

        // should 조건
        FullTextQueryBuilder multiMatchQuery = null;
        for(String keyword : searchKeywordList) {
            multiMatchQuery = new FullTextQueryBuilder();
            multiMatchQuery.multiMatch(new String[]{"attach_name", "attach_body"}, keyword, null, "phrase", "attach");
            attachBoolQuery.should(multiMatchQuery);
        }

        // 첨부파일이 존재하는 경우
        TermQueryBuilder termQuery = new TermQueryBuilder()
                .term("attach_exist.keyword", "Y");
        attachBoolQuery.must(termQuery);

        attachBoolQuery.minimumShouldMatch(1);

        return attachBoolQuery;
    }

    private BoolQueryBuilder createMailSearchQuery(List<String> searchKeywordList) throws QueryBuilderException {
        BoolQueryBuilder mailBoolQuery = new BoolQueryBuilder().builder();

        // should 조건
        FullTextQueryBuilder multiMatchQuery = null;
        for(String keyword : searchKeywordList) {
            multiMatchQuery = new FullTextQueryBuilder();
            multiMatchQuery.multiMatch(new String[]{"subject", "em_body"}, keyword, null, "phrase", "mail");
            mailBoolQuery.should(multiMatchQuery);
        }

        // 첨부파일이 존재하지 않는 경우
        TermQueryBuilder termQuery = new TermQueryBuilder()
                .term("attach_exist.keyword", "N");

        mailBoolQuery.must(termQuery);

        mailBoolQuery.minimumShouldMatch(1);

        return mailBoolQuery;
    }


    private Map<String, Map<String, Object>> getAggregationOptions() {
        Map<String, Map<String, Object>> aggs = new HashMap<>();
        aggs.put("total_count", Map.of(
                "cardinality", Map.of(
                        "field", "em_id.keyword"
                )
        ));
        return aggs;
    }

    private Map<String, Object> getHighlightOptions(boolean isDetailSearch) {
        Map<String, Object> options = new HashMap<>();
        options.put("fragment_size", 150);
        options.put("number_of_fragments", isDetailSearch ? 0 : 1);
        options.put("pre_tags", new String[]{"<span style=\"color: red\">"});
        options.put("post_tags", new String[]{"</span>"});
        return options;
    }

    private int getIntValue(Map<String, Object> paramMap, String key, int defaultValue) {
        if (paramMap.containsKey(key)) {
            return (int) paramMap.get(key);
        }
        return defaultValue;
    }

    private String[] getSourceIncludes(Map<String, Object> paramMap) {
        if (paramMap.containsKey("attachId")) {
            return new String[]{"attach_id", "attach_name", "attach_exist", "attach_body"};
        }
        return new String[]{"attach_id", "attach_name", "attach_exist", "subject", "em_body"};
    }

    private String[] getSearchFields(Map<String, Object> paramMap) {
        if (paramMap.containsKey("searchFields")) {
            return (String[]) paramMap.get("searchFields");
        }
        return new String[]{"subject", "em_body", "attach_name", "attach_body"};
    }
}
