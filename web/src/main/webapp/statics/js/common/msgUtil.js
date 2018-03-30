/**
 * Created by vanki on 16/10/28.
 */

function vankiMsgAlert(msg, title, fnOnclose) {
    if (!title) title = "消息";
    var d = dialog({
        title: title,
        content: msg,
        onclose: fnOnclose
    });
    d.show();
}

function vankiMsgConfirm(msg) {
    return confirm(msg);
}

function vankiMsgBubble(msg) {
    var d = dialog({
        content: msg,
        quickClose: true// 点击空白处快速关闭
    });
    d.show(document.getElementById('quickref-bubble'));
}

function vankiMsgAlertAutoClose(msg, time) {
    if (!time) time = 2000;
    var d = dialog({
        content: msg,
        quickClose: true
    });
    d.show();
    setTimeout(function () {
        d.close().remove();
    }, time);
}

function vankiMsgWindow(content, winAttrJson) {
    var title = "提示";
    var width = 200;
    var height = 50;
    var onok = function () {
        this.title = "提交中...";
        return true;
    };
    var onclose = function () {
    };
    if (winAttrJson) {
        if (winAttrJson['title']) title = winAttrJson['title'];
        if (winAttrJson['width']) width = winAttrJson['width'];
        if (winAttrJson['height']) height = winAttrJson['height'];
        if (winAttrJson['onok']) onok = winAttrJson['onok'];
        if (winAttrJson['onclose']) onclose = winAttrJson['onclose'];
    }
    var d = dialog({
        title: title,
        content: content,
        okValue: '确定',
        ok: onok,
        onclose: onclose,
        cancelValue: '取消',
        cancel: function () {
        }
    });
    d.width(width).height(height).show();
    return d;
}

function vankiLayerMsgSuccGou(msg, time) {
    if (!msg) return;
    time = time ? time : 3000;
    layer.msg(msg, {time: time, icon: 6});
}

function vankiLayerMsgSuccTou(msg, time) {
    if (!msg) return;
    time = time ? time : 3000;
    layer.msg(msg, {time: time, icon: 1});
}

function vankiLayerMsgFailCha(msg, time) {
    if (!msg) return;
    time = time ? time : 3000;
    layer.msg(msg, {time: time, icon: 5});
}

function vankiLayerMsgFailTou(msg, time) {
    if (!msg) return;
    time = time ? time : 3000;
    layer.msg(msg, {time: time, icon: 2});
}
