/**
 * 
 */
 
/**
 * bytes를 입력받아 해당 단위로 환산해 리턴해 주는 함수
 */
function bytesToSize(bytes) {
	  const sizes = ['B', 'KB', 'MG', 'GB', 'TB'];
	  if (bytes == 0) return 'n/a';
	  const mock = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)), 10);
	  if (mock == 0) return bytes + sizes[i];
	  return (bytes / (1024 ** mock)).toFixed(1) + sizes[mock];
}

/**
 * Date 값을 yyyy-MM-dd HH:mm:ss 형식으로 변환
 */
function formatDateString(dateString) {
	if(dateString === undefined || dateString === null) {
		return dateString;
	}

	const TIME_ZONE = 9 * 60 * 60 * 1000; // 9시간
	let d = new Date(dateString);
	
	let date = new Date(d.getTime() + TIME_ZONE).toISOString().split('T')[0];
	let time = d.toTimeString().split(' ')[0];
	return date + ' ' + time;
}


/**
 * 특수 문자 여부
 */
function checkSpecialChar(text) {
	let reg = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g;
	return reg.test(text);
}

/**
 * 사용자 / 그룹 계정 아이콘의 색상 클래스를 알아낸다.
 */
function getAccoutColorClass(used, builtin) {
	let className = '';
	
	if( used == 'Y' ) {
		if(builtin == 'Y') {
			className = 'text-warning';
		} else {
			className = 'text-success';
		}
	} else {
		if(builtin == 'Y') {
			className = 'text-warning text-opacity-25';
		} else {
			className = 'text-success text-opacity-25';
		}
	}
	
	return className;
}

/**
 * 업로드 파일 사이즈를 제한한다.
 */
function isExceedFileSize(uploadFile) {
	let maxSize = 1024 * 10000; // 10MB
	let fileSize = uploadFile.files[0].size;
	if(fileSize > maxSize) {
		alertEx('error', '업로드 파일의 사이즈는 10MB를 초과할 수 없습니다.');
		return true;
	} else {
		return false;
	}
}

/**
 * 데이터 소스, 저장소의 상태 아이콘을 출력한다.
 */
function drawCommonStatusIcon(statusText) {
	let icon;
	if (statusText === '사용') {
		icon = $('<div class="badge text-bg-success text-wrap" style="width: 4rem;" title="동기화 정책에서 사용하고 있습니다." /> ')
			.text(statusText)
	} else {
		icon = $('<div class="badge text-bg-secondary text-wrap" style="width: 4rem;" title="동기화 정책에서 사용하지 않습니다." /> ')
			.text(statusText)
	}

	return $(icon);
}

function disableFocus(target) {
	$.each(target.find('input, select, a, button, textarea'), function(index, ele) {
		$(ele).blur();
	})
}


/**
 * 테이블에 데이터가 없을 경우 기본 정보를 출력
 */
function drawEmptyTable(tableTarget, text, height, colCount) {
	let trHeight;
	let emptyText;
	if(text === undefined || text == null) {
		emptyText = '데이터가 없습니다.';
	} else {
		emptyText = text;
	}

	if(height === undefined || height == null) {
		trHeight = '300px';
	} else {
		trHeight = height
	}

	let colCnt;
	if(colCount === undefined || colCount == null) {
		colCnt = tableTarget.find('thead th').length;
	} else {
		colCnt = colCount;
	}
	if(tableTarget.find('tbody tr').length == 0) {
		tableTarget.append(
			$('<tr />')
				.attr('data-nodetype', 'nodata')
				.append(
					$('<td />')
						.attr('colspan', colCnt)
						.css('height', trHeight)
						.text(emptyText)
						.addClass('text-center')
				)
		)
	}
}

/**
 * Bootstrap과 SweetAlert를 함께 사용할 때 키보드 이벤트를 제어하는 함수
 */
function setModalKeyEvent(target) {
	// Bootstrap 5 모달 옵션 설정
	const modalElement = target[0];  // jQuery 객체를 DOM 요소로 변환
	const modal = new bootstrap.Modal(modalElement, {
		keyboard: false
	});

	// 전역 키보드 이벤트 핸들러
	$(document).on('keydown', function(e) {
		if (Swal.isVisible()) {
			// SweetAlert이 표시중일 때
			e.stopPropagation();
			if (e.keyCode === 13 || e.keyCode === 32) {
				Swal.clickConfirm();
				return false;
			}
		} else if (e.keyCode === 27) {
			// ESC 키 입력시
			if (target.hasClass('show')) {
				modal.hide();
			}
		} else if (e.keyCode === 13) {
			// Enter 키 입력시
			if (target.hasClass('show')) {
				target.find('.modal-footer button').eq(0).trigger('click');
			}
		}
	});

	// 모달 이벤트 핸들러
	target.on('shown.bs.modal', function() {
		const bsModal = bootstrap.Modal.getInstance(modalElement);
		if (bsModal) {
			bsModal._config.keyboard = false;
		}
	});
}