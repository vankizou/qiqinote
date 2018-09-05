$(function () {
    menuDisplay();

    $('.mfb-component__button--main').click(function () {
        var title = $(this).attr("title");

        if (title == '评论') {
            $('#j_note_comment_content').focus();
        } else if (title == '下载') {
            downloadNote(c_noteId, c_noteIdLink, c_pwd)
        }
    });

    function downloadNote(noteId, noteIdLink, password) {
        var params = {
            is_pop: false,
            id: noteId,
            idLink: noteIdLink,
            password: password
        };
        var fnSucc = function (data) {
            window.location = "/note/download.json?id=" + noteId + "&idLink=" + noteIdLink + "&password=" + password;
        };
        var fnFail = function (data) {
            if (data['code'] != ConstStatusCode.CODE_1100[0]) return;
            var tempPwd = prompt("请输入密码");
            if (tempPwd == null) return false;
            downloadNote(tempPwd);
        };
        vankiAjax(ConstAjaxUrl.Note.preDownload, params, fnSucc, fnFail);
    }

    /**
     * 边距过小不显示悬浮
     */
    function menuDisplay() {
        if ($('#j_note_info .col-xs-9').offset().left < 30 || $(window).height() < 320) {
            $('#menu').hide();
        } else {
            $('#menu').show();
        }
    }
});