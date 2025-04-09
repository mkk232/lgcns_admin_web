/*
 * 파일이름: jquery-rayful-pageSize.js
 * 설명: 한 페이지에 보여줄 갯수를 설정하는 control 관련 jquery custom plugin
 * 주의사항: jquery.min.js include 후 이 파일을 include
 * 		   Bootstrap 디자인을 이용한 태그 생성
 */
(function($) {

	$.extend($.fn, {
		
		pageSizePlugin: function(options) {
			
			let baseTarget = $(this);
			
			// 파라미터 셋팅
			let settings = $.extend({
				listItems: [10, 30, 50, 100],
				defaultItem: 10,
				__onChangeHandler : null
			}, options)
			
			const pluginObj = {
				getPageSize: __getPageSize
			}
			
			drawControl();
			bindEvent();
			
			// Event 처리
			function bindEvent() {
				baseTarget.off().on('click', 'ul.dropdown-menu li a', function() {
					let selectedPageSize = $(this).text();
					baseTarget.find('button.dropdown-toggle').text('데이터 수 : ' + selectedPageSize);
					baseTarget.find('ul.dropdown-menu li a').removeClass('active');
					baseTarget.find('ul.dropdown-menu li a').filter(function () {
						return $(this).text() == selectedPageSize;
					}).addClass('active');
					
					if(settings.__onChangeHandler != null) {
						settings.__onChangeHandler();
					}
				})
			}
			
			// 컨트롤 그리기
			function drawControl() {
				baseTarget.empty();
				baseTarget.append(
					$('<div class="dropdown d-inline-block" />')
						.append(
							$('<button class="btn btn-white dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false" />')
								.text('데이터 수 : ' + settings.defaultItem)
						)
						.append($('<ul class="dropdown-menu"/>'))
				);
				
				$.each(settings.listItems, function(index, data) {
					baseTarget.find('ul.dropdown-menu')
						.append(
							$('<li/>')
								.append(
									$('<a class="dropdown-item '+ (settings.listItems[index] == settings.defaultItem ? 'active' : '')  +' " href="#" />')
										.text(data)
								)
						)
				});
			}
			
			function __getPageSize() {
				return parseInt($($('.page_pageSize ul li a.active')[0]).text())
			}
			return pluginObj;
		}
	});

})(jQuery);


