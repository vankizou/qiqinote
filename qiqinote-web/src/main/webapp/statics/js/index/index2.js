$(function () {
    var pageSize = 5;

    buildHomeNote(1, pageSize);

    $('.j_page_prev_next').click(function () {
        buildHomeNote($(this).attr('val'), pageSize);
    });

    $('#j_page_jump').click(function () {
        var value = $('#j_page_jump_val').val();
        if (value && /^\d+$/.test(value)) buildHomeNote(value, pageSize);
    });

    $('#j_note_title_like').keyup(function (event) {
        if (event.keyCode != 13) return;
        buildHomeNote(1, pageSize)
    });
});

function buildHomeNote(pageNo, pageSize) {
    var params = {
        type: ConstDB.Note.typeMarkdown,
        currPage: pageNo,
        pageSize: pageSize,
        navNum: 3,
        titleLike: $('#j_note_title_like').val()
    };
    var fnSucc = function (data) {
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

        $('.home_note').hide();

        var datas = data['data'];
        for (var i in datas) {
            var d = datas[i];

            var noteId = d['note']['id'];
            var noteIdLink = d['note']['idLink'];
            var noteTitle = d['note']['title'];
            var noteCreateDatetime = d['note']['createDatetime'];
            var noteViewNum = d['note']['viewNum'];
            noteViewNum = numToHumanView(noteViewNum, null, 1);

            var userName = d['user']['name'];
            var userAlias = d['user']['alias'];
            /*var userAvatar;
            if (d['user']['avatar']) {
                userAvatar = d['user']['avatar']['path'];
            }*/

            var pNoteTitle = '默认';
            var pNote;
            if ((pNote = d['parentNote'])) {
                pNoteTitle = pNote['title'];
            }

            var idOrIdLink = noteId + ".html";
            if (ConstDB.Note.secretLink == d['note']['secret']) {
                idOrIdLink = noteIdLink;
            }
            var viewNoteUrl = ConstAjaxUrl.Note.view_html[0].replace(ConstAjaxUrl.Note.view_html[1], idOrIdLink);
            var viewUserUrl = ConstAjaxUrl.Index.userHome_html[0].replace(ConstAjaxUrl.Index.userHome_html[1], userName);

            var seq = Number(i) + 1;

            buildEditor(seq, d['noteContent']);
            $('#j_home_note' + seq).show();
            $('#j_home_note' + seq + "_title").html(noteTitle);
            $('#j_home_note' + seq + "_title").attr("onclick", 'window.open("' + viewNoteUrl + '")');
            $('#j_home_note' + seq + "_parent").html(pNoteTitle);

            /*if (userAvatar) {
                $('#j_home_note' + seq + "_user img").attr("src", userAvatar);
            }*/

            $('#j_home_note' + seq + "_user span").html(userAlias);
            $('#j_home_note' + seq + "_user").attr("onclick", 'window.open("' + viewUserUrl + '")');
            $('#j_home_note' + seq + "_date").html(noteCreateDatetime);
            $('#j_home_note' + seq + "_view_num").html(numToHumanView(noteViewNum, null, 1));
        }
        if (pageCurr <= 1 && (!datas || datas.length == 0)) {
            vankiLayerMsgFailTou("未找到相关笔记");
        }
    };
    vankiAjax(ConstAjaxUrl.Note.pageOfHome, params, fnSucc);
}

function buildEditor(seq, markdown) {
    if (!markdown) markdown = "";

    var divId = "vanki-editormd-home-note" + seq;
    $('#' + divId).empty();

    return editormd.markdownToHTML(divId, {
        markdown: markdown,//+ "\r\n" + $("#append-test").text(),
        htmlDecode: false,       // 开启 HTML 标签解析，为了安全性，默认不开启
        htmlDecode: "style,script,iframe,form",  // you can filter tags decode
        toc: false,
        tocm: true,
        emoji: false,
        taskList: false,
        tex: false,  // 默认不解析
        flowChart: false,  // 默认不解析
        sequenceDiagram: false,  // 默认不解析
    });
}