/*
 * 파일이름: jquery-rayful-sorting.js
 * 설명: 테이블 제목 영역에 sorting 정보를 보여주고 사용자의 이벤트를 처리하는 jquery custom plugin 
 *      테이블 sorting 처리가 필요한 column의 th에 data-sort-columnid(sort 컬럼 정보) 속성을 설정해야 함.
 * 주의사항: jquery.min.js include 후 이 파일을 include
 * 		   css에 sorting, sorting_dw, sorting_up 관련 css 설정이 필요함.
 */

(function($) {

	$.extend($.fn, {
		
		schedulePlugIn: function(options) {
			
			var baseTarget = $(this);
			
			// 파라미터 셋팅
			var settings = $.extend({
				crontabVal: '0 0 1 * * ?',
				__switchFailEvent : null,
				editable: true
			}, options)
			
			const pluginObj = {
				getCronValue: __getCronValue
			}

			if(settings.editable) {
				// 폼 컨트롤 그리기
				drawEditControl();

				initEditControl(settings.crontabVal.split(' '));

				// 이벤트
				bindEvent();
			} else {
				// view
				drawViewControl();
			}


			// Event 처리
			function bindEvent() {
				$('#inputFrequency').off().on('change', function() {
					let frequency = $(this).val();
					showHideScheduleControl(frequency);
				});

				$('#switchControl').off().on('click', function() {
					if($('#inputFrequency').closest('div.c_schedule').is(':visible')) {
						// create cron expression 링크를 클릭했을 때 (Crontab 형식으로 변경)
						$('#inputCrontab').val(__getCronValue());

						initEditControl($('#inputCrontab').val().split(' '), false)
					} else {
						// create basic expression 링크를 클릭했을 때 (UI 형식으로 변경)
						let returnVal = initEditControl($('#inputCrontab').val().split(' '), true)
						if(returnVal === 2) {
							if(settings.__switchFailEvent != null) {
								settings.__switchFailEvent();
							}
						}
					}

					return false;
				})
			}

			// frequency에 따라 폼 컨트롤 조절
			function showHideScheduleControl(frequency) {
				$('.c_schedule').hide();
				$('#inputFrequency').closest('.c_schedule').show();
				$('.' + frequency).show();
			}

			function createSelectElement(id, optionValueArr, optionTextArr) {
				let selectEle;
				selectEle = $('<select />')
					.addClass('form-select')
					.attr('id', id);

				for(let i = 0; i < optionTextArr.length; i++) {
					selectEle.append(
						$('<option />')
							.val(optionValueArr[i])
							.text(optionTextArr[i])
					)
				}

				return selectEle;
			}

			function createSelectElement2(id, start, end, textSuffix) {
				let optionText;
				let selectEle;
				selectEle = $('<select />')
					.addClass('form-select')
					.attr('id', id);

				for(let i = start; i <= end; i++) {
					if(textSuffix !== undefined) {
						optionText = i + textSuffix;
					} else {
						optionText = i;
					}

					selectEle.append(
						$('<option />')
							.val(i)
							.text(optionText)
					)
				}

				return selectEle;
			}

			function drawEditControl() {
				let frequencyOptionTextArr = ['minute', 'hour', 'date', 'week', 'month', 'year'];
				let frequencyOptionValArr = ['c_minute', 'c_hour', 'c_date', 'c_week', 'c_month', 'c_year'];

				let id;
				let title;

				// draw frequency
				id = 'inputFrequency';
				title = 'Every';
				baseTarget.append(
					$('<div />')
						.addClass('col-12 mb-3 c_schedule')
						.append(
							$('<div />')
								.addClass('input-group ')
								.append(
									$('<label />')
										.attr('for', id)
										.addClass('input-group-text')
										.text(title)
								)
								.append(
									createSelectElement(id, frequencyOptionValArr, frequencyOptionTextArr)
								)
						)
				)

				// draw Month
				id = 'inputMonth';
				title = 'Month';
				baseTarget.append(
					$('<div />')
						.addClass('col-12 mb-3 c_schedule c_year')
						.append(
							$('<div />')
								.addClass('input-group ')
								.append(
									$('<label />')
										.attr('for', id)
										.addClass('input-group-text')
										.text(title)
								)
								.append(
									createSelectElement2(id, 1, 12, '월')
								)
						)
				)

				// draw Day
				let dayOptionValueArr = ['1', '2', '3', '4', '5', '6', '7'];
				let dayOptionTextArr = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
				id = 'inputDay';
				title = 'Day';
				baseTarget.append(
					$('<div />')
						.addClass('col-12 mb-3 c_schedule c_week')
						.append(
							$('<div />')
								.addClass('input-group ')
								.append(
									$('<label />')
										.attr('for', id)
										.addClass('input-group-text')
										.text(title)
								)
								.append(
									createSelectElement(id, dayOptionValueArr, dayOptionTextArr)
								)
						)
				)

				// Draw Date
				id = 'inputDate';
				title = 'Date';
				baseTarget.append(
					$('<div />')
						.addClass('col-12 mb-3 c_schedule c_month c_year')
						.append(
							$('<div />')
								.addClass('input-group ')
								.append(
									$('<label />')
										.attr('for', id)
										.addClass('input-group-text')
										.text(title)
								)
								.append(
									createSelectElement2(id, 1, 31, '일')
								)
						)
				)

				// Draw Hour
				id = 'inputHour';
				title = 'Hour';
				baseTarget.append(
					$('<div />')
						.addClass('col-6 mb-3 c_schedule c_date c_week c_month c_year')
						.append(
							$('<div />')
								.addClass('input-group ')
								.append(
									$('<label />')
										.attr('for', id)
										.addClass('input-group-text')
										.text(title)
								)
								.append(
									createSelectElement2(id, 0, 23, '시')
								)
						)
				)

				// Draw Minute
				id = 'inputMinute';
				title = 'Minute';
				baseTarget.append(
					$('<div />')
						.addClass('col-6 mb-3 c_schedule c_hour c_date c_week c_month c_year')
						.append(
							$('<div />')
								.addClass('input-group ')
								.append(
									$('<label />')
										.attr('for', id)
										.addClass('input-group-text')
										.text(title)
								)
								.append(
									createSelectElement2(id, 0, 59, '분')
								)
						)
				)

				// Draw crontab input
				id = 'inputCrontab';
				title = 'Crontab';
				baseTarget.append(
					$('<div />')
						.addClass('col-12 mb-3 c_crontab')
						.append(
							$('<div />')
								.addClass('input-group')
								.append(
									$('<label />')
										.attr('for', id)
										.addClass('input-group-text')
										.text(title)
								)
								.append(
									$('<input type="text" />')
										.attr('id', id)
										.addClass('form-control')
								)
								.append(
									$('<div class="invalid-feedback" />').text('$$$')
								)
						)
				)

				baseTarget.append(
					$('<div />')
						.append(
							$('<label />')
								.append(
									$('<a />')
										.attr('href', '#')
										.attr('id', 'switchControl')
										.text('create cron expression')
								)
						)
				)

			}

			function drawViewControl() {
				let crontabArr = settings.crontabVal.split(' ');
				let value;
				let frequencyValue = '';
				let showableScheduleUI = isShowableScheduleUI(crontabArr);
				if(showableScheduleUI) {
					value = '';
					let weekTextArr = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
					let timeTextArr = ['', '분', '시', '일', '월', '년'];
					for(let index = crontabArr.length - 1; index >= 0; index--) {
						if(crontabArr[index] !== '?' && crontabArr[index] !== '*') {
							if(index === 5) { // week 인 경우
								if(frequencyValue.length == 0) {
									frequencyValue = '매주';
								}
								value += ' ' + weekTextArr[crontabArr[index] - 1];
							} else {
								if(frequencyValue.length == 0) {
									frequencyValue = '매' + timeTextArr[index + 1];
								}

								if(index != 0) { // 초가 아닌 경우만
									value += ' ' + crontabArr[index] + timeTextArr[index];
								}
							}
						}
					}

					value = frequencyValue + value + ' 마다';
				} else {
					value = settings.crontabVal;
				}
				baseTarget.append(
					$('<div />')
						.addClass('col-12 mb-3 c_crontab')
						.append(
							$('<div />')
								.append(
									$('<input type="text" />')
										.prop('readonly', true)
										.addClass('form-control-plaintext')
										.val(value)
								)
						)
				)
			}

			function isShowableScheduleUI(crontabArr) {
				let showable = true;
				if(crontabArr.length != 6) {
					showable = false;
				} else {
					for(let i = 0; i < crontabArr.length; i++) {
						if(i == 0) {
							if(crontabArr[i] != 0) {
								// 초 값이 0초가 아니라면 UI로 표현할 수 없음.
								showable = false;
								break;
							}
						} else if (i == 5) {
							if(!isNaN(crontabArr[i])) {
								if(crontabArr[i - 1] !== '*' || crontabArr[i - 2] !== '?') {
									// 주 값이 설정되었는데 월이 '*' 이 아니고 일이 '?' 가 아니라면 UI로 표현할 수 없음.
									showable = false;
									break;
								}
							}
						}

						if(crontabArr[i] !== '*' && crontabArr[i] !== '?') {
							if(isNaN(crontabArr[i])) {
								// 분, 시, 일, 월, 주 값이 *, ?, 숫자 값이 아니라면 UI로 표현할 수 없음.
								showable = false;
								break;
							}
						}
					}
				}

				return showable;
			}

			function getFrequencyDefaultValue(crontabArr) {
				let selectIndex = -1;
				for(let i = crontabArr.length - 1; i >= 0; i--) {
					if(crontabArr[i] !== '*' && crontabArr[i] !== '?') {
						selectIndex = i;
						break;
					}
				}

				let selectedValue = '';
				if(selectIndex == 5) {
					selectedValue = 'c_week';
				} else if(selectIndex == 4) {
					selectedValue = 'c_year';
				} else if(selectIndex == 3) {
					selectedValue = 'c_month';
				} else if(selectIndex == 2) {
					selectedValue = 'c_date';
				} else if(selectIndex == 1) {
					selectedValue = 'c_hour';
				} else if(selectIndex == 0) {
					selectedValue = 'c_minute';
				} else {
					selectedValue = 'c_minute';
				}

				return selectedValue;
			}

			function initEditControl(crontabValArr, forceShowScheduleUI) {
				let returnVal;
				let showableScheduleUI;
				let crontabArr = crontabValArr;
				if(forceShowScheduleUI === undefined) {
					// 최초 로드할 때
					showableScheduleUI = isShowableScheduleUI(crontabArr);
					returnVal = 1;
				} else if(forceShowScheduleUI) {
					// create basic expression 링크를 클릭했을 때 (UI 형식으로 변경)
					showableScheduleUI = isShowableScheduleUI(crontabArr);
					if(!showableScheduleUI) {
						crontabArr = ['0', '0', '1', '*', '*', '?'];
						showableScheduleUI = true;
						returnVal = 2;
					} else {
						returnVal = 3;
					}
				} else {
					// create cron expression 링크를 클릭했을 때 (Crontab 형식으로 변경)
					showableScheduleUI = false;
					returnVal = 4;
				}

				let optionValue;
				if(showableScheduleUI) {
					$('.c_schedule').show();
					$('.c_crontab').hide();

					$('#switchControl').text('create cron expression')

					optionValue = getFrequencyDefaultValue(crontabArr);
					showHideScheduleControl(optionValue);
					$('#inputFrequency option[value=' + optionValue + ']').prop('selected', true);

					optionValue = crontabArr[1];
					if (optionValue !== '*') {
						$('#inputMinute option[value=' + optionValue + ']').prop('selected', true);
					}

					optionValue = crontabArr[2];
					if (optionValue !== '*') {
						$('#inputHour option[value=' + optionValue + ']').prop('selected', true);
					}

					optionValue = crontabArr[3];
					if (optionValue !== '*' && optionValue !== '?') {
						$('#inputDate option[value=' + optionValue + ']').prop('selected', true);
					}

					optionValue = crontabArr[4];
					if (optionValue !== '*') {
						$('#inputMonth option[value=' + optionValue + ']').prop('selected', true);
					}

					optionValue = crontabArr[5];
					if (optionValue !== '*' && optionValue !== '?') {
						$('#inputDay option[value=' + optionValue + ']').prop('selected', true);
					}

				} else {
					$('.c_schedule').hide();
					$('.c_crontab').show();

					$('#switchControl').text('create basic interval');

					$('#inputCrontab').val(crontabArr.join(' '));
				}

				return returnVal;
			}

			function __getCronValue() {
				let cronValue = '';

				if(settings.editable) {
					if ($('#inputFrequency').closest('div.c_schedule').is(':visible')) {
						let element;
						let header = ['Minute', 'Hour', 'Date', 'Month', 'Day'];
						let value = [];

						const dateIndex = 3;
						const dayIndex = 5;

						value[0] = '0';
						for (let i = 0; i < header.length; i++) {
							element = '#input' + header[i];
							if ($(element).closest('div.c_schedule').is(':visible')) {
								value[i + 1] = $(element).find('option:selected').val();
							} else {
								value[i + 1] = '*';
							}
						}

						if (value[dateIndex] === '*' && value[dayIndex] === '*') {
							value[dayIndex] = '?';
						} else {
							if (value[dateIndex] !== '*') {
								value[dayIndex] = '?';
							} else {
								value[dateIndex] = '?';
							}
						}

						for (let i = 0; i < value.length; i++) {
							cronValue += value[i];
							if (i != value.length - 1) {
								cronValue += ' ';
							}
						}
					} else {
						cronValue = $('#inputCrontab').val();
					}
				}
				return cronValue;
			}

			return pluginObj;
		}
	});

})(jQuery);


