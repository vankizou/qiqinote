/**
 * Created by vanki on 2017/2/25.
 */

$(function () {
    $(".homeUrl").addClass("topActive");

    var pageSize = 20;
    getNoteList(1, pageSize);

    $('.j_page_prev_next').click(function () {
        getNoteList($(this).attr('val'), pageSize);
    });

    $('#j_page_jump').click(function () {
        var val = $('#j_page_jump_val').val();
        if (val && /^\d+$/.test(val)) getNoteList(val, pageSize);
    });
});

function getNoteList(pageNo, pageSize, navNum) {
    var params = {
        type: ConstDB.Note.typeNote,
        isOrderByTimeDesc: true,
        pageNo: pageNo,
        pageSize: pageSize,
        navNum: navNum
    };
    var fnSucc = function (data) {
        console.info(data)
        var pageLast = 0;   // 尾页
        var pagePrev = 0;   // 上一页
        var pageNext = 0;   // 下一页
        var pageCurr = 0;   // 本页
        var pageJump = 0;   // 跳转页
        if (data) {
            pageLast = data['endPage'];
            pagePrev = data['prevPage'];
            pageNext = data['nextPage'];
            pageCurr = data['currPage'];
            pageJump = pageCurr + 3;
            pageJump = pageJump > pageLast ? pageLast : pageJump;
        }
        $('#j_page_info').html(pageCurr + "/" + pageLast);
        $('#j_page_previous').attr('val', pagePrev);
        $('#j_page_next').attr('val', pageNext);
        $('#j_page_jump_val').val(pageJump);

        var node = '';
        var datas = data['data'];
        for (var i in datas) {
            var d = datas[i];

            var noteId = d['note']['id'];
            var noteTitle = d['note']['title'];
            var noteCreateDatetime = d['note']['createDatetime'];
            var noteViewNum = d['note']['viewNum'];
            noteViewNum = numToHumanView(noteViewNum, null, 1);

            var userName = d['user']['name'];
            var userAlias = d['user']['alias'];

            var pNoteTitle = '';
            var pNote;
            if ((pNote = d['parentNote'])) {
                pNoteTitle = pNote['title'];
            }

            var viewNoteUrl = ConstAjaxUrl.Note.view_html[0].replace(ConstAjaxUrl.Note.view_html[1], noteId);
            var viewUserUrl = ConstAjaxUrl.Index.userHome_html[0].replace(ConstAjaxUrl.Index.userHome_html[1], userName);

            node += '<tr>'
            node += '<td><a href="' + viewNoteUrl + '" target="_blank">' + noteTitle + '</a></td>';
            node += '<td>' + pNoteTitle + "</td>";
            node += '<td><a href="' + viewUserUrl + '" target="_blank">' + userAlias + '</a></td>';
            node += '<td>' + noteCreateDatetime + "</td>";
            node += '<td>' + noteViewNum + "</td>";
            node += '</tr>';
        }
        $('#j_note_list_table').children().remove();
        $('#j_note_list_table').append(node);
    };
    vankiAjax(ConstAjaxUrl.Note.pageOfHome, params, fnSucc);
}