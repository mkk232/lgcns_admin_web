<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!-- ////////////////////////////////////// Side Bar start //////////////////////////////////////-->
<div id="contentsSidebar" class="col sidebar shadow">
    <div class="sidebar-body">
        <ul data-menu-id="0"></ul>
        <div class="section">
            <a class="link-secondary link-underline link-underline-opacity-0">
                <i class="bi bi-database-fill-add"></i>
                사전
            </a>
            <ul class="">
                <li class="mt-2">
                    <a href="/dictionary/keyword/list.html"
                       class="link-dark link-underline link-underline-opacity-0 link-underline-opacity-75-hover">
                        <i class="bi bi-shield-lock-fill"></i>
                        필터링 사전
                    </a>
                </li>
            </ul>
        </div>
        <div class="section">
            <a class="link-secondary link-underline link-underline-opacity-0">
                <i class="bi bi-question-circle"></i>
                시뮬레이션
            </a>
            <ul class="">
                <li class="mt-2">
                    <a href="/simulation/filter/search.html"
                       data-url="/setting/admin/list.do"
                       class="link-dark link-underline link-underline-opacity-0 link-underline-opacity-75-hover">
                        <i class="bi bi-envelope-at"></i>
                        필터링 검색
                    </a>
                </li>
                <li class="mt-2">
                    <a href="/simulation/search.html"
                       data-url="/setting/system/list.do"
                       class="link-dark link-underline link-underline-opacity-0 link-underline-opacity-75-hover">
                        <i class="bi bi-search"></i>
                        일반 검색
                    </a>
                </li>
            </ul>
        </div>

    </div>
</div>
<!-- ////////////////////////////////////// Side Bar end //////////////////////////////////////-->