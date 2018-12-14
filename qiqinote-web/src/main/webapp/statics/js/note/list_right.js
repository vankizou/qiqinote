/**
 * Created by vanki on 2017/4/15.
 */
var a_note_content_json = {};   // 笔记内容数量，一般只存大于0的
var vankiEditor;
var initMDStr;
var defaultKeyword = "请输入关键词";
var titleTmp;
var keywordTmp;
$(function () {
    if (!c_isMine) {
        $("#note_content_edit").hide();
        $("#j_common_title").attr("contenteditable", "false");
        $("#j_note_info_keyword").attr("contenteditable", "false");
    }
    /**
     * 初始化markdown工具
     * @type {string}
     */
    initMDStr = "### 欢迎使用奇奇笔记！\r\n" +
        "> 功能清单\r\n" +
        ">> 1. 奇奇笔记支持Markdown\r\n" +
        "\r\n" +
        ">> 2. 在菜单列表右击会有相应操作\r\n" +
        "\r\n" +
        ">> 3. 单击菜单列表打开 笔记/目录 \r\n" +
        "\r\n" +
        ">> 4. 目录也是笔记的一种\r\n" +
        "\r\n" +
        ">> 5. 支持拖拽更换笔记目录\r\n" +
        "------------\r\n" +
        "\r\n" +
        "> 注意点\r\n" +
        ">> 1. 访问网站主页以及笔记详情页时，只有`公开`的笔记才会上`主页`以及`右侧推荐栏`，其它类型都不会\r\n" +
        "\r\n" +
        ">> 2. 访问他人主页时，可以看到`公开和加密`的 目录/笔记，但若父目录为`私密或访链`，子 目录/笔记 则不管为何种状态都不可见（但不影响上主页）\r\n" +
        "\r\n" +
        ">> 3. 目录设置`私密`不会影响`子 目录/笔记` 公开或加密，反之设置`私密或加密的目录下`若有`公开`笔记是会上`主页`的\r\n" +
        "\r\n" +
        ">> 4. 访链的笔记与私密类似, 但是访链知道笔记链接可直接访问\r\n" +
        "------------\r\n" +
        "\r\n" +
        "> **记录生活，记录点滴！**\r\n" +
        ">> **你的笔记，大家的财富！**\r\n" +
        "\r\n" +
        "#### <a href='" + basePath + "info/markdown/case.html' target='_blank'>常用Markdown操作</a>";

    initMD();

    /**
     * 点击编辑
     */
    $('#j_note_edit').bind('click', function () {
        vankiEditor.previewed();
    });

    /**
     * 点击保存
     */
    $('#j_note_save').bind('click', function () {
        updateNote();
    });

    $('#j_note_info_edit_secret').bind('change', function () {
        var val = $(':checked').val();
        if (val == ConstDB.Note.secretPwd) {
            currPwd = prompt("请输入密码", currPwd);
            if (currPwd == null) {  // 取消恢复原来成的type
                if (a_secretType != null || a_secretType != undefined) $('#j_note_info_edit_secret').val(a_secretType);
                return;
            }
        }
        // var viewSecretStr = buildViewSecretStr(val, currPwd);
        // $('#j_note_info_secret').text(viewSecretStr);
        setViewSecretStr(val, currPwd);
    });

    /**
     * 修改标题
     */
    $("#j_common_title").focus(function () {
        var ele = $(this);
        ele.removeClass("textOverflow");
        titleTmp = [$('#j_curr_note_id').val(), ele.text()];
    });

    $("#j_common_title").blur(function () {
        var ele = $(this);
        ele.addClass("textOverflow");
        var title = ele.text();

        // 编辑时，切换笔记
        var noteId = $('#j_curr_note_id').val();
        if (titleTmp && titleTmp[0] != noteId) {
            titleTmp = undefined;
            return;
        }

        if (title && titleTmp[1] != title) {
            updateNote(true, function (data, context) {
                ele.text(context["title"][1]);   // 更新失败
            });

            // beforeRename(null, null, title, null);
        } else {
            ele.text(titleTmp[1]);
        }
        titleTmp = undefined;
    });

    $("#j_common_title").keydown(function (event) {
        var ele = $(this);
        if (event.keyCode == 13) {
            return false;
        }
    });

    $("#j_common_title").keyup(function (event) {
        var ele = $(this);
        if (event.keyCode == 13) {
            ele.blur();
        }
    });

    /**
     * 修改关键词
     */
    $("#j_note_info_keyword").focus(function () {
        var ele = $(this);
        ele.removeClass("textOverflow");
        var keyword = ele.text();
        var noteId = $('#j_curr_note_id').val();
        if (keyword == defaultKeyword) {
            keywordTmp = [noteId, undefined];
            ele.text("");
        } else {
            keywordTmp = [noteId, keyword]
        }
    });

    $("#j_note_info_keyword").blur(function () {
        var ele = $(this);
        ele.addClass("textOverflow");

        // 编辑时切换笔记
        var noteId = $('#j_curr_note_id').val();
        if (keywordTmp && keywordTmp[0] != noteId) {
            keywordTmp = undefined;
            return;
        }

        var keyword = ele.text();
        if (keyword == defaultKeyword) keyword = undefined;

        if (keywordTmp && keywordTmp[1] != keyword && (keywordTmp[1] || keyword)) {
            updateNote(true, function (data, context) {
                ele.text(context["keyword"][1]);   // 更新失败
            });
        } else {
            if (keywordTmp && keywordTmp[1] && keywordTmp[1].trim() != "" && keyword.trim() != "") {
                ele.text(keywordTmp[1])
            } else {
                ele.text(defaultKeyword);
            }
        }
        keywordTmp = undefined;
    });

    $("#j_note_info_keyword").keydown(function (event) {
        var ele = $(this);
        if (event.keyCode == 13) {
            return false;
        }
    });

    $("#j_note_info_keyword").keyup(function (event) {
        var ele = $(this);
        if (event.keyCode == 13) {
            ele.blur();
        }
    });

    /**
     * 编辑内容
     */
    $("#note_content_edit").click(function () {
        vankiEditor.previewed();
    });

    /*$("#j_vanki-editormd-dynamic").dblclick(function () {
        if (!c_isMine || isInitedMD) return;
        if (!vankiEditor.state.preview) {// 编辑状态
            return;
        }

        if (vankiEditor.state.preview) {
            vankiEditor.previewed();
        } else {
            vankiEditor.fullscreenExit();
        }
    });*/

    nClickEvent($("#j_vanki-editormd-dynamic"), function() {
        if (!c_isMine || isInitedMD) return;
        if (!vankiEditor.state.preview) {// 编辑状态
            return;
        }

        if (vankiEditor.state.preview) {
            vankiEditor.previewed();
        } else {
            vankiEditor.fullscreenExit();
        }
    });

    /**
     * 修改秘密状态
     */
    $("#j_note_info_secret_dropdown span").click(function () {
        setSecretCommon($(this).attr("secrettype"));
    });

});

function nClickEvent(dom, fn, n) {
    if (!n) n = 3;
    var count = 0,
        lastTime = 0;//用于记录上次结束的时间
    var handler = function (event) {
        var currentTime = new Date().getTime();//获取本次点击的时间
        count = (currentTime - lastTime) < 500 ? count + 1 : 0;//如果本次点击的时间和上次结束时间相比大于500毫秒就把count置0
        lastTime = new Date().getTime();
        if (count >= n - 1) {
            fn(event, n);
            count = 0;
        }
    };
    dom.bind('click', handler);
}

window.onresize = function () {
    /**
     * 左边菜单动态高度
     */
    changeLeftHeight();

    /**
     * 右边的markdown编辑动态高度
     */
    var nch = $('.note_common2').height();
    if (!nch || nch <= 0) {
        nch = 95;
    } else {
        nch = 0;
    }
    vankiEditor.height(this.getEditorHeight(nch));
};

var isInitedMD = true;

function initMD() {
    buildMarkdownEdit(initMDStr, 95);
    isInitedMD = true;
}

function updateNote(justUpdateCommon, failFn) {
    var noteId = $('#j_curr_note_id').val();
    var title = $('#j_common_title').text();
    var keyword = $('#j_note_info_keyword').text();

    if (keyword == defaultKeyword || keyword.trim() == "") {
        keyword = "";
    }

    var params = {
        "note.id": noteId,
        "note.title": title,
        // "note.secret": secretType,
        "note.password": currPwd,
        "note.keyword": keyword
    };
    if (!justUpdateCommon) {
        var contentId = $('#j_curr_note_detail_id').val();
        var content = vankiEditor.getMarkdown();

        params["noteDetailList[0].id"] = contentId;
        params["noteDetailList[0].content"] = content;
    }

    var fnSucc = function (countNoteCount) {
        delete noDetailNodeId[noteId];

        vankiMsgAlertAutoClose("保存成功");

        /**
         * 更新树节点的信息
         */
        var node = tree.getSelectedNodes()[0];
        // noteSecretTypeJson[noteId] = secretType;
        node.name = title;

        tree.updateNode(node);
        updateViewTitle(title);
        $('#j_note_info_keyword').text(keyword);

        a_note_content_json[noteId] = countNoteCount;
        updateDiyDom(node, 0);
    };
    vankiAjax(ConstAjaxUrl.Note.updateById, params, fnSucc, failFn, {"title": titleTmp, "keyword": keywordTmp});
}

function updateNoteContent(fnSucc2) {
    var noteId = $('#j_curr_note_id').val();
    var contentId = $('#j_curr_note_detail_id').val();
    var content = vankiEditor.getMarkdown();

    var params = {
        "note.id": noteId,
        "noteDetailList[0].id": contentId,
        "noteDetailList[0].content": content
    };
    var fnSucc = function () {
        delete noDetailNodeId[noteId];
        if (fnSucc2) fnSucc2();
        vankiMsgAlertAutoClose("保存成功");
    };
    vankiAjax(ConstAjaxUrl.Note.updateById, params, fnSucc);
}

function getEditorHeight(heightDiff) {
    var infoHeight = $('#j_note_info_div').height();
    if (!infoHeight) infoHeight = 0;
    heightDiff = heightDiff ? heightDiff : 160 + infoHeight;
    return $(window).height() - heightDiff;
}

function buildMarkdownEdit(val, heightDiff) {
    if (vankiEditor) vankiEditor.editor.remove();
    if ((!c_noteUserId || !c_myUserId || c_myUserId != c_noteUserId) && !val) return;

    $('#j_vanki-editormd-dynamic').append('<div id="vanki-editormd-edit-note"></div>');

    var height = this.getEditorHeight(heightDiff);
    vankiEditor = editormd("vanki-editormd-edit-note", {
        width: "100%",
        height: height,
        fontSize: "14px",
        tocm: true,
        emoji: false,
        taskList: false,
        tex: false,  // 默认不解析
        flowChart: false,  // 默认不解析
        sequenceDiagram: false,  // 默认不解析
        htmlDecode: "style,script,iframe,form",  // you can filter tags decode
        syncScrolling: "single",
        path: "/statics/third/markdown/lib/",
        toolbarIcons: [
            "undo", "redo", "|",
            "bold", "del", "italic", "quote", "ucwords", "uppercase", "lowercase", "|",
            "h1", "h2", "h3", "h4", "h5", "h6", "|",
            "list-ul", "list-ol", "hr", "|",
            "link", "reference-link", "image",
            "code", "preformatted-text", "code-block", "table", "datetime", "html-entities", "pagebreak", "|",
            "goto-line", "watch", "preview", "search"
            , "|", "addImage", "fullscreen", "saveNoteContent"
        ],
        toolbarIconsClass: {
            addImage: "fa-upload",
            // saveNoteContent: "fa-save",
            fullscreen: ""
        },
        toolbarIconTexts: {
            addImage: '<span style="font-size: 14px; font-weight: 700">添加本地图片</span>',
            saveNoteContent: '<span style="font-size: 14px; font-weight: 700">保存</span>',
            fullscreen: '<span style="font-size: 14px; font-weight: 700">保存并退出</span>'
        },
        lang: {
            toolbar: {
                addImage: "添加本地图片",
                saveNoteContent: "保存笔记内容"
            }
        },
        toolbarHandlers: {
            addImage: function () {
                createAddImagePop();
            },
            saveNoteContent: function () {
                updateNoteContent();
            }
        },
        onload: function () {
            this.setMarkdown(val);
            this.previewing();
            hideMarkdownCloseIcon();
        },
        onpreviewed: function () {
            if (!vankiEditor.state.fullscreen) {
                vankiEditor.fullscreen();
            }
            showEdit();
        },
        onpreviewing: function () {
            /**
             * 关注md弹出框
             */
            if (this.editor.dialogCancelFn) {
                for (var i in this.editor.dialogCancelFn) {
                    if (this.editor.dialogCancelFn[i]) {
                        this.editor.dialogCancelFn[i]();
                    }
                }
            }
            /**
             * 关注图片弹出框
             */
            if (uploadImageLayerIndex) {
                layer.close(uploadImageLayerIndex);
            }

            // 打开显示样式
            showView();
            /**
             * 1. 全屏预览禁用Esc
             * 2. 全屏编辑按Esc退出全屏并预览
             */
            $(document).keyup(function (event) {
                if (event.keyCode == 27) {
                    if (vankiEditor.state.fullscreen) {
                        if (!vankiEditor.state.preview) {
                            vankiEditor.fullscreenExit();
                        }
                        return false;
                    }
                } else {
                    return true
                }
            });
            if (!vankiEditor.state.fullscreen) {
                hideMarkdownCloseIcon();
            }
        },
        onfullscreen: function () {
            $("#ratio_menu").hide();
        },
        onfullscreenExit: function () {
            if (!vankiEditor.state.preview) {
                vankiEditor.previewing();
            }
            updateNoteContent(function () {
                hideMarkdownCloseIcon();
            });
            $("#ratio_menu").show();
        }
    });
    isInitedMD = false;
}

var currPwd;    // 密码
function viewNote(noteId, msgIfNeedPwd, isNeedPwd) {
    if (noteId == ConstDB.defaultParentId) return;

    if (!msgIfNeedPwd) msgIfNeedPwd = "请输入密码";

    var secretType = noteSecretTypeJson[noteId];
    var password;
    if (c_myUserId != c_noteUserId && (secretType == ConstDB.Note.secretPwd || isNeedPwd)) {
        if (((password = openedPwdJson[noteId]) == undefined) || isNeedPwd) {
            password = prompt(msgIfNeedPwd);
            if (password == null) return;
        }
    } else if (noteId in noDetailNodeId) {
        // 无内容，不请求
        return
    }
    var idLink = noteIdAndNoteIdLinkJson[noteId];
    var params = {
        "idOrIdLink": idLink,
        "password": password,
        "is_pop": false
    };
    var context;
    var fnSucc = function (data) {
        a_curr_note_id = noteId;

        var noteDetailList = data['noteDetailList'];
        var val = "";
        if (noteDetailList && noteDetailList[0]) {
            $('#j_curr_note_detail_id').val(noteDetailList[0]['id']);

            if (!(val = noteDetailList[0]['content'])) val = "";
        } else {
            $('#j_curr_note_detail_id').val("");

            /**
             * 没有内容的笔记下次将不再请求
             */
            if (data['note']['noteNum']) {
                noDetailNodeId[noteId] = noteId
            }
        }
        buildViewNoteCommonInfo(val, data['note']);

        $(".note_common").css("display", "block");

        if (data['note']['secret'] == ConstDB.Note.secretLink) {
            history.pushState(null, null, "/note/" + data['note']['idLink']);
        } else {
            history.pushState(null, null, "/note/" + data['note']['id'] + ".html");
        }

        context = true;
    };
    var fnFail = function (data) {
        if (data['code'] == ConstStatusCode.CODE_1120[0]) {
            viewNote(noteId, "密码错误, 请重新输入", true)
        }
    };
    vankiAjax(ConstAjaxUrl.Note.getNoteVOById, params, fnSucc, fnFail, null, false);
    return context;
}

/**
 * 查看笔记时该笔记公共信息
 * @param note
 */
function buildViewNoteCommonInfo(noteContentVal, note) {
    if (!note) return;
    if (note['secret'] == ConstDB.Note.secretPwd) {
        openedPwdJson[note['id']] = note['password'];
    }

    showView();
    hideMarkdownCloseIcon();

    if (c_myUserId && c_myUserId == c_noteUserId) {
        $('#j_note_edit').parent().show();
    } else {
        $('#j_note_edit').parent().hide();
    }

    $('#j_curr_note_id').val(note['id']);

    // 标题
    var originTitle = note['title'];
    updateViewTitle(originTitle);

    // 审核状态
    $('#j_note_info_status').html(buildStatusStr(note['status'], note['statusDescription']));

    // 私密
    var pwd = note['password'];
    var secret = note['secret'];
    // $('#j_common_secret').html(buildViewSecretStr(secret, pwd));
    setViewSecretStr(secret, pwd);

    // 浏览数
    var viewNum = note['viewNum'];
    $('#j_note_info_viewNum').text(numToHumanView(viewNum, null, 1));

    // 创建时间
    var createDatetime = note['createDatetime'];
    $('#j_note_info_create_datetime').text(createDatetime);

    // 关键词
    var keyword = note['keyword'];
    if (!keyword || keyword.trim() == '') keyword = defaultKeyword;
    if (!c_isMine) keyword = "未设置关键词";
    var keywordEle = $('#j_note_info_keyword');
    keywordEle.text(keyword);

    keywordEle.attr("title", keyword);
    buildMarkdownEdit(noteContentVal);
}

/**
 * 显示标题更新
 * @param originTitle
 */
function updateViewTitle(originTitle) {
    var title = originTitle;
    var titleEle = $('#j_common_title');
    titleEle.text(title);
    titleEle.attr("title", title);
}

var a_secretType;

function setViewSecretStr(secretType, pwd) {
    currPwd = undefined;
    secretType = Number(secretType);
    var ele = $("#j_note_info_secret_dropdown span[secrettype=" + secretType + "]");
    if (secretType == ConstDB.Note.secretPwd) {
        ele.attr("title", "密码（" + pwd + "）");
    } else {
        $("#j_note_info_secret_dropdown span[secrettype=" + ConstDB.Note.secretPwd + "]").removeAttr("title");
    }
    var activeEle = $("#j_note_info_secret_active");
    activeEle.html("");
    ele.clone(false).appendTo(activeEle);
    a_secretType = secretType;
}

function buildViewSecretStr(secretType, pwd) {
    var secretStr = "";
    currPwd = undefined;
    switch (Number(secretType)) {
        case ConstDB.Note.secretPwd:
            secretStr = '<i class="fa fa-key" style="fvertical-align: middle;font-weight:500;cursor: pointer;" title="密码：(' + pwd + ')"> 密码访问</i><span id="j_note_info_secret" style="display: none">' + secretType + '</span>';
            currPwd = pwd;
            break;
        case ConstDB.Note.secretClose:
            secretStr = '<i class="fa fa-user-o" style="vertical-align: middle;font-weight:500;"> 仅自己可见</i><span id="j_note_info_secret" style="display: none">' + secretType + '</span>';
            break;
        case ConstDB.Note.secretLink:
            secretStr = '<i class="fa fa-link" style="vertical-align: middle;font-weight:500;"> 链接访问</i><span id="j_note_info_secret" style="display: none">' + secretType + '</span>';
            break;
        default:
            secretStr = '<i class="fa fa-share" style="vertical-align: middle;font-weight:500;"> 公开</i><span id="j_note_info_secret" style="display: none">' + secretType + '</span>';
    }
    a_secretType = secretType;
    return secretStr;
}

function buildStatusStr(status, statusDescription) {
    var statusStr = "";
    if (statusDescription) {
        statusDescription = "（" + statusDescription + "）";
    } else {
        statusDescription = "";
    }

    switch (Number(status)) {
        case ConstDB.Note.statusExaming:
            statusStr = '<span class="vertical-align: middle;" title="正在审核中，请耐心等候...' + statusDescription + '">待审核</span>';
            break;
        case ConstDB.Note.statusNoPass:
            statusStr = '<span class="vertical-align: middle;" title="审核不通过，请修改后再提交' + statusDescription + '">审核不通过</span>';
            break;
        case ConstDB.Note.statusPass:
            statusStr = '<span class="vertical-align: middle;" title="审核通过' + statusDescription + '">审核通过</span>';
            break;
        default:
            statusStr = '<span class="vertical-align: middle;" title="审核不通过，请修改后再提交' + statusDescription + '">审核不通过</span>';
    }
    return statusStr;
}

function showView() {
    var ele = $("#note_content_edit");
    ele.html('<i title="编辑内容（快捷键：鼠标在内容框三击左键）" class="fa fa-edit"></i>');
}

function showEdit() {
    var ele = $("#note_content_edit");
    ele.html('<i title="保存修改" class="fa fa-spinner fa-spin"></i>');
}

function hideMarkdownCloseIcon() {
    $('.fa-close').hide();
}

