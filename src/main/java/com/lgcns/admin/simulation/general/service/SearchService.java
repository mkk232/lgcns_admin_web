package com.lgcns.admin.simulation.general.service;

import com.lgcns.admin.common.builder.BoolQueryBuilder;
import com.lgcns.admin.common.builder.DSLQueryBuilder;
import com.lgcns.admin.common.builder.FullTextQueryBuilder;
import com.lgcns.admin.common.service.WebService;
import com.lgcns.admin.common.utils.ConvertUtils;
import com.lgcns.admin.common.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SearchService {

    private final WebService webService;

    public SearchService(WebService webService) {
        this.webService = webService;
    }

    public Map<String, Object> search(Map<String, Object> paramMap) {
        String query = buildSearchQuery(paramMap);
        Map<String, Object> resultMap = this.webService.requestSearch(query);

        ConvertUtils.convert(resultMap, null);
        ConvertUtils.cutBodyLength(resultMap);

        log.debug(GsonUtils.toJson(resultMap));

        return resultMap;
    }

    /**
     * 필터링 검색 쿼리 생성
     */
    private String buildSearchQuery(Map<String, Object> paramMap) {
        // 검색 파라미터 추출
        String searchKeyword = (String) paramMap.get("searchKeyword");
        String[] searchFields = getSearchFields(paramMap);
        int from = getIntValue(paramMap, "from", 0);
        int pageSize = getIntValue(paramMap, "pageSize", 10);

        // 메인 bool 쿼리 생성
        BoolQueryBuilder mainBoolQuery = new BoolQueryBuilder().builder();

        // 검색어 쿼리 추가
        FullTextQueryBuilder multiMatchQuery = new FullTextQueryBuilder()
                .multiMatch(searchFields, searchKeyword, null, "phrase");
        mainBoolQuery.must(multiMatchQuery);

        // 전체 쿼리 구성
        DSLQueryBuilder dslQueryBuilder = new DSLQueryBuilder()
                .builder()
                .trackTotalHits(true)
                .from(from)
                .size(pageSize)
                .query(mainBoolQuery)
                .highlight(searchFields, getHighlightOptions(false))
                .build();

        return dslQueryBuilder.buildJson();
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

    private String[] getSearchFields(Map<String, Object> paramMap) {
        if (paramMap.containsKey("searchFields")) {
            return (String[]) paramMap.get("searchFields");
        }

        return new String[]{"subject", "em_body", "attach_name", "attach_body"};
    }
}
