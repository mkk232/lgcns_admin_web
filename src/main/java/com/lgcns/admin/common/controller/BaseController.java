package com.lgcns.admin.common.controller;

import com.lgcns.admin.common.vo.SessionVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BaseController {
    public static String getSessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String sessionId = null;
        String loginId = null;
        if(session != null) {
            sessionId = session.getId();
            SessionVO loginVO = (SessionVO) session.getAttribute("userInfo");
            if(loginVO != null) {
                loginId = loginVO.getUserLoginId();
            }
        }

        return sessionId + " - " + loginId;
    }
}
