<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
    div.displayDiv {
        min-height: 38px;
    }
</style>

<script type="text/javascript">
    $(document).ready(function() {
        drawEmptyTable($('.table'), '필터링 검색 결과가 없습니다.');

        $('.page_pageSize').pageSizePlugin({
            __onChangeHandler: reqSearchList
        })

        reqSearchList();

        function reqSearchList(pageNo) {
            let sendData = {};
            if(pageNo === undefined) {
                pageNo = 1;
            }

            let callUrl = '/simulation/filter/search';

            sendData['pageNo'] = pageNo;

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
                        $('<tr>')
                            .attr('data-mailid', source.em_id)
                            .append(
                                $('<td>').html(source.subject),
                                $('<td>').text(source.sender),
                                $('<td>').html(formatDateString(source.senddtm)),
                                $('<td>').append(
                                    $('<a href="/simulation/filter/search/attachList.html?mailId=' + source.em_id + '">')
                                        .text('상세보기')
                                )
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

<div class="modal fade" id="dlgSearchDetail" tabindex="-1" aria-labelledby="dlgSearchDetail" aria-hidden="true">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">
                     결과 상세보기
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row mb-3">
                    <label class="col-sm-2 col-form-label">필터링 키워드</label>
                    <div class="col-sm-10">
                        <%--<input type="text" class="form-control" id="subjectInput">--%>
                        <div id="filterKeywordInput" class="form-control displayDiv"></div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label class="col-sm-2 col-form-label">메일 제목</label>
                    <div class="col-sm-10">
                        <%--<input type="text" class="form-control" id="subjectInput">--%>
                        <div id="subjectInput" class="form-control displayDiv"></div>
                    </div>
                </div>
                <div class="row mb-3" style="overflow-y: scroll">
                    <label class="col-sm-2 col-form-label">메일 내용</label>
                    <div class="col-sm-10">
                        <%--<textarea class="form-control" id="emBodyText" readonly></textarea>--%>
                        <div id="emBodyText" class="form-control displayDiv" style="max-height: 600px; "></div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label class="col-sm-2 col-form-label">첨부파일 명</label>
                    <div class="col-sm-10">
                        <%--<input type="text" class="form-control" id="attachNameInput" readonly>--%>
                        <div id="attachNameInput" class="form-control displayDiv"></div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label class="col-sm-2 col-form-label">첨부파일 내용</label>
                    <div class="col-sm-10">
                        <%--<textarea class="form-control" id="attachBodyText" readonly></textarea>--%>
                        <div id="attachBodyText" class="form-control displayDiv"></div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label class="col-sm-2 col-form-label">보낸 사람</label>
                    <div class="col-sm-10">
                        <%--<textarea class="form-control" id="attachBodyText" readonly></textarea>--%>
                        <div id="senderInput" class="form-control displayDiv"></div>
                    </div>
                </div>
                <div class="row mb-3">
                    <label class="col-sm-2 col-form-label">발송 시간</label>
                    <div class="col-sm-10">
                        <%--<textarea class="form-control" id="attachBodyText" readonly></textarea>--%>
                        <div id="sendDtmInput" class="form-control displayDiv"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" name="btnClose" class="btn btn-secondary" data-bs-dismiss="modal">
                    <i class="bi bi-x-lg"></i> 닫기
                </button>
            </div>
        </div>
    </div>
</div>

<div class="vstack gap-2 m-3">
    <div class="mt-2">
        <h4 class="fw-bold">
            <i class="bi bi-envelope-at"></i>
            필터링 검색
        </h4>
    </div>

    <div class="form-text">필터링 검색은 사전에 등록된 필터링 키워드로 검색된 결과를 나타냅니다.</div>
    <div>
        <div class="col-12 mt-3">
            <table class="table table-bordered text-break align-middle" style="table-layout: fixed;">
                <colgroup>
                    <col style="width: 10%;">
                    <col style="width: 4%;">
                    <col style="width: 4%;">
                    <col style="width: 2%;">
                </colgroup>
                <thead>
                <tr class="text-center">
                    <th>메일 제목</th>
                    <th>보낸 사람</th>
                    <th>보낸 시간</th>
                    <th></th>
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