package com.lgcns.admin.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommonController {

    /**
     * 메인 페이지 이동
     */
    @GetMapping("/")
    public ModelAndView goMainPage() {
        return new ModelAndView("layout");
    }

/*    *//**
     * 로그인 페이지 이동
     *//*
    @GetMapping("/auth/login.html")
    public ModelAndView goLoginPage() {
        return new ModelAndView("login/auth");
    }*/
}
