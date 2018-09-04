$(function () {
    $("#j_tree_content_ratio").click(function () {
        var treeEle = $("#j_note_tree");
        var contentEle = $("#j_note_content");

        var treeCls = treeEle.attr("class");
        var contentCls = contentEle.attr("class");

        var newTreeCls;
        var newContentCls;

        if (treeCls == "col-xs-3") {
            newTreeCls = "col-xs-4";
            newContentCls = "col-xs-8";
        } else if (treeCls == "col-xs-4") {
            newTreeCls = "col-xs-5";
            newContentCls = "col-xs-7";
        } else {
            newTreeCls = "col-xs-3";
            newContentCls = "col-xs-9";
        }
        if (newTreeCls && newContentCls) {
            upsertRatio(newTreeCls, newContentCls, function() {
                treeEle.removeClass(treeCls);
                contentEle.removeClass(contentCls);

                treeEle.addClass(newTreeCls);
                contentEle.addClass(newContentCls);
            });
        }
    });

    function upsertRatio(treeCls, contentCls, fnSucc2) {
        if (!treeCls || !contentCls) return;

        var param = {
            "treeCssCls": treeCls,
            "contentCssCls": contentCls
        };

        var fnSucc = function () {
            if (a_curr_note_id) {
                viewNote(a_curr_note_id);
                fnSucc2()
            } else {
                window.location.href = "/" + c_noteUserName
            }
        };

        var fnFail = function () {
            vankiMsgAlertAutoClose("调整失败")
        };
        vankiAjax(ConstAjaxUrl.Config.upsertNoteTreeConfig, param, fnSucc, fnFail);
    }
});