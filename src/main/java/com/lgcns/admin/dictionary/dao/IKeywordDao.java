package com.lgcns.admin.dictionary.dao;

import com.lgcns.admin.dictionary.vo.KeywordVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IKeywordDao {

    int existKeyword(String keyword);

    KeywordVO selectKeyword(int keywordId);

    int selectKeywordListCnt(Map<String, Object> paramMap);

    List<String> selectKeywordAllList();

    List<KeywordVO> selectKeywordList(Map<String, Object> paramMap);

    int insertKeyword(KeywordVO keywordVO);

    int updateKeyword(KeywordVO keywordVO);

    int deleteKeyword(int keywordId);
}
