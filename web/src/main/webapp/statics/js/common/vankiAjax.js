/**
 * Created by vanki on 16/10/28.
 */

var vankiAjax = function (ajaxInfo, ajaxParams, successFun, failFun, ajaxContext, isNotAsync) {
    $.ajax({
        url: ajaxInfo[0],
        type: ajaxInfo[1],
        dataType: ajaxInfo[2],
        data: ajaxParams,
        async: !isNotAsync,

        success: function (data) {
            operateMyAjaxData(data, ajaxContext, successFun, failFun, ajaxParams ? ajaxParams.is_pop_error_window : undefined);
        }
    })
}

var operateMyAjaxData = function (data, ajaxContext, successFun, failFun, is_pop_error_window) {
    if (typeof data == 'string') data = eval('(' + data + ')');
    var code = data['code'];
    if (code == ConstStatusCode.CODE_200[0]) {
        if (successFun) successFun(data['data'], ajaxContext);
    } else {
        if (code == '404') {
            window.location = ConstAjaxUrl.Index.error404_html[0];
            return;
        }
        // 是否弹出错误码信息
        if (is_pop_error_window != false && !is_pop_error_window) is_pop_error_window = true;
        var errInfo = ConstStatusCode["CODE_" + code];
        if (code == 201) {// 未登录
            popLoginRegister();
        } else if (errInfo && is_pop_error_window) {
            vankiLayerMsgFailTou(errInfo[1]);
            // vankiMsgAlertAutoClose(errInfo[1], 3000);
        }

        if (failFun) failFun(data, ajaxContext);
    }
}

var vankiParseResponseData = function (data, succFn, failFn, is_pop_error_window) {
    if (typeof data == 'string') data = eval('(' + data + ')');
    var code = data['code'];
    if (code == ConstStatusCode.CODE_200[0]) {
        if (succFn) succFn(data['data']);
    } else {
        if (code == '404') {
            window.location = ConstAjaxUrl.Index.error404_html[0];
            return;
        }
        // 是否弹出错误码信息
        if (is_pop_error_window != false && !is_pop_error_window) is_pop_error_window = true;
        var errInfo = ConstStatusCode["CODE_" + code];
        if (code == 201) {// 未登录
            popLoginRegister();
        } else if (errInfo && is_pop_error_window) {
            vankiLayerMsgFailTou(errInfo[1]);
            // vankiMsgAlertAutoClose(errInfo[1], 3000);
        }

        if (failFn) failFn(data);
    }
}