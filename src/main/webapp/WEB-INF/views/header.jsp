<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    $(document).ready(function() {
        // 메뉴 토클 아이콘 클릭 이멘트 처리
        $('.menu-togglee').on('click', function() {
            $('#contentsSidebar').toggle('slide')
        });
    })

</script>

<!-- ////////////////////////////////////// Header navigation bar start //////////////////////////////////////-->
<header class="sticky-top bg-white">
    <nav class="navbar navbar-expand-lg shadow-sm rounded">
        <div class="container-fluid">
            <div class="bg-white d-flex">
                <a class="navbar-brand menu-togglee btn btn-outline-light btn-sm" href="#">
                    <img width="28px" height="28px" src="/image/list.svg">
                </a>
                <a class="navbar-brand brand fw-bold m-0" href="/"> </a>
            </div>
            <div class="" id="navbarNavDropdown" style="display: inline-flex; height: 44px;" >
                <ul class="navbar-nav ms-5" style="display: inline-block;">
                    <li class="nav-item">
                    </li>
                    <%--<li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <c:out value="${sessionScope.userInfo.userName}"></c:out>
                            <img src="/image/person-circle.svg" width="28" height="28" />
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="/user/myInfo.html">Settings</a></li>
                            <li><a class="dropdown-item" href="/auth/logout">Logout</a></li>
                        </ul>
                    </li>--%>
                </ul>
            </div>
        </div>
    </nav>
</header>
<!-- ////////////////////////////////////// Header navigation bar end //////////////////////////////////////-->