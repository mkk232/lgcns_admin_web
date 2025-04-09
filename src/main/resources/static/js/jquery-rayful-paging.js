/*
 * 파일이름: jquery-rayful-paging.js
 * 설명: 페이징 정보를 보여주고 사용자의 이벤트를 처리하는 jquery custom plugin 
 * 주의사항: jquery.min.js include 후 이 파일을 include
 *         Bootstrap 디자인을 이용한 태그 생성
 */

(function($) {
	$.extend($.fn, {
		pagingPlugin: function(options) {
			
			let baseTarget = $(this);
			
			// 파라미터 셋팅
			let settings = $.extend({
				pageNo: null, // 현재 페이지
				pageSize: null, // maxRows
				totalCnt: 0,
				field: null,
				pageBlockSize: 10,
				showOnlyOnePage: true,
				totalCntSelector: null,
				__onClickHandler : null
			}, options)
			
			// 기존 Element 초기화
			baseTarget.empty();
			
			// 페이징 버튼 그리기 
			if(settings.showOnlyOnePage) {
				drawPagination();
			} else {
				if(settings.totalCnt > settings.pageSize) {
					drawPagination();
				}
			}
			
			// 이벤트 설정
			bindEvent();
			
			if(settings.totalCntSelector != null) {
				let totalCntEle = $(document).find(settings.totalCntSelector);
				totalCntEle.empty();
				totalCntEle.text('결과 수: ' + settings.totalCnt);
			}
			
			// 함수 - Event 처리
			function bindEvent() {
				baseTarget.off().on('click', 'li.page-item', function() {
					pageNo = $(this).find('a').attr('data-page-no');
					
					if(!$(this).children().hasClass('active')) {
						if(settings.__onClickHandler != null) {
							if(settings.field != null) {
								settings.__onClickHandler(pageNo, settings.field);
							} else {
								settings.__onClickHandler(pageNo);
							}
						}
					} 
				})
			}
			
			// 함수 - 페이징 그리기
			function drawPagination() {
				let selectedPage = settings.pageNo;
				let totalCnt = settings.totalCnt;
				let maxPageCnt = totalCnt / settings.pageSize < 1 ? 1 : Math.ceil(totalCnt / settings.pageSize);
				
				let pageBlockSize = settings.pageBlockSize;
				let pageBlockNo = Math.floor((selectedPage - 1) / pageBlockSize);
				let start = pageBlockNo * pageBlockSize + 1;
				let end = Math.min((start + pageBlockSize - 1), maxPageCnt);
				for (var index = start; index <= end; index++) {
					baseTarget.append(
						$('<li class="page-item"/>')
							.append(
								$('<a href="#" class="page-link"/>')
									.attr('data-page-no', index)
									.addClass((index == selectedPage ? 'active' : ''))
									.text(index)
							)
					);
			 	}
			 	
				if(maxPageCnt >= (pageBlockNo + 1) * pageBlockSize + 1) {											
					baseTarget.append(
						$('<li class="page-item"/>')
							.append(
								$('<a class="page-link" href="#"/>')
									.attr('data-page-no', (pageBlockNo + 1) * pageBlockSize + 1)
									.append(
										$('<span/>')
											.addClass('nextPage')
											.attr('aria-hidden', 'true')
											.html('&gt;')
									)
							)
					);
				}
				
				if(pageBlockNo > 0) {
					baseTarget.prepend(
						$('<li class="page-item"/>')
							.append(
								$('<a class="page-link" href="#"/>')
									.attr('data-page-no', (pageBlockNo - 1) * pageBlockSize + 1)
									.append(
										$('<span/>')
											.addClass('prevPage')
											.attr('aria-hidden', 'true')
											.html('&lt;')
									)
							)
					);
				}
			}
			return this;
		}
	});

})(jQuery);


