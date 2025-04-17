package com.lgcns.admin.simulation.filtering.controller;

import com.lgcns.admin.common.utils.PageUtils;
import com.lgcns.admin.common.utils.ParamUtils;
import com.lgcns.admin.common.vo.ResponseMessageVO;
import com.lgcns.admin.exception.QueryBuilderException;
import com.lgcns.admin.simulation.filtering.service.FilteringSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FilteringSearchController {
    private final FilteringSearchService filteringSearchService;

    public FilteringSearchController(FilteringSearchService filteringSearchService) {
        this.filteringSearchService = filteringSearchService;
    }

    /**
     * 필터링 검색 페이지 이동
     */
    @GetMapping("/simulation/filter/search.html")
    public ModelAndView goPage() {
        ModelAndView mav = new ModelAndView("layout");
        mav.addObject("viewName", "simulation/keyword/search");
        return mav;
    }

    /**
     * 필터링 검색 결과 상세 페이지 이동
     */
    @GetMapping("/simulation/filter/search/attachList.html")
    public ModelAndView goDetailPage(@RequestParam String mailId) {
        ModelAndView mav = new ModelAndView("layout");
        mav.addObject("viewName", "simulation/keyword/detail");
        mav.addObject("mailId", mailId);
        return mav;
    }

    /**
     * 시뮬레이션 필터링 검색
     */
    @GetMapping("/simulation/filter/search")
    public ResponseEntity<ResponseMessageVO> getSearch(@RequestParam int pageNo
                                                       , @RequestParam int pageSize) throws QueryBuilderException {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("from", ParamUtils.getFrom(pageNo, pageSize));
        paramMap.put("pageNo", pageNo);
        paramMap.put("pageSize", pageSize);

        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> searchResultMap = this.filteringSearchService.search(paramMap);
        resultMap.put("result", searchResultMap);
        resultMap.put("page", PageUtils.getPaging(pageNo, pageSize, (int) searchResultMap.get("totalCnt")));

        return ResponseEntity.ok().body(new ResponseMessageVO(resultMap));
    }

    /**
     * 시뮬레이션 필터링 첨부파일 리스트 조회
     */
    @GetMapping("/simulation/filter/search/attachList")
    public ResponseEntity<ResponseMessageVO> getAttachList(@RequestParam String mailId) throws QueryBuilderException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mailId", mailId);

        Map<String, Object> resultMap = this.filteringSearchService.searchDetail(paramMap);

        return ResponseEntity.ok().body(new ResponseMessageVO(resultMap));
    }

    /**
     * 시뮬레이션 필터링 상세 검색
     */
    @GetMapping("/simulation/filter/search/detail")
    public ResponseEntity<ResponseMessageVO> getSearchDetail(@RequestParam String mailId,
                                        @RequestParam(required = false) String attachId) throws QueryBuilderException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mailId", mailId);
        if(attachId != null) {
            paramMap.put("attachId", attachId);
        }

        Map<String, Object> resultMap = this.filteringSearchService.searchDetail(paramMap);

        return ResponseEntity.ok().body(new ResponseMessageVO(resultMap));
    }

}
