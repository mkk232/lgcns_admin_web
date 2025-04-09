
/**
 * MS365 노드 아이콘 그리기
 */
function drawMS365Icon(nodeType) {
    let icon;
    if (nodeType === 'tenant') {
        icon = '<i class="bi bi-buildings me-1 text-black text-opacity-50"></i>';
    } else if (nodeType === 'site') {
        icon = '<i class="bi bi-globe-americas me-1 text-primary text-opacity-50"></i>';
    } else if (nodeType === 'drive') {
        icon = '<i class="bi bi-hdd me-1 text-success text-opacity-50"></i>';
    } else if (nodeType === 'folder') {
        icon = '<i class="bi bi-folder-fill me-1 text-warning text-opacity-50"></i>';
    } else if (nodeType === 'file') {
        icon = '<i class="bi bi-file-text me-1 text-black text-opacity-50"></i>';
    }

    return $(icon);
}

/**
 * 데이터 소스 위치 영역에 위치 정보를 출력한다.
 */
function drawMS365Location(locations, selectMode) {
    $('#positionTable tbody tr[data-nodetype=nodata]').remove();
    let tr;

    if(selectMode == 'one') {
        $('#positionTable tbody tr').remove();
    }

    let hasStatus = $('#positionTable th[data-role=status]').length > 0;
    let hasAction = $('#positionTable th[data-role=action]').length > 0;

    $.each(locations, function(index, location) {
        let driveId = location.driveId == -1 ? null : location.driveId;
        let itemId = location.itemId == -1 ? null : location.itemId;
        tr = $('<tr />')
            .attr('data-locationid', location.locationId)
            .attr('data-nodetype', location.nodeType)
            .attr('data-siteid', location.siteId)
            .attr('data-sitename', location.siteName)
            .attr('data-driveid', driveId)
            .attr('data-drivename', location.driveName)
            .attr('data-itemid', itemId)
            .attr('data-itempath', location.itemPath)
            .attr('data-used', location.used);

        if(hasStatus) {
            tr.append(
                    $('<td class="text-center" data-role="status"/>')
                        .append(
                            drawLocationStatusIcon(location.locationId, location.used)
                        )
                );
        }

        tr.append(
                $('<td class="text-center" />')
                    .append(
                        drawMS365Icon(location.nodeType)
                    )
                    .append(
                        location.nodeType
                    )
            );
        tr.append(
                $('<td />').text(location.siteName)
            );
        tr.append(
                $('<td />').text(location.driveName)
            );
        tr.append(
                $('<td />').text(location.itemPath)
            );

        if(hasAction) {
            tr.append(
                $('<td class="text-center" data-role="action"/>')
                    .append(
                        drawLocationStopStartBtnIcon(location.locationId, location.used)
                    )
                    .append(
                        $('<a href="#" class="mx-1 "/>')
                            .attr('name', 'btnRemove')
                            .attr('title', '삭제')
                            .append(
                                $('<i class="bi bi-trash3" />')
                            )
                    )
            );
        }

        $('#positionTable tbody').append(tr)
    })

    $('#positionTable').removeClass('is-invalid').removeClass('is-valid');
}

