/**
 * 파일이름: common-rayful-dialog.js
 * 설명: SweetAlert2를 커스텀하여 alert, confirm 및 toast 등의 랩핑한 함수 작성
 * 주의사항: SweetAlert2 include 후 선언
 *         Bootstrap 디자인을 이용한 태그 생성
 */

const Toast = Swal.mixin({
  toast: true,
  position: 'top-end',
  showConfirmButton: false,
  timer: 3000,
  timerProgressBar: true,
  didOpen: (toast) => {
    toast.onmouseenter = Swal.stopTimer;
    toast.onmouseleave = Swal.resumeTimer;
  }
});

const SwalWithBootstrap = Swal.mixin({
	customClass: {
		confirmButton: 'btn btn-primary m-1',
		cancelButton: 'btn btn-secondary m-1'
	},
	buttonsStyling: false,
});

const alertEx = function (icon, message, callback) {
  SwalWithBootstrap.fire({
    icon: icon,
    html: message,
    confirmButtonText: '<i class="bi bi-check-lg"></i> 확인',
    allowOutsideClick: false,
  }).then(function () {
    if (callback) { callback(); }
  });
};

const alertToast = function(icon, message) {
	Toast.fire({
	  icon: icon,
	  title: message
	});
}

const alertToastPos = function(icon, message, position) {
    Toast.fire({
        icon: icon,
        title: message,
        position: position
    });
}


const confirmEx = function (message, callback, fallback) {
  SwalWithBootstrap.fire({
	icon: 'question',
    text: message,
    showCancelButton: true,
    confirmButtonText: '<i class="bi bi-check-lg"></i> 확인',
    cancelButtonText: '<i class="bi bi-x-lg"></i> 취소',
    allowOutsideClick: false,
  }).then(function (result) {
    if (result.isConfirmed) {
      if (callback) { callback(); }
    } else if (result.isDismissed) {
      if (fallback) { fallback(); }
    }
  });
  
};

