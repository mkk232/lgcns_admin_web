/**
 * 파일이름: jquery-rayful-ajax.js
 * 설명: Ajax call 함수를 랩핑한 메소드를 제공
 * 주의사항: jquery.min.js, jquery.fileDownload.js, common-dialog.js include 후 이 파일을 include
 */

$(document).ajaxStart(function () {
	$('#my-spinner').show();
})
.ajaxComplete(function () {
	$('#my-spinner').hide();
});

/*
$.ajaxSetup({
	beforeSend: function() {
		$('#my-spinner').show();
	},
	complete: function() {
		$('#my-spinner').hide();
	}
});
*/
function callAjax(url, method, sendData, successCallback, errorCallback, showBlock) {
	let global = showBlock;
	if(global === undefined) {
		global = true;
	}
	
	const promise = new Promise((resolve, reject) => {
		$.ajax({
			url: url,
			type: method,
			data: sendData,
			global: global,
			dataType : 'JSON',
			timeout: 10000,
			success: function(data) {
				if(successCallback !== undefined && successCallback != null) {
					successCallback(data, resolve);
				}
			},
			error: function(xhr) {
				if(xhr.status == 401) {
					window.location.href='/';
				} else if(xhr.status == 403) {
					alertEx('error', '편집 권한이 없습니다.');
				} else if(xhr.statusText == 'timeout') {
					alertEx('error', '서버와의 연결이 원활하지 않습니다. 네트워크 연결을 확인해주세요.');
				} else {
					if(errorCallback !== undefined && errorCallback != null) {
						errorCallback(xhr, reject);
					} else {
						alertEx('error', '서버 오류가 발생하였습니다. 다시 시도해주세요.');
					}
				}
			}
		})
	});
	
	return promise;
}

function callAjaxJson(url, method, sendData, successCallback, errorCallback, showBlock) {
	let global = showBlock;
	if(global === undefined) {
		global = true;
	}
	
	const promise = new Promise((resolve, reject) => {
		$.ajax({
			url: url,
			type: method,
			data: JSON.stringify(sendData),
			global: global,
			dataType : 'JSON',
			contentType: 'application/json; charset=utf-8',
			timeout: 10000,
			success: function(data) {
				if(successCallback !== undefined && successCallback != null) {
					successCallback(data, resolve);
				}
			},
			error: function(xhr) {
				if(xhr.status == 401) {
					alertEx('error', '세션이 종료되어 로그인 페이지로 이동됩니다.');
					window.location.href='/';
				} else if(xhr.status == 403) {
					alertEx('error', '권한이 없습니다.');
				} else if(xhr.statusText == 'timeout') {
					alertEx('error', '서버와의 연결이 원활하지 않습니다. 네트워크 연결을 확인해주세요.');
				} else {
					if(errorCallback !== undefined && errorCallback != null) {
						errorCallback(xhr, reject);
					} else {
						alertEx('error', '서버 오류로 작업에 실패하였습니다.');
					}
				}
			}
		})
	});
	
	return promise;
}
