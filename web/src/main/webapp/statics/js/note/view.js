/**
 * Created by vanki on 16/10/27.
 */
var vankiEditor;
$(function () {
    var startContent = $('#j_content').html();

    if (c_isNeedPwd == ConstDB.Note.isNeedPwdYes) {
        fnGetNoteVo(c_noteIdLink);
    } else if (!startContent) {
        $('#j_empty_content').show();
        $('.j_note_info').hide();
    } else {
        fnInitVankiEditor();
        $('#j_empty_content').hide();
        $('.j_note_info').show();
    }
});

function fnGetNoteVo(noteIdOrIdLink, msg) {
    if (!msg) msg = "请输入密码";
    var pwd = prompt(msg);
    if (pwd == null) return;//fnGetNoteVo(noteId);
    var params = {
        "idOrIdLink": noteIdOrIdLink,
        "password": pwd,
        "is_pop_error_window": false
    };
    var fnSucc = function (data) {
        var val = "";
        if (data['noteDetailList'] && data['noteDetailList'][0]) {
            val = data['noteDetailList'][0]['content'];
        }
        if (val) {
            $('#j_empty_content').hide();
            $('.j_note_info').show();

            /**
             * 密码的不能一次性获取完数据
             */
            if (data['parentNote']) {
                $('#j_note_info_parent_title').html(data['parentNote']['title']);
            }
            if (data['user']) {
                $('#j_note_info_user_alias').html(data['user']['alias']);
            }
            $('#j_note_info_create_datetime').html(data['createDatetimeStr']);
            $('#j_note_info_update_datetime').html(data['updateDatetimeStr']);
            $('#j_note_info_user_url').attr('href', '/user/' + data['user']['name']);

            if (!vankiEditor) fnInitVankiEditor(val);
        } else {
            $('#j_empty_content').show();
            $('.j_note_info').hide();
        }
        changeFooterMarginTop();
    };
    var fnFail = function (data) {
        fnGetNoteVo(noteIdOrIdLink, "密码输入错误，请重新输入");
    };
    vankiAjax(ConstAjaxUrl.Note.getNoteVOById, params, fnSucc, fnFail);
}

function fnInitVankiEditor(val) {
    vankiEditor = editormd.markdownToHTML("vanki-editormd-view-note", {
        markdown: val,//+ "\r\n" + $("#append-test").text(),
        htmlDecode: false,       // 开启 HTML 标签解析，为了安全性，默认不开启
        htmlDecode: "style,script,iframe,form",  // you can filter tags decode
        toc: true,
        tocm: true,
        emoji: false,
        taskList: true,
        tex: true,  // 默认不解析
        flowChart: true,  // 默认不解析
        sequenceDiagram: true,  // 默认不解析
    });
}