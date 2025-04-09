package com.lgcns.admin.simulation.general.controller;

import com.lgcns.admin.common.utils.PageUtils;
import com.lgcns.admin.common.utils.ParamUtils;
import com.lgcns.admin.common.vo.ResponseMessageVO;
import com.lgcns.admin.simulation.general.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * 일반 검색 페이지 이동
     */
    @GetMapping("/simulation/search.html")
    public ModelAndView goPage() {
        ModelAndView mav = new ModelAndView("layout");
        mav.addObject("viewName", "simulation/general/search");
        return mav;
    }

    /**
     * 일반 검색 수행
     */
    @GetMapping("/simulation/search")
    public ResponseEntity<ResponseMessageVO> search(@RequestParam String searchKeyword,
                                                    @RequestParam String searchType,
                                                    @RequestParam int pageNo,
                                                    @RequestParam int pageSize) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("searchKeyword", searchKeyword);
        paramMap.put("searchFields", ParamUtils.getSearchType(searchType));
        paramMap.put("from", ParamUtils.getFrom(pageNo, pageSize));
        paramMap.put("pageNo", pageNo);
        paramMap.put("pageSize", pageSize);

        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> searchResultMap = this.searchService.search(paramMap);
        resultMap.put("result", searchResultMap);
        resultMap.put("page", PageUtils.getPaging(pageNo, pageSize, (int) searchResultMap.get("totalCnt")));

        return ResponseEntity.ok().body(new ResponseMessageVO(resultMap));
    }
}
