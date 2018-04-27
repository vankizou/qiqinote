/**
 * Created by vanki on 2017/6/22.
 */

/**
 *
 * @param formId        form表单ID
 * @param fileInputId   要上传的input[type=file]的ID
 * @param succFn        成功回调函数
 * @param failFn        失败回调函数
 * @param maxNum        最大上传图片数量，默认10
 * @param maxSize       单张图片最大尺寸，单位(M)，默认5M
 */
function vankiUploadImageMulti(formId, fileInputId, succFn, failFn, maxNum, maxSize) {
    if (!formId || !fileInputId) return;
    if (!maxNum) maxNum = 10;
    if (!maxSize) maxSize = 5;  // 5M

    var maxSize2 = maxSize * 1024 * 1024;
    /**
     * 上传图片
     */
    var layerIndex;
    var fnBeforeSubmit = function () {
        var images = $("#" + fileInputId)[0].files;

        if (images.length > maxNum) {
            vankiLayerMsgFailTou("最多可同时上传" + maxNum + "张图片", {icon: 5, time: 3000});
            return false;
        }

        for (i in images) {
            var image = images[i];
            var type = image.type;
            if (!type) continue;

            if (type.indexOf("jpg") == -1
                && type.indexOf("jpeg") == -1
                && type.indexOf("png") == -1
                && type.indexOf("gif") == -1) {
                vankiLayerMsgFailTou("不支持的图片格式（jpg、jpeg、png、gif）");
                return false;
            }

            if (image.size > maxSize2) {
                vankiLayerMsgFailTou("图片大小不能超过 " + maxSize + " M");
                return false
            }
        }
        layerIndex = layer.load(0, {shade: false});
        return true;
    };
    newSuccFn = function (data) {
        if (layerIndex) layer.close(layerIndex);
        if (succFn) succFn(data);
    };
    if (!failFn) {
        if (layerIndex) layer.close(layerIndex);
        failFn = function () {
            vankiLayerMsgFailTou("网络异常，请稍后重试...");
        }
    }
    var options = {
        beforeSubmit: fnBeforeSubmit,
        url: ConstAjaxUrl.Image.uploadMulti[0],
        type: "post",
        dataType: "json",
        clearForm: true,
        resetForm: true,
        success: newSuccFn,
        error: failFn
    };
    $("#" + formId).ajaxForm(options);
}