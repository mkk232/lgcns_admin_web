<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .list-group-item.active {
        background-color: #007bffff;
        opacity: 0.8;
        color: white;
    }

    ul.list-group li.list-group-item {
        cursor: pointer;
    }

    ul.list-group li.list-group-item:hover:not(.active) {
        background-color: #88888833;
    }
</style>
<script type="text/javascript">
    $(document).ready(function() {
        reqAttachList();

        // 목록 버튼 클릭 이벤트
        $('button[name=btnList]').on('click', function() {
            window.location.href='/simulation/filter/search.html';
        })

        // 첨부파일 목록 클릭 이벤트
        $('ul.list-group').on('click', 'li', function() {
            if($(this).hasClass('active')) {
                return;
            }

            $('ul.list-group').find('li').removeClass('active');
            $(this).addClass('active');

            let mailId = $('#mailId').val();
            let params = {
                mailId: mailId
            }

            let attachId = $(this).data('attach-id');
            if (attachId !== 'em_body') {
                // 메일 본문이 아닌 경우
                params.attachId = attachId;
            } else {
                params.attachId = "";
            }

            let encodedParam = new URLSearchParams(params);

            let callUrl = '/simulation/filter/search/detail?' + encodedParam.toString();
            callAjax(callUrl, 'GET', null,
                function(data) {
                    console.log(data.result);
                    drawContents(data.result);
                    drawFilteringKeyword(data.result);
                },
                function (xhr) {
                    console.log(xhr);
                }
            )

        })

        function reqAttachList() {
            let mailId = $('#mailId').val();

            // URL 인코딩 적용
            let encodedParam = new URLSearchParams({
                mailId: mailId,
            });

            let callUrl = '/simulation/filter/search/attachList?' + encodedParam.toString();
            callAjax(callUrl, 'GET', null,
                function(data) {
                    console.log(data.result);
                    $('ul.list-group').find('li').removeClass('active');

                    drawAttachList(data.result);
                    drawContents(data.result);
                    drawFilteringKeyword(data.result);
                },
                function (xhr) {
                    console.log(xhr);
                }
            )
        }

        function drawAttachList(data) {
            let target = $('ul.list-group');
            target.empty();

            let source;
            $.each(data.hits.hits, function(index, attach) {
                source = attach._source;
                if(source.attach_exist === 'N') {
                    // 본문
                    target.prepend(
                        $('<li class="list-group-item">')
                            .text('메일 본문')
                            .attr('data-attach-id', 'em_body')
                    )
                } else {
                    // 첨부
                    target.append(
                        $('<li class="list-group-item">')
                            .html(source.attach_name)
                            .attr('data-attach-id', source.attach_id)
                    )
                }

            })

            target.find('li:first').addClass('active');

        }

        function drawContents(data) {
            // 데이터가 없을 경우
            if(data.hits.hits.length === 0) {
                return;
            }

            let currentLiId = $('ul.list-group').find('li.active').data('attach-id');
            let targetContents = $('#doc-content').empty();
            let targetTitle = $('#doc-title').empty();

            let source = data.hits.hits[0]._source;
            let title = currentLiId === source.attach_id && source.attach_exist === 'Y' ? source.attach_name : source.subject;
            let body = currentLiId === source.attach_id && source.attach_exist === 'Y' ? source.attach_body : source.em_body;

            // 메일 본문 우선 출력
            targetContents.append(
                $('<p>').html(body)
            )

            targetTitle.append(
                $('<h4>').html(title)
            )
        }

        function drawFilteringKeyword(data) {
            let target = $('#filteringKeyword');
            target.empty();

            // 현재 active된 li의 data-attach-id
            let currentAttachId = $('ul.list-group li.active').data('attach-id');
            let hitMatch = null;

            if(data.hits.hits.length > 0) {
                $.each(data.hits.hits, function (index, hit) {
                    if(hit._source.attach_exist === 'Y') {
                        // 첨부
                        // 현재 선택된 첨부파일의 본문을 찾는다.
                        if (hit._source.attach_id === currentAttachId) {
                            hitMatch = hit;
                            return false; // 일치 항목이 발견되면 루프 종료
                        }
                    } else {
                        // 본문
                        hitMatch = null;
                    }

                });

                if (hitMatch) {
                    // 첨부
                    target.text('필터링 키워드 : ' + hitMatch.filteringKeywords.join(', '));
                } else {
                    // 본문
                    $.each(data.hits.hits, function (index, hit) {
                        if (hit._source.attach_exist === 'N') {
                            target.text('필터링 키워드 : ' + hit.filteringKeywords.join(', '));
                            return false; // 일치 항목이 발견되면 루프 종료
                        }
                    });
                }
            }
        }
    })

</script>
<div  class="vstack gap-3 m-3">
    <div class="row">
        <input type="hidden" id="mailId" value="${mailId}" />
        <!-- 문서 리스트 -->
        <div class="col-md-3 border-end" style="height: calc(100vh - 10rem); overflow-y: auto;">
            <h6 class="mb-3">첨부파일 목록</h6>
            <ul class="list-group" id="document-list">
            </ul>
        </div>

        <!-- 상세 보기 -->
        <div class="col-md-8 vstack gap-2">
            <div class="border-bottom">
                <p id="filteringKeyword" class="mb-3">필터링 키워드 :</p>
            </div>
            <div style="padding: .5rem;">
                <h4 id="doc-title"></h4>
                <p id="doc-content"></p>
            </div>
        </div>
    </div>

    <div class="col-12 mt-3 btn-toolbar">
        <div class="btn-group me-3">
            <button type="button" class="btn btn-primary" name="btnList">
                <i class="bi bi-list-ul"></i> 목록
            </button>
        </div>
    </div>
</div>