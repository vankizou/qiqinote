/**
 * Created by vanki on 2017/2/25.
 */
$(function () {
    //显示弹出层
    $('#j_login_register').click(function () {
        popLoginRegister();
    });

    rebuildUnreadInfo();
});

/**
 * 弹出登录注册
 */
function popLoginRegister() {
    $('#j_register').hide();
    $('#j_login').show();
    $.blockUI({
        message: $('#j_win_loginRegister'),
        css: {width: '500px'},
        onOverlayClick: $.unblockUI
    });
}

/**
 * 未读数据
 */
function rebuildUnreadInfo() {
    // 评论未读数
    var noteCommentUnread = unreadNumOfComment();

    // 总未读数
    var totalUnread = noteCommentUnread;

    if (totalUnread) {
        $("#j_total_unread_num").find(".badge").html(totalUnread);
    } else {
        $("#j_total_unread_num").find(".badge").html("");
    }
    if (noteCommentUnread) {
        $("#j_commend_unread_num").find(".badge").html(noteCommentUnread);
        $("#j_commend_unread_num").show();
    } else {
        $("#j_commend_unread_num").hide();
    }
}

// 评论未读
function unreadNumOfComment() {
    var params = {
        "type": ConstDB.Comment.typeNote
    };
    var num = 0;
    var fnSucc = function(data) {
        num = data;
    };
    vankiAjax(ConstAjaxUrl.Comment.unreadNum, params, fnSucc, null, null, false);
    return num
}