/**
 * Created by vanki on 2017/2/25.
 */
$(function () {
    //显示弹出层
    $('#j_login_register').click(function () {
        popLoginRegister();
    });
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