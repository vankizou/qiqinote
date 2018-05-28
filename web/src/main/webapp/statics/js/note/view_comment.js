var rootPageSize = 5;
var subPageSize = 5;

$(function () {
    list(null, 1, rootPageSize);

    $("#j_note_comment_create_btn").on("click", function () {
        var rootId = $("#j_note_comment_content").attr("root_id");
        var parentId = $("#j_note_comment_content").attr("parent_id");
        var toUserId = $("#j_note_comment_content").attr("to_user_id");
        createComment(rootId, parentId, toUserId);
    });

    /* 根评论回复预处理 */
    $("body").on("click", ".comment_view_content_reply", function () {
        prepareReply(this);
    });

    /* 子评论回复预处理 */
    $("body").on("click", ".comment_view_content_reply_sub", function () {
        prepareReply(this);
    });

    /* 取消回复 */
    $("#j_note_comment_retry_cancel").on("click", function () {
        clearReply();
    });

    /* 点击查看回复 */
    $("body").on("click", ".comment_view_reply", function () {
        var rootId = $(this).attr("comment_id");
        if ($("#j_comment_view_ul_li_" + rootId).find(".comment_view_ul_sub").children().length > 1) {
            // 已经获取过了
            return;
        }
        list(rootId, 1, subPageSize);
    });

    /* 根评论加载更多 */
    $("#comment_view_load_more_root").on("click", function () {
        list(null, $(this).attr("next_page"), rootPageSize)
    });

    /* 子评论加载更多 */
    $("body").on("click", ".comment_view_load_more", function () {
        list($(this).attr("root_id"), $(this).attr("next_page"), subPageSize);
    });

    /* 根评论删除 */
    $("body").on("click", ".comment_view_content_delete", function () {
        deleteCommentOfRoot($(this).attr("comment_id"));
    });

    /* 子评论删除 */
    $("body").on("click", ".comment_view_content_delete_sub", function () {
        deleteCommentOfSub($(this).attr("root_id"), $(this).attr("comment_id"));
    });
});

function deleteCommentOfRoot(commentId) {
    if (!confirm("确定删除该条评论？")) return;

    var fnSucc = function () {
        $("#j_comment_view_ul_li_" + commentId).remove();
        removeLoadMoreBtnOfRoot();
        vankiMsgAlertAutoClose("评论删除成功");
    };
    deleteComment(commentId, fnSucc);
}

function deleteCommentOfSub(rootId, commentId) {
    if (!confirm("确定删除该条回复？")) return;

    var fnSucc = function () {
        $("#j_comment_view_ul_sub_li_" + commentId).remove();
        removeLoadMoreBtnOfSub(rootId);
        subCommentReplyNumOperation(rootId, -1);
        vankiMsgAlertAutoClose("回复删除成功");
    };
    deleteComment(commentId, fnSucc);
}

function deleteComment(commentId, fnSucc) {
    if (!commentId) return;
    var params = {
        "type": ConstDB.Comment.typeNote,
        "id": commentId
    };
    vankiAjax(ConstAjaxUrl.Comment.delete, params, fnSucc);
}

function clearReply() {
    $("#j_note_comment_create_btn").html("发表评论");
    $("#j_note_comment_content").attr("placeholder", "说点什么...");
    $("#j_note_comment_retry_cancel").hide();
    $("#j_note_comment_content").val("");

    $("#j_note_comment_content").attr("root_id", "");
    $("#j_note_comment_content").attr("parent_id", "");
    $("#j_note_comment_content").attr("to_user_id", "");
}

function prepareReply(replayEle) {
    clearReply();

    replayEle = $(replayEle);
    var rootId = replayEle.attr("parent_root_id");
    var parentId = replayEle.attr("parent_id");
    var parentUserAlias = replayEle.attr("parent_user_alias");
    var parentUserId = replayEle.attr("parent_user_id");
    var parentContent = replayEle.attr("parent_content");

    if (!a_loginUserId) {
        vankiMsgAlertAutoClose("回复请先登录，谢谢！");
        return;
    }
    if (parentUserId == a_loginUserId) {
        vankiMsgAlertAutoClose("自己不能回复自己");
        return;
    }

    var placeholder = "回复 @" + parentUserAlias + "：" + parentContent;
    $("#j_note_comment_content").attr("placeholder", placeholder);

    $("#j_note_comment_content").attr("root_id", rootId);
    $("#j_note_comment_content").attr("parent_id", parentId);
    $("#j_note_comment_content").attr("to_user_id", parentUserId);

    $("#j_note_comment_retry_cancel").show();
    $("#j_note_comment_create_btn").html("回复评论");
    $("#j_note_comment_content").focus();
}

/**
 * 创建评论
 * @param rootId
 * @param parentId
 * @param toUserId
 */
function createComment(rootId, parentId, toUserId) {
    if (!rootId) rootId = ConstDB.defaultParentId;
    if (!parentId) parentId = ConstDB.defaultParentId;
    if (!toUserId) toUserId = c_noteUserId;

    var params = {
        "type": ConstDB.Comment.typeNote,
        "targetId": c_noteId,
        "fromUserId": a_loginUserId,
        "content": $("#j_note_comment_content").val(),
        "rootId": rootId,
        "parentId": parentId,
        "toUserId": toUserId
    };

    if (!params.fromUserId) {
        vankiMsgAlertAutoClose("评论请先登录，谢谢！");
        return;
    }
    if (!params.targetId || !params.fromUserId || !params.toUserId) {
        vankiLayerMsgFailTou("出问题啦！请刷新页面再试试");
        return;
    }
    if (!params.content) {
        vankiMsgAlertAutoClose("评论内容不能为空的！🙄");
        return;
    }
    if (params.content.length > 500) {
        vankiMsgAlertAutoClose("评论内容限制500字");
        return;
    }
    if (params.fromUserId == params.toUserId && parentId != ConstDB.defaultParentId) {
        vankiMsgAlertAutoClose("自己不能回复自己");
        return;
    }

    var fnSucc = function (data) {
        var rootId = data["rootId"];
        if (rootId == ConstDB.defaultParentId) {
            buildLiOfComment(data);
            removeLoadMoreBtnOfRoot();
        } else {
            buildLiOfSubComment(data);
            /**
             * 回复数＋1
             */
            subCommentReplyNumOperation(rootId, 1);
            $("#j_comment_view_ul_li_" + rootId).find(".comment_view_reply").show();

            // 新加后不允许再加载更多，数据会乱
            // $('#j_comment_view_ul_li_' + rootId).find(".comment_view_load_more").hide();
            rebuildLoadMoreInfoOfSub(rootId);
        }
        vankiLayerMsgSuccTou("评论成功！");
        clearReply();
    };

    vankiAjax(ConstAjaxUrl.Comment.create, params, fnSucc);
}

/**
 * 回复总数加减
 *
 * @param rootId
 * @param opsNum
 */
function subCommentReplyNumOperation(rootId, opsNum) {
    var rootReplyNumEle = $("#j_comment_view_ul_li_" + rootId).find(".comment_view_replyNum");
    var replyNum = rootReplyNumEle.html() || 0;
    replyNum = Number(replyNum) + opsNum;
    rootReplyNumEle.html(replyNum);
}

function list(rootId, currPage, pageSize) {
    if (!rootId) rootId = ConstDB.defaultParentId;
    if (!currPage) currPage = 1;
    if (!pageSize) pageSize = 10;

    var params = {
        "type": ConstDB.Comment.typeNote,
        "targetId": c_noteId,
        "rootId": rootId,
        "currPage": currPage,
        "pageSize": pageSize
    };

    var fnSucc = function (data) {
        if (!data) return;

        if (rootId == ConstDB.defaultParentId) {
            for (var i in data) {
                buildLiOfComment(data[i]);
            }
            rebuildLoadMoreInfo();
        } else {
            for (var i in data) {
                buildLiOfSubComment(data[i]);
            }
            rebuildLoadMoreInfoOfSub(rootId);
        }
    };

    vankiAjax(ConstAjaxUrl.Comment.listOfTarget, params, fnSucc)
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

    var subNum = comment["subNum"];
    if (subNum) {
        titleEle.find(".comment_view_replyNum").html(subNum);

        /**
         * 构建查看回复所需数据
         */
        var viewReplyEle = titleEle.find(".comment_view_reply");
        viewReplyEle.attr("comment_id", comment["id"]);
    } else {
        titleEle.find(".comment_view_reply").hide();
    }

    if (comment["fromUserId"] == c_noteUserId) {
        titleEle.find(".badge").show();
    }

    var contentEle = newLiEle.find(".comment_view_content");
    contentEle.find(".comment_view_content_data").html(comment["content"]);

    /**
     * 构建回复所需数据
     */
    var replayEle = contentEle.find(".comment_view_content_reply");
    replayEle.attr("parent_root_id", comment["id"]);
    replayEle.attr("parent_id", comment["id"]);
    replayEle.attr("parent_user_alias", comment["fromUserAlias"]);
    replayEle.attr("parent_user_id", comment["fromUserId"]);

    var content = comment["content"];
    if (content.length > 20) {
        content = content.substring(0, 20) + "...";
    }
    replayEle.attr("parent_content", content);

    /**
     * 构建删除
     */
    var delEle = contentEle.find(".comment_view_content_delete");
    if (comment["fromUserId"] == a_loginUserId || a_loginUserId == a_adminUserId) {
        delEle.attr("comment_id", comment["id"]);
        delEle.show();
    } else {
        delEle.hide();
    }

    newLiEle.show();
    $("#j_comment_view_ul").append(newLiEle);
}

function buildLiOfSubComment(comment) {
    if (!comment) return;

    var newLiEle = $('#j_comment_view_ul_sub_li_default').clone();
    newLiEle.attr('id', 'j_comment_view_ul_sub_li_' + comment["id"]);

    var userInfoEle = newLiEle.find(".comment_view_user_info_sub");
    userInfoEle.attr('href', '/' + comment["fromUserName"]);
    userInfoEle.find("img").attr("src", comment["fromUserAvatar"] || "");

    var titleEle = newLiEle.find(".comment_view_title_sub");
    titleEle.find(".comment_view_user_alias_sub").html(comment["fromUserAlias"]);
    titleEle.find(".comment_view_time_sub").html(comment["createDatetime"]);

    var contentEle = newLiEle.find(".comment_view_content_sub");

    var content = comment["content"];
    var parent = comment["parent"];
    if (parent) {
        var contentCombine = '<div style="float: left;">' + content + '</div>' +
            '<div style="float: left;"><a target="_blank" href="' + parent["fromUserName"] + '">' +
            '&nbsp;回复@' + parent["fromUserAlias"] + '：</a></div>' + parent["content"];
        contentEle.find(".comment_view_content_data_sub").html(contentCombine);
    } else {
        contentEle.find(".comment_view_content_data_sub").html(content);
    }

    if (comment["fromUserId"] == c_noteUserId) {
        titleEle.find(".badge").show();
    }

    /**
     * 构建回复所需数据
     */
    var replayEle = contentEle.find(".comment_view_content_reply_sub");
    replayEle.attr("parent_root_id", comment["rootId"]);
    replayEle.attr("parent_id", comment["id"]);
    replayEle.attr("parent_user_alias", comment["fromUserAlias"]);
    replayEle.attr("parent_user_id", comment["fromUserId"]);

    if (content.length > 20) {
        content = content.substring(0, 20) + "...";
    }
    replayEle.attr("parent_content", content);

    /**
     * 构建删除
     */
    var delEle = contentEle.find(".comment_view_content_delete_sub");
    if (comment["fromUserId"] == a_loginUserId || a_loginUserId == a_adminUserId) {
        delEle.attr("root_id", comment["rootId"]);
        delEle.attr("comment_id", comment["id"]);
        delEle.show();
    } else {
        delEle.hide();
    }

    newLiEle.css("display", "block");

    $("#j_comment_view_ul_li_" + comment["rootId"]).find(".comment_view_ul_sub").append(newLiEle);
}

/**
 * 重新更新加载更多数据
 */
function rebuildLoadMoreInfoOfSub(rootId) {
    var rootCommentLiEle = $('#j_comment_view_ul_li_' + rootId);
    var loadMoreEle = rootCommentLiEle.find(".comment_view_load_more");
    var lastLeftNum = loadMoreEle.find(".comment_view_load_more_left").html();
    if (!lastLeftNum) {
        lastLeftNum = rootCommentLiEle.find(".comment_view_replyNum").html()
    }
    lastLeftNum = Number(lastLeftNum);

    if (!lastLeftNum) {
        loadMoreEle.hide();
        return;
    }
    var leftNum = lastLeftNum - subPageSize;
    loadMoreEle.find(".comment_view_load_more_left").html(leftNum);
    if (leftNum <= 0) {
        loadMoreEle.hide();
    } else {
        loadMoreEle.show();
        var page = loadMoreEle.attr("next_page") || 1;
        loadMoreEle.attr("next_page", Number(page) + 1);
        loadMoreEle.attr("root_id", rootId);
    }
}

function rebuildLoadMoreInfo() {
    var loadMoreEle = $("#comment_view_load_more_root");
    var lastLeftNum = loadMoreEle.find(".comment_view_load_more_root_left").html();
    if (!lastLeftNum) {
        var params = {
            "type": ConstDB.Comment.typeNote,
            "targetId": c_noteId
        };
        var fnSucc = function (data) {
            lastLeftNum = data;
        };
        vankiAjax(ConstAjaxUrl.Comment.countRoot, params, fnSucc, null, null, false);
    }
    lastLeftNum = Number(lastLeftNum);
    if (!lastLeftNum) {
        loadMoreEle.hide();
        return;
    }
    var leftNum = lastLeftNum - rootPageSize;
    loadMoreEle.find(".comment_view_load_more_root_left").html(leftNum);
    if (leftNum <= 0) {
        loadMoreEle.hide();
    } else {
        loadMoreEle.show();
        var page = loadMoreEle.attr("next_page") || 1;
        loadMoreEle.attr("next_page", Number(page) + 1);
    }
}

function removeLoadMoreBtnOfRoot() {
    $('#comment_view_load_more_root').hide();
}

function removeLoadMoreBtnOfSub(rootId) {
    $('#j_comment_view_ul_li_' + rootId).find(".comment_view_load_more").hide();
}