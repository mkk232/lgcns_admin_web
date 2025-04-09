package com.lgcns.admin.dictionary.controller;

import com.lgcns.admin.common.utils.PageUtils;
import com.lgcns.admin.common.vo.ResponseMessageVO;
import com.lgcns.admin.dictionary.service.KeywordService;
import com.lgcns.admin.dictionary.vo.KeywordVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class KeywordController {

    @Value("${admin.ui.search.max-rows}")
    private int maxRows;

    private final KeywordService keywordService;

    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    /**
     * 키워드 페이지 접근
     */
    @GetMapping("/dictionary/keyword/list.html")
    public ModelAndView goPage() {
        ModelAndView mav = new ModelAndView("layout");
        mav.addObject("viewName", "dictionary/keyword/list");
        return mav;
    }

    /**
     * 키워드 목록 조회
     */
    @GetMapping("/dictionary/keyword/list")
    public ResponseEntity<ResponseMessageVO> getList(@RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                                @RequestParam(name = "pageSize", required = false, defaultValue = "-1") int pageSize,
                                @RequestParam(name = "sortColumnId", required = false, defaultValue = "1") String sortColumnId,
                                @RequestParam(name = "sortDirection", required = false, defaultValue = "DESC") String sortDirection,
                                @RequestParam(name = "searchKeyword", required = false) String searchKeyword) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();

        int localPageSize = pageSize > 0 ? pageSize : this.maxRows;

        paramMap.put("sortColumnId", sortColumnId);
        paramMap.put("sortDirection", sortDirection);
        paramMap.put("page", PageUtils.getPageQueryParams(pageNo, localPageSize));
        paramMap.put("searchKeyword", searchKeyword);

        int totalCnt = this.keywordService.getListCnt(paramMap);
        resultMap.put("result", this.keywordService.getList(paramMap));
        resultMap.put("page", PageUtils.getPaging(pageNo, localPageSize, totalCnt));

        return ResponseEntity.ok().body(new ResponseMessageVO(resultMap));
    }

    /**
     * 키워드 추가
     */
    @PostMapping("/dictionary/keyword/regist")
    public ResponseEntity<ResponseMessageVO> regist(@RequestBody KeywordVO keywordVO) {
        try {
            this.keywordService.regist(keywordVO);
            return ResponseEntity.ok().body(new ResponseMessageVO());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.ok().body(new ResponseMessageVO(HttpStatus.CONFLICT.value(), "이미 등록된 키워드입니다."));
        }
    }

    /**
     * 키워드 수정
     */
    @PostMapping("/dictionary/keyword/modify")
    public ResponseEntity<ResponseMessageVO> modify(@RequestBody KeywordVO keywordVO) {
        this.keywordService.modify(keywordVO);
        return ResponseEntity.ok().body(new ResponseMessageVO());
    }

    /**
     * 키워드 삭제
     */
    @PostMapping("/dictionary/keyword/remove")
    public ResponseEntity<ResponseMessageVO> remove(@RequestBody KeywordVO keywordVO) {
        this.keywordService.remove(keywordVO.getKeywordIdList());
        return ResponseEntity.ok().body(new ResponseMessageVO());
    }
}
