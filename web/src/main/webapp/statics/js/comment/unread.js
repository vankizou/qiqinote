$(function () {
    list();

    $("#comment_view_load_more").on("click", function () {
        list();
    });
});

var pageSize = 10;

function list() {
    var params = {
        "type": ConstDB.Comment.typeNote,
        "pageSize": pageSize
    };

    var fnSucc = function (page) {
        if (!page) return;
        var datas = page["data"];
        if (!datas) {
            loadMoreShowOrHide(0);
            return;
        }

        for (var i in datas) {
            buildLiOfComment(datas[i]);
        }
        loadMoreShowOrHide(datas.length);
        rebuildUnreadInfo();
    };
    vankiAjax(ConstAjaxUrl.Comment.pageOfUnread, params, fnSucc);
}

function loadMoreShowOrHide(dataSize) {
    dataSize = dataSize || 0;

    if (dataSize < pageSize) {
        $("#comment_view_load_more").hide();
    } else {
        $("#comment_view_load_more").show();
    }
}

function buildLiOfComment(comment) {
    if (!comment) return;

    var newLiEle = $('#j_comment_view_ul_li_default').clone();
    newLiEle.attr('id', 'j_comment_view_ul_li_' + comment["id"]);

    var userInfoEle = newLiEle.find(".comment_view_user_info");
    userInfoEle.attr('href', '/' + comment["fromUserName"]);
    userInfoEle.find("img").attr("src", comment["fromUserAvatar"] || "");

    var titleEle = newLiEle.find(".comment_view_title");
    titleEle.find(".comment_view_user_alias").html(comment["fromUserAlias"]);
    titleEle.find(".comment_view_time").html(comment["createDatetime"]);

    // 标题
    var uri = comment["targetUri"] || comment["targetId"];
    if (Number(uri)) {
        uri += ".html";
    }
    titleEle.find(".comment_view_title").html("《" + comment["targetTitle"] + "》");
    titleEle.find(".comment_view_title").attr("href", "/note/" + uri);

    var parentId = comment["parentId"];
    if (!parentId || parentId == ConstDB.defaultParentId) {
        titleEle.find(".comment_view_type_str").html("评论");
    } else {
        titleEle.find(".comment_view_type_str").html("回复");
    }

    var contentEle = newLiEle.find(".comment_view_content");
    contentEle.find(".comment_view_content_data").html(comment["content"]);

    newLiEle.show();
    $("#j_comment_view_ul").append(newLiEle);
}