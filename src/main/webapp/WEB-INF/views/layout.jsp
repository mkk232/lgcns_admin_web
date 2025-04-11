<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>LG PoC</title>

    <link rel="stylesheet" href="/css/lib/bootstrap.min.css">
    <link rel="stylesheet" href="/css/lib/bootstrap-select.min.css">
    <link rel="stylesheet" href="/css/lib/bootstrap-icons.css">
    <link rel="stylesheet" href="/css/lib/bootstrap-datepicker.min.css">
    <link rel="stylesheet" href="/css/lib/c3.min.css"/>
    <!-- 사이트 css -->
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>

    <script src="/js/lib/jquery.min.3.7.1.js"></script>
    <script src="/js/lib/jquery.fileDownload.1.4.2.js"></script>
    <script src="/js/lib/popper.min.js"></script>
    <script src="/js/lib/bootstrap.bundle.min.5.3.2.js"></script>
    <script src="/js/lib/sweetalert2.11.js"></script>

    <!-- 사이트 라이브러리 스크립트 -->
    <script type="text/javascript" src="/js/jquery-rayful-sorting.js"></script>
    <script type="text/javascript" src="/js/jquery-rayful-paging.js"></script>
    <script type="text/javascript" src="/js/jquery-rayful-pageSize.js"></script>
    <script type="text/javascript" src="/js/jquery-rayful-ajax.js"></script>
    <script type="text/javascript" src="/js/jquery-rayful-schedule.js"></script>
    <script type="text/javascript" src="/js/common-rayful-dialog.js"></script>

    <script src="/js/lib/bootstrap-datepicker.min.js"></script>
    <script src="/js/lib/d3.v3.min.js"></script>
    <script src="/js/lib/c3.min.js"></script>


    <!--  일반적인 공틍 스크립트 함수 모음 -->
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript" src="/js/policy.js"></script>
    <script type="text/javascript" src="/js/ms365.js"></script>

    <%-- Fonts --%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">


    <style>
        body {
            font-family: "Noto Sans KR", sans-serif;
            font-optical-sizing: auto;
            font-style: normal;
        }

        header, main {
            min-width: 1200px;
        }

        .sidebar {
            position: sticky;
            top: 4rem;
            z-index: 1000;
            min-width: 280px;
            max-width: 280px;
            height: calc(100vh - 4rem);
        }

        .sidebar .sidebar-body {
            position: sticky;
            height: auto;
            overflow-y: auto;
        }

        .sidebar .sidebar-body .section {
            margin-top: 10px;
            /*margin-left: 10px;*/
        }

        .sidebar .sidebar-body ul {
            margin-top: 5px;
            margin-left: 0px;
            padding-left: 1rem;
            list-style: none;
        }

        .table tr.active td {
            background-color: var(--bs-primary-bg-subtle)
        }

    </style>
    <script>

    </script>
</head>
<body>
<div id='my-spinner' style="display: none;">
    <div>
        <span class="loader"> </span>
    </div>
</div>
<jsp:include page="header.jsp" />
<main>
    <div class="container-fluid">
        <div class="row">
            <jsp:include page="sidebar.jsp" />

            <!-- ////////////////////////////////////// Content start //////////////////////////////////////-->
            <div class="col" id="contents"<%-- style="max-width: 85%;"--%>>
                <c:if test="${not empty viewName}">
                    <jsp:include page="${viewName}.jsp" />
                </c:if>
            </div>
            <!-- ////////////////////////////////////// Content end //////////////////////////////////////-->

        </div>
    </div>
</main>
<!-- ////////////////////////////////////// Header navigation bar end //////////////////////////////////////-->
</body>
</html>