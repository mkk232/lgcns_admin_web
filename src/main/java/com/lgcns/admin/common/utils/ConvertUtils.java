package com.lgcns.admin.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class ConvertUtils {
    private ConvertUtils() {}

    /**
     * Total Count 설정, 필터링 키워드 추출, 하이라이트 텍스트 source로 이동
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> convert(Map<String, Object> resultMap, List<String> keywordList) {
        // total count 설정
        setTotalCount(resultMap);
        Map<String, Object> hitsMap = (Map<String, Object>) resultMap.get("hits");
        if(hitsMap != null && !hitsMap.isEmpty()) {
            List<Map<String, Object>> hitsList = (List<Map<String, Object>>) hitsMap.get("hits");
            for (Map<String, Object> hits : hitsList) {

                // 필터링 키워드 추출
                if(keywordList != null) {
                    getFilteringKeyword(hits);
                }

                // 하이라이트 내용을 source로 이동
                Map<String, Object> sourceMap = (Map<String, Object>) hits.get("_source");
                if(hits.containsKey("highlight")) {
                    Map<String, Object> highlightMap = (Map<String, Object>) hits.get("highlight");
                    for(String fieldName : highlightMap.keySet()) {
                        sourceMap.put(fieldName, ((List<String>) highlightMap.get(fieldName)).get(0));
                    }
                }
            }
        }

        return resultMap;
    }

    @SuppressWarnings("unchecked")
    /**
     * 메일 본문, 첨부 본문 길이가 200자를 넘어가는 경우 ... 처리
     */
    public static void cutBodyLength(Map<String, Object> resultMap) {
        Map<String, Object> hitsMap = (Map<String, Object>) resultMap.get("hits");
        if(hitsMap != null && !hitsMap.isEmpty()) {
            List<Map<String, Object>> hitsList = (List<Map<String, Object>>) hitsMap.get("hits");
            for (Map<String, Object> hits : hitsList) {

                Map<String, Object> sourceMap = (Map<String, Object>) hits.get("_source");
                // 메일 본문, 첨부 본문 길이가 200자를 넘어가는 경우 ... 처리
                String[] fields = {"em_body", "attach_body"};
                String body = null;
                for(String field : fields) {
                    if(sourceMap.containsKey(field)) {
                        body = (String) sourceMap.get(field);
                        if(body != null) {
                            if (body.length() > 200) {
                                body = body.substring(0, 200) + "...";
                                sourceMap.put(field, body);
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void setTotalCount(Map<String, Object> resultMap) {
        int count = 0;
        if(resultMap.containsKey("aggregations")) {
            // collapse 쿼리 사용 시 total count가 일치하지 않는 문제로 인해 aggregations.total_count로 설정
            Map<String, Object> aggregationsMap = (Map<String, Object>) resultMap.get("aggregations");
            count = (int)((Map<String, Object>) aggregationsMap.get("total_count")).get("value");
        } else {
            Map<String, Object> hitsMap = (Map<String, Object>) resultMap.get("hits");
            if(hitsMap != null && !hitsMap.isEmpty()) {
                Map<String, Object> totalMap = (Map<String, Object>) hitsMap.get("total");
                count = (int) totalMap.get("value");
            }
        }

        resultMap.put("totalCnt", count);
    }

    @SuppressWarnings("unchecked")
    private static void getFilteringKeyword(Map<String, Object> hits) {
        Map<String, Object> sourceMap = (Map<String, Object>) hits.get("_source");
        boolean isMailType = "N".equals(sourceMap.get("attach_exist"));
        boolean isMail;
        boolean isAttach;
        Set<String> keywordSet;
        String[] matchedQuerySplit;
        String type;

        if (hits.containsKey("matched_queries")) {
            keywordSet = new TreeSet<>();
            List<String> matchedQueries = (List<String>) hits.get("matched_queries");
            for (String matchedQuery : matchedQueries) {
                matchedQuerySplit = matchedQuery.split("_");
                type = matchedQuerySplit[1];
                isMail = "mail".equals(type);
                isAttach = "attach".equals(type);

                // Mail 타입이고 subject, em_body 또는 Attach 타입이고 attach_name, attach_body인 경우
                if ((isMailType && isMail) || (!isMailType && isAttach)) {
                    keywordSet.add(matchedQuerySplit[0]);
                }
            }

            hits.put("filteringKeywords", keywordSet);
        }
    }

    // 필터링 키워드 추출 방식 변경으로 인해 미사용
    @Deprecated
    @SuppressWarnings("unchecked")
    private static void getFilteringKeyword(Map<String, Object> hits, List<String> searchKeywords) {
        Set<String> keywordSet = new TreeSet<>();
        boolean isMailType;
        Map<String, Object> sourceMap = (Map<String, Object>) hits.get("_source");
        if("N".equals(sourceMap.get("attach_exist"))) {
            isMailType = true;
        } else {
            isMailType = false;
        }

        boolean isMailField = false;
        boolean isAttachField = false;
        List<String> highlightValues = null;
        String key = null;
        if (hits.containsKey("highlight")) {
            Map<String, Object> highlightMap = (Map<String, Object>) hits.get("highlight");
            for (Map.Entry<String, Object> entry : highlightMap.entrySet()) {
                highlightValues = (List<String>) entry.getValue();
                key = entry.getKey();

                isMailField = "subject".equals(key) || "em_body".equals(key);
                isAttachField = "attach_name".equals(key) || "attach_body".equals(key);

                // Mail 타입이고 subject, em_body 또는 Attach 타입이고 attach_name, attach_body인 경우
                if ((isMailType && isMailField) || (!isMailType && isAttachField)) {
                    for (String highlightValue : highlightValues) {
                        String plainText = highlightValue.replaceAll("<[^>]*>", ""); // HTML 태그 제거
//                        plainText = plainText.replaceAll("[^\\p{L}\\p{N}]", ""); // 모든 특수문자 제거
                        for (String keyword : searchKeywords) {
                            if (plainText.matches(".*" + keyword + ".*")) {
                                keywordSet.add(keyword);
                            }
                        }
                    }
                }
            }

            hits.put("filteringKeywords", keywordSet);
        }
    }

    public static void main(String[] args) {

    }
}
