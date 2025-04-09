<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">
    $(document).ready(function() {
        drawEmptyTable($('.table'), '검색할 단어를 입력하세요.');

        $('.page_pageSize').pageSizePlugin({
            __onChangeHandler: reqSearchList
        })

        // 검색 엔터 이벤트
        $('#searchKeyword').on('keyup', function(key) {
            if(key.keyCode == 13) {
                reqSearchList();
            }
        })

        // 검색 버튼 클릭 이벤트
        $('button[name=btnSearch]').on('click', function() {
            reqSearchList();
        })

        function reqSearchList(pageNo) {
            let sendData = {};
            if(pageNo === undefined) {
                pageNo = 1;
            }

            let callUrl = '/simulation/search';

            sendData['pageNo'] = pageNo;
            sendData['searchType'] = $('select[name=searchType]').val();
            sendData['searchKeyword'] = $('#searchKeyword').val();

            // pageSize
            if($('.page_pageSize ul li a').hasClass('active')) {
                sendData['pageSize'] = parseInt($($('.page_pageSize ul li a.active')[0]).text());
            }

            console.log(sendData);

            callAjax(callUrl, 'GET', sendData,
                function(data) {
                    console.log(data);
                    if(data.error == 0) {
                        drawSearchList($('.table tbody'), data.result);
                    } else {
                        drawEmptyTable($('.table tbody'), data.msg);
                    }
                }
            )
        }

        function drawSearchList(target, data) {
            target.empty();

            let dataList = data.result.hits.hits;
            if(dataList.length != 0) {
                $.each(dataList, function(index, data) {
                    let source = data._source;
                    target.append(
                        $('<tr>').append(
                            $('<td>').html(source.subject),
                            $('<td>').html(source.em_body),
                            $('<td>').html(source.attach_name),
                            $('<td>').html(source.attach_body),
                            $('<td>').html(source.sender)
                        )
                    )
                });
            } else {
                drawEmptyTable($('.table'), '검색 결과가 없습니다.');
            }

            $('ul.pagination').pagingPlugin({
                pageNo: data.page.pageNo,
                pageSize: data.page.pageSize,
                totalCnt: data.page.totalCnt,
                totalCntSelector: '.page_totalCnt',
                __onClickHandler: reqSearchList
            });
        }
    })

</script>
<div class="vstack gap-3 m-3">
    <div class="mt-2">
        <h4 class="fw-bold">
            <i class="bi bi-search"></i>
            일반 검색
        </h4>
    </div>

    <div class="col-12">
        <div class="row justify-content-center">
            <div class="col-6">
                <div class="input-group">
                    <select class="form-select col-2" name="searchType" style="max-width: 160px;">
                        <option value="all" selected>전체</option>
                        <option value="subject">메일 제목</option>
                        <option value="body">메일 본문</option>
                        <option value="attachName">첨부파일 명</option>
                        <option value="attachBody">첨부파일 내용</option>
                        <option value="sender">보낸 사람</option>
                    </select>
                    <input id="searchKeyword" type="text" class="form-control" placeholder="검색할 단어를 입력하세요." aria-label="Recipient's username" aria-describedby="button-addon2" autocomplete="off">
                    <button class="btn btn-primary" type="button" name="btnSearch">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div class="form-text"></div>
    <div>
        <div class="col-12 mt-3">
            <table class="table table-bordered text-break align-middle" style="table-layout: fixed;">
                <colgroup>
                    <col style="width: 5%;">
                    <col style="width: 8%;">
                    <col style="width: 5%;">
                    <col style="width: 8%;">
                    <col style="width: 5%;">
                </colgroup>
                <thead>
                <tr class="text-center">
                    <th>메일 제목</th>
                    <th>메일 본문</th>
                    <th>첨부파일 제목</th>
                    <th>첨부파일 내용</th>
                    <th>보낸 사람</th>
                </tr>
                </thead>
                <tbody class="table-group-divider">
                </tbody>

            </table>
        </div>
        <div class="col-12">
            <nav class="page">
                <!--  -->
                <div class="page_pageSize"></div>

                <!--  -->
                <ul class="pagination justify-content-center"></ul>

                <!--  -->
                <div class="page_totalCnt"></div>
            </nav>
        </div>
    </div>
</div>