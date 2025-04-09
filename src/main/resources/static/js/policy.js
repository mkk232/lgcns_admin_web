/**
 * 스케줄 값이 Crontab 형식에 맞는지 요청
 */
function reqValidSchedule(cronValue) {
    let callUrl = '/sync/policy/valid/schedule';
    let sendData = {};
    sendData['policySchedule'] = cronValue
    let promise = callAjax(callUrl, 'GET', sendData,
        function(data, resolve) {
            resolve()
        },
        function(xhr, reject) {
            reject(xhr.responseJSON.message);
        }
    )

    return promise;
}

/**
 * 위치 정보의 상태 아이콘을 출력한다.
 */
function drawLocationStatusIcon(locationId, used) {
    let icon;
    if(locationId == null || locationId === undefined) {
        icon = $('<div class="badge text-bg-warning text-wrap" style="width: 3rem;" title="저장되지 않은 신규 정보."/> ')
            .text('new')
    } else if (used === 'N') {
        icon = $('<div class="badge text-bg-secondary text-wrap" style="width: 3rem;" title="동기화를 일시적으로 중지."/> ')
            .text('stop')
    } else if (used === 'Y') {
        icon = $('<div class="badge text-bg-success text-wrap" style="width: 3rem;" title="동기화 수행됨."/> ')
            .text('sync')
    } else if (used === 'I') {
        icon = $('<div class="badge text-bg-danger text-wrap" style="width: 3rem;" title="MS365 원본 데이터가 삭제됨." /> ')
            .text('del')
    }

    return $(icon);
}

/**
 * 정책 정보의 상태 아이콘을 출력한다.
 */
function drawPolicyStatusIcon(used, disabled) {
    let icon;
    if(disabled === 'P') {
        icon = $('<div class="badge text-bg-warning text-wrap" style="width: 4rem;" /> ')
            .text('삭제중')
    } else  {
        if (used === 'N') {
            icon = $('<div class="badge text-bg-secondary text-wrap" style="width: 4rem;" /> ')
                .text('stop')
        } else {
            icon = $('<div class="badge text-bg-success text-wrap" style="width: 4rem;" /> ')
                .text('sync')
        }
    }

    return $(icon);
}

/**
 * 위치 정보의 Stop, Start 버튼 아이콘을 출력한다.
 */
function drawLocationStopStartBtnIcon(locationId, used) {
    let stopStartBtn;
    if(locationId != null && locationId !== undefined) {
        stopStartBtn = drawPolicyStopStartBtnIcon(used);
    }

    return stopStartBtn;
}

/**
 * 동기화 정책의 Stop, Start 버튼 아이콘을 출력한다.
 */
function drawPolicyStopStartBtnIcon(used) {
    let stopStartBtn;
    if(used === 'Y') {
        stopStartBtn = $('<a href="#" class="mx-1 " />')
            .attr('name', 'btnStop')
            .attr('title', '중지')
            .append(
                $('<i class="bi bi-pause-circle-fill text-danger"></i>')
            )
    } else if(used === 'N') {
        stopStartBtn = $('<a href="#" class="mx-1 " />')
            .attr('name', 'btnStart')
            .attr('title', '시작')
            .append(
                $('<i class="bi bi-play-circle-fill text-success"></i>')
            )
    }

    return stopStartBtn;
}

function drawExecSummaryLogStatusIcon(status) {
    let icon;
    if (status === 'READY') {
        icon = $('<div class="badge text-bg-secondary text-wrap" style="width: 4rem;" /> ')
            .text('스캔 중')
    } if (status === 'SCAN') {
        icon = $('<div class="badge text-bg-warning text-wrap" style="width: 4rem;" /> ')
            .text('스캔 완료')
    } if (status === 'SUCCESS') {
        icon = $('<div class="badge text-bg-success text-wrap" style="width: 4rem;" /> ')
            .text('성공')
    } if (status === 'FAIL') {
        icon = $('<div class="badge text-bg-danger text-wrap" style="width: 4rem;" /> ')
            .text('실패')
    }

    return $(icon);
}

function drawExecDetailLogStatusIcon(status) {
    let icon;
    if (status === 'READY') {
        icon = $('<div class="badge text-bg-secondary text-wrap" style="width: 4rem;" /> ')
            .text('대기')
    } if (status === 'SYNC') {
        icon = $('<div class="badge text-bg-warning text-wrap" style="width: 4rem;" /> ')
            .text('동기화')
    } if (status === 'SUCCESS') {
        icon = $('<div class="badge text-bg-success text-wrap" style="width: 4rem;" /> ')
            .text('성공')
    } if (status === 'FAIL') {
        icon = $('<div class="badge text-bg-danger text-wrap" style="width: 4rem;" /> ')
            .text('실패')
    }

    return $(icon);
}
