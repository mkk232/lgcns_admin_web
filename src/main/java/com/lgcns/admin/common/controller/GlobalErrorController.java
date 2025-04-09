package com.lgcns.admin.common.controller;

import com.lgcns.admin.common.vo.ResponseMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class GlobalErrorController {
    @RequestMapping(value = "/error/handling")
    public void handleJspErrorException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String session = BaseController.getSessionInfo(request);
        log.warn("[{}] JSP error handling", session);
        String msg = (String) request.getAttribute("msg");
        int status = (int) request.getAttribute("status");

        log.warn("[{}] send error({}, {})", session, status, msg);
        response.sendError(status, msg);
    }

    @RequestMapping(value = "/error/handling", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessageVO> handleJsonErrorException(HttpServletRequest request) {
        log.warn("[{}] JSON error handling", BaseController.getSessionInfo(request));
        String msg =  (String) request.getAttribute("msg");
        int status = (int) request.getAttribute("status");

        return ResponseEntity.status(status).body(new ResponseMessageVO(status, msg));
    }
}
