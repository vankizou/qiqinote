/**
 * Created by vanki on 2017/5/17.
 */

$(function () {
    $('.j_change').click(function () {
        var val = $(this).html();

        if (val == '登录') {
            $('#j_register').hide();
            $('#j_login').show();
        } else {
            $('#j_login').hide();
            $('#j_register').show();
            fnGetImageCode();
        }
    });

    $('#j_login_id, #j_login_pwd').keyup(function (event) {
        if (event.keyCode == 13) fnLogin();
    });

    $('#j_register input').keyup(function (event) {
        if (event.keyCode == 13) fnRegister();
    });

    /**
     * 登录
     */
    $('#j_login_submit').click(fnLogin);

    /**
     * 注册
     */
    $('#j_register_submit').click(fnRegister);


    $('#j_reg_imagecode_img').click(function () {
        fnGetImageCode();
    });

    function fnLogin() {
        var account = $('#j_login_id').val();
        var pwd = $('#j_login_pwd').val();
        var isRemember = $('#j_login_is_remember').is(':checked');

        if (!account) {
            vankiLayerMsgFailTou("请输入ID号或用户名");
            return;
        }
        if (!pwd) {
            vankiLayerMsgFailTou("请输入密码");
            return;
        }
        var params = {
            account: account,
            password: pwd,
            isRemember: isRemember ? 1 : 0
        };
        var fnSucc = function () {
            // window.location = ConstAjaxUrl.Index.userHome_html[0].replace(ConstAjaxUrl.Index.userHome_html[1], account);
            window.location.reload();
        };
        vankiAjax(ConstAjaxUrl.Index.signIn, params, fnSucc);
    };

    function fnRegister() {
        var username = $('#j_reg_username').val();
        var pwd = $('#j_reg_pwd').val();
        var confirmPwd = $('#j_reg_confirm_pwd').val();
        var findPwdValidation = $('#j_reg_find_pwd_validation').val();
        var imageCodeVal = $('#j_reg_imagecode').val();

        if (!username) {
            vankiLayerMsgFailTou("请输入唯一用户名");
            return;
        }
        if (!pwd) {
            vankiLayerMsgFailTou("请输入密码");
            return;
        }
        if (!confirmPwd) {
            vankiLayerMsgFailTou("请输入确认密码");
            return;
        }
        if (pwd != confirmPwd) {
            vankiLayerMsgFailTou("两次密码输入不一致");
            return;
        }
        if (!imageCodeVal) {
            vankiLayerMsgFailTou("请计算图片验证码的值");
            return;
        }
        var params = {
            name: username,
            password: pwd,
            imageCode: imageCodeVal
        };

        var fnSucc = function (data) {
            if ($('#j_win_loginRegister').attr('is_pop_win')) $.unblockUI();
            $('#j_login').hide();
            $('#j_register').hide();

            var msg = "<div style='padding: 20px;'>" +
                "<div>恭喜！帐号注册成功！</div>" +
                "<div>您的登录ID为：<span style='color:red; font-size:20px; font-weight: 700;'>" + data.id + "</span></div>" +
                "<div>您的用户名为：<span style='color:red; font-size:20px; font-weight: 700;'>" + data.name + "</span></div>" +
                "<div class='text-center' style='margin-top: 20px;'><button id='btn_close_msg' class='btn' style='background: #0593d3; color: #fff;''>好的，我已牢记我的ID和用户名</button></div>" +
                "</div>";

            //页面层
            var regSuccLayerIndex = layer.open({
                type: 1,
                title: false,
                skin: 'layui-layer-rim', //加上边框
                skin: 'layui-layer-lan',
                closeBtn: false,
                content: msg,
            });
            $('#btn_close_msg').click(function () {
                layer.close(regSuccLayerIndex);
                // window.location = ConstAjaxUrl.Index.userHome_html[0].replace(ConstAjaxUrl.Index.userHome_html[1], data['name']||data['id']);
                window.location.reload();
            });
        };
        var fnFail = function (data) {
            if (data['code'] != ConstStatusCode.CODE_1102[0]) return;
            fnGetImageCode();
        };
        vankiAjax(ConstAjaxUrl.Index.signUp, params, fnSucc, fnFail);
    };

    /*==========图片验证码===========*/
    var fnGetImageCode = function () {
        var params = {
            width: 170,
            height: 45
        };
        var fnSucc = function (imageCode) {
            $('#j_reg_imagecode_img').attr('src', 'data:image/png;base64,' + imageCode);
        };
        vankiAjax(ConstAjaxUrl.ImageCode.getImageCode, params, fnSucc);
    };
});

