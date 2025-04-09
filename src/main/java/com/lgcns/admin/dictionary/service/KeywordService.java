package com.lgcns.admin.dictionary.service;

import com.lgcns.admin.dictionary.dao.IKeywordDao;
import com.lgcns.admin.dictionary.vo.KeywordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KeywordService {

    private final IKeywordDao keywordDao;


    public KeywordService(IKeywordDao keywordDao) {
        this.keywordDao = keywordDao;
    }

    public int getListCnt(Map<String, Object> paramMap) {
        return this.keywordDao.selectKeywordListCnt(paramMap);
    }

    public List<String> getAllKeywordList() {
        return this.keywordDao.selectKeywordAllList();
    }

    public List<KeywordVO> getList(Map<String, Object> paramMap) {
        return this.keywordDao.selectKeywordList(paramMap);
    }

    public void regist(KeywordVO keywordVO) {
        int affectedRow = this.keywordDao.insertKeyword(keywordVO);
        if(affectedRow == 0) {
            log.error("Can't regist keyword. - keyword: " + keywordVO.getKeyword());
            throw new IllegalArgumentException("Can't regist keyword - keyword: " + keywordVO.getKeyword());
        }
    }

    public void modify(KeywordVO keywordVO) {
        KeywordVO dbKeywordVO = this.keywordDao.selectKeyword(keywordVO.getKeywordId());
        if (dbKeywordVO == null) {
            log.error("Can't find keyword. - keywordId: {}", keywordVO.getKeywordId());
            throw new NullPointerException("Can't find keyword - keywordId: " + keywordVO.getKeywordId());
        }

        int affectedRow = this.keywordDao.updateKeyword(keywordVO);
        if (affectedRow == 0) {
            log.error("Can't modify keyword. - keywordId: " + keywordVO.getKeywordId());
            throw new IllegalArgumentException("Can't modify keyword - keywordId: " + keywordVO.getKeywordId());
        }
    }

    public void remove(List<Integer> keywordIdList) {
        for(int keywordId : keywordIdList) {
            KeywordVO keywordVO = this.keywordDao.selectKeyword(keywordId);
            if (keywordVO == null) {
                log.error("Can't find keyword. - keywordId: {}", keywordId);
                throw new NullPointerException("Can't find keyword - keywordId: " + keywordId);
            }

            int affectedRow = this.keywordDao.deleteKeyword(keywordId);
            if (affectedRow == 0) {
                log.error("Can't remove keyword. - keywordId: " + keywordId);
                throw new IllegalArgumentException("Can't remove keyword - keywordId: " + keywordId);
            }
        }
    }
}
