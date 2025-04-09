<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">
    $(document).ready(function() {
        // Bootstrap - SweetAlert 모달 키 충돌 방지 설정
        setModalKeyEvent($('#dlgKeyword'));

        let sortingPlugIn = $('.table').sortingPlugIn({
            sortColumnId: 1,
            sortDirection: 'DESC',
            __onClickHandler: reqKeywordList
        })

        $('.page_pageSize').pageSizePlugin({
            __onChangeHandler: reqKeywordList
        })

        reqKeywordList();

        ////////////////// 이벤트 시작 /////////////////

        // 테이블 리스트가 없을 경우
        if($('.table tbody tr').length === 0) {
            drawEmptyTable($('.table'))
        }

        // 키워드 검색 이벤트 - 엔터
        $('#searchKeyword').on('keyup', function(key) {
            if(key.keyCode === 13) {
                reqKeywordList();
            }
        })

        // 키워드 검색 이벤트 - 버튼 클릭
        $('button[name=btnSearch]').on('click', function() {
            reqKeywordList();
        })

        // 체크박스 클릭 이벤트
        $('.table').on('click', 'input[name=checkKeyword]', function(e) {
            const target = e.target; // 선택된 체크박스
            if($(target).data('id') == 'ALL') { // 선택된 체크박스가 전체선택 체크박스 일 경우
                if($(target).is(':checked')) {
                    $('input[name="checkKeyword"]').prop('checked',true);
                } else {
                    $('input[name="checkKeyword"]').prop('checked',false);
                }

            } else {
                let normalCheckCnt = 0; // 선택된 체크박스 수
                let totalCheckCnt = 0; // 모든 체크박스 수

                $('input[name="checkKeyword"][data-id!="ALL"]').each(function(i, data) {
                    if($(data).is(':checked')) {
                        normalCheckCnt++;
                    }

                    totalCheckCnt++;
                });

                if(normalCheckCnt === totalCheckCnt) {
                    $('input[name="checkKeyword"][data-id="ALL"]').prop('checked',true);
                } else {
                    $('input[name="checkKeyword"][data-id="ALL"]').prop('checked',false);
                }
            }

            // 삭제 버튼 활성화/비활성화
            if($('input[name="checkKeyword"][data-id!=ALL]:checked').is(':checked')) {
                $('button[name="btnRemove"]').prop('disabled', false);
            } else {
                $('button[name="btnRemove"]').prop('disabled', true);
            }
        });

        // 키워드 추가 이벤트 - 클릭
        $('button[name=btnSave]').on('click', function() {
            if($('#keywordInput').val().trim().length === 0) {
                alertEx('warning', '키워드를 입력해 주세요.');
                return;
            }

            // 유효성 검사
            $('form[name="frmKeyword"]').removeClass('was-validated').addClass('was-validated');
            if(!$('form[name="frmKeyword"]')[0].checkValidity()) {
                return false;
            }

            reqSaveKeyword($(this).attr('data-button-mode'));
        })

        // 키워드 수정 버튼 클릭
        $('.table tbody').on('click', 'button[name=btnEdit]', function() {
            $('#dlgKeyword .modal-title').html('<i class="bi bi-pencil-square"></i> 키워드 수정');
            let tr = $(this).closest('tr');
            $('#keywordInput').val(tr.find('pre').text());
            $('input[name=keywordId]').val(tr.data('keywordid'));

            $('button[name=btnSave]').attr('data-button-mode', 'modify');
        })

        // 키워드 삭제 버튼 클릭
        $('button[name=btnRemove]').on('click', function() {
            confirmEx('선택된 키워드를 삭제하시겠습니까?', function() {
                let callUrl = '/dictionary/keyword/remove';
                let sendData = {};
                let keywords = [];
                $.each($('input[name=checkKeyword][data-id!=ALL]:checked'), function(index, data) {
                    keywords.push($(data).closest('tr').data('keywordid'));
                });
                sendData['keywordIdList'] = keywords;

                console.log(sendData);

                callAjaxJson(callUrl, 'POST', sendData,
                    function(data) {
                        $('button[name=btnRemove]').prop('disabled', true);
                        $('input[name=checkKeyword]').prop('checked', false);
                        alertToast('success', '삭제되었습니다.');
                        reqKeywordList();
                    }, function(xhr) {
                        console.log(xhr);
                        alertEx('error', '키워드 삭제 작업 중 오류가 발생하였습니다.');
                    }
                )
            })
        })

        // modal - 초기화
        $('#dlgKeyword').on('show.bs.modal', function() {
            $('#dlgKeyword .modal-title').html('<i class="bi bi-pencil-square"></i> 키워드 추가');
            $('#keywordInput').val('');
            $('#keywordInput').focus();
            $('form[name="frmKeyword"]').removeClass('was-validated');
            $('button[name=btnSave]').attr('data-button-mode', 'regist');
        })

        // modal - 포커스 처리
        $('#dlgKeyword').on('hide.bs.modal', function() {
            disableFocus($('#dlgKeyword'));
        })

        ////////////////// 이벤트 종료 /////////////////

        ////////////////// 함수 시작 //////////////////
        function reqKeywordList(pageNo) {
            let sendData = {};
            if(pageNo === undefined) {
                pageNo = 1;
            }

            let callUrl = '/dictionary/keyword/list';

            let keyword = $('#searchKeyword').val();
            if(keyword !== undefined) {
                sendData['searchKeyword'] = $('#searchKeyword').val();
            }

            let sortInfo = sortingPlugIn.getSortInfo();
            sendData['sortColumnId'] = sortInfo.sortColumnId;
            sendData['sortDirection'] = sortInfo.sortDirection;

            sendData['pageNo'] = pageNo;

            // pageSize
            if($('.page_pageSize ul li a').hasClass('active')) {
                sendData['pageSize'] = parseInt($($('.page_pageSize ul li a.active')[0]).text());
            }

            callAjax(callUrl, 'GET', sendData,
                function(data) {
                    let target = $('.table tbody');
                    drawKeywordList(data.result, target);

                    $('input[name=checkKeyword]').prop('checked', false);
                }, function(data) {
                    alertEx('error', '키워드 목록을 가져오는 중 오류가 발생하였습니다.');
                }
            )
        }

        function reqSaveKeyword(btnMode) {
            let sendData = {};
            let successMsg;
            let callUrl;
            if(btnMode === 'regist') {
                callUrl = '/dictionary/keyword/regist';
                successMsg = '키워드가 등록되었습니다.';
            } else {
                callUrl = '/dictionary/keyword/modify';
                sendData['keywordId'] = $('input[name=keywordId]').val();
                successMsg = '키워드가 수정되었습니다.';
            }

            sendData['keyword'] = $('#keywordInput').val();

            console.log(sendData);

            callAjaxJson(callUrl, 'POST', sendData,
                function(data) {
                    if(data.error !== 0) {
                        if(data.error === 409) {
                            alertEx('error', '이미 등록된 키워드입니다.');
                        }
                    } else {
                        reqKeywordList();
                        $('#dlgKeyword').modal('hide');
                        alertToast('success', successMsg);
                    }
                }, function(data) {
                    console.log(data);
                    alertEx('error', '알 수 없는 이유로 실패하였습니다.');
                }
            )


        }

        function drawKeywordList(data, target) {
            target.empty();

            if (data.result.length > 0) {
                $.each(data.result, function (index, data) {
                    target.append(
                        $('<tr />')
                            .attr('data-keywordid', data.keywordId)
                            .append(
                                $('<td />')
                                    .addClass('text-center')
                                    .append(
                                        $('<input />')
                                            .attr('type', 'checkbox')
                                            .attr('name', 'checkKeyword')
                                            .addClass('form-check-input')
                                    )
                            )
                            .append(
                                $('<td />')
                                    .append(
                                        $('<button type="button" />')
                                            .addClass('focus-ring focus-ring-light btn btn-link')
                                            .attr('name', 'btnEdit')
                                            .attr('data-bs-toggle', 'modal')
                                            .attr('data-bs-target', '#dlgKeyword')
                                            .append(
                                                $('<pre />').text(data.keyword)
                                                    .append(
                                                        $('<i class="bi bi-pencil-square"></i>')
                                                    )
                                                    .css('font-family', 'inherit')
                                                    .css('font-size', 'inherit')
                                                    .css('font-weight', 'inherit')
                                                    .css('margin-bottom', '0')
                                            )
                                    )
                            )
                            .append(
                                $('<td />').text(formatDateString(data.regDt))
                            )
                            .append(
                                $('<td />').text(data.regUser)
                            )
                    );
                });
            } else {
                drawEmptyTable($('.table'));
            }

            $('ul.pagination').pagingPlugin({
                pageNo: data.page.pageNo,
                pageSize: data.page.pageSize,
                totalCnt: data.page.totalCnt,
                totalCntSelector: '.page_totalCnt',
                __onClickHandler: reqKeywordList
            });

        }

        ////////////////// 함수 종료 //////////////////
    })
</script>

<div class="modal fade" id="dlgKeyword" tabindex="-1" aria-labelledby="dlgKeyword" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <input type="hidden" name="keywordId" data-keywordid="">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">
                    <i class="bi bi-file-earmark-plus"></i> 키워드 추가
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form name="frmKeyword" class="" onsubmit="return false;">
                    <div class="mb-3 mb-5">
                        <label for="keywordInput" class="form-label">키워드</label>
                        <input type="text" id="keywordInput" class="form-control" placeholder="키워드를 입력하세요." autocomplete="off" required maxlength="30" pattern="[^`~!@#$%^&*\(\)\[\]\{\}\-_+=:;,.<>\/\?\\\|&apos;&quot;]+">
                        <div class="invalid-feedback">필수 항목, 최대 30자, 특수문자 제외</div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" name="btnSave" class="btn btn-primary" data-button-mode="regist">
                    <i class="bi bi-floppy"></i> 저장
                </button>
                <button type="button" name="btnClose" class="btn btn-secondary" data-bs-dismiss="modal">
                    <i class="bi bi-x-lg"></i> 닫기
                </button>
            </div>
        </div>
    </div>
</div>

<div class="vstack gap-3 m-3">
    <div class="mt-2">
        <h4 class="fw-bold">
            <i class="bi bi-shield-lock-fill"></i>
            필터링 사전
        </h4>
    </div>
    <div class="h-25"></div>
    <div class="row">
        <div class="col-12">
            <div class="row justify-content-center">
                <div class="col-6">
                    <div class="input-group">
                        <input id="searchKeyword" type="text" class="form-control" placeholder="검색할 단어를 입력하세요." aria-label="Recipient's username" aria-describedby="button-addon2" autocomplete="off">
                        <button class="btn btn-primary" type="button" name="btnSearch">
                            <i class="bi bi-search"></i>
                        </button>
                    </div>
                </div>
            </div>

            <div class="col-12 mt-3 btn-toolbar d-flex justify-content-between">
                <div class="btn-group me-2">
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#dlgKeyword">
                        <i class="bi bi-file-earmark-plus"></i> 추가
                    </button>
                    <button class="btn btn-danger" type="button" id="btnRemove" name="btnRemove" disabled>
                        <i class="bi bi-trash3"></i> 삭제
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="col-12">
        <table class="table table-bordered text-break align-middle table-sm" style="table-layout: fixed;">
            <colgroup>
                <col style="width: 5%;">
                <col style="width: 40%;">
                <col style="width: 15%;">
                <col style="width: 10%;">
            </colgroup>
            <thead>
            <tr class="text-center">
                <th data-sortable="0">
                    <input class="form-check-input" type="checkbox" data-id="ALL" id="keywordCheckAll" name="checkKeyword">
                </th>
                <th data-sort-columnid="2">키워드</th>
                <th data-sort-columnid="1">등록일</th>
                <th>등록자</th>
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