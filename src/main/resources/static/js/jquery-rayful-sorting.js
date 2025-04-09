/*
 * 파일이름: jquery-rayful-sorting.js
 * 설명: 테이블 제목 영역에 sorting 정보를 보여주고 사용자의 이벤트를 처리하는 jquery custom plugin 
 *      테이블 sorting 처리가 필요한 column의 th에 data-sort-columnid(sort 컬럼 정보) 속성을 설정해야 함.
 * 주의사항: jquery.min.js include 후 이 파일을 include
 * 		   css에 sorting, sorting_dw, sorting_up 관련 css 설정이 필요함.
 */

(function($) {

	$.extend($.fn, {
		
		sortingPlugIn: function(options) {
			
			var baseTarget = $(this);
			
			// 파라미터 셋팅
			var settings = $.extend({
				sortColumnId: null,
				sortDirection: 'ASC',
				__onClickHandler : null,	// 
			}, options)
			
			const pluginObj = {
				getSortInfo: __getSortInfo
			}
			
			if(settings.sortColumnId != null) {
				drawDefaultSort();
			} 
			
			if(settings.__onClickHandler != null) {
				bindEvent();
			}
			
			// Event 처리
			function bindEvent() {
				baseTarget.off().on('click', 'th[data-sort-columnid]', function(e) {
					// let sortColumnId = null;
					// let sortDirection = null;
					
					// sortColumnId = $(this).attr('data-sort-columnid');
					
					if($(this).hasClass('sorting_up')) {
						// sortDirection = 'DESC';
						$(this).removeClass('sorting_up').addClass('sorting_dw');
					} else {
						// sortDirection = 'ASC';
						baseTarget.find('th[data-sort-columnid]').removeClass('sorting_dw').removeClass('sorting_up');
						$(this).addClass('sorting_up');
					}
					
					e.stopPropagation();
					
					if(settings.__onClickHandler != null) {
						settings.__onClickHandler();
					}
				});
			}
			
			// Default sorting 정보 화면 출력
			function drawDefaultSort() {
				let sortColumnId = settings.sortColumnId;
				let sortDirection = settings.sortDirection;
				$.each(baseTarget.find('thead th[data-sort-columnid]'), function(index, item) {
					if($(item).find('span.sorting').length == 0) {
						$(item).append($('<span class="sorting"/>'));
					}
				});
				
				if(sortColumnId != null) {
					let defaultSortTh = baseTarget.find('thead th[data-sort-columnid='+sortColumnId+']');
					if(baseTarget.find(defaultSortTh.length > 0)) {
						if(sortDirection == 'DESC') {
							defaultSortTh.addClass('sorting_dw');
						} else {
							defaultSortTh.addClass('sorting_up');	
						}
					} else {
						console.log('invalid sortIndex');
					}
				}
			}
			function __getSortInfo() {
				let sortColumnId = null;
				let sortDirection = null;
				let sortEle = baseTarget.find('th[data-sort-columnid].sorting_up, th[data-sort-columnid].sorting_dw');
				if(sortEle.length > 0) {
					sortColumnId = sortEle.attr('data-sort-columnid');
					if(sortEle.hasClass('sorting_up')) {
						sortDirection = 'ASC';
					} else {
						sortDirection = 'DESC';
					}
				}
				
				return {
					sortColumnId: sortColumnId,
					sortDirection: sortDirection
				}
			}

			return pluginObj;
		}
		
		
	});

})(jQuery);


