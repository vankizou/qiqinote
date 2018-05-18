/**
 * Created by vanki on 2017/4/15.
 */
var a_note_content_json = {};   // 笔记内容数量，一般只存大于0的
var vankiEditor;
$(function () {
    /**
     * 初始化markdown工具
     * @type {string}
     */
    var initStr = "### 欢迎使用奇奇笔记！\r\n" +
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
        "\r\n" +
        ">> 6. 暂不支持自定义排序。默认排序规则：访链、私密、加密、公开\r\n" +
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

    buildMarkdownEdit(initStr, 95);

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
        var noteId = $('#j_curr_note_id').val();
        var title = $('#j_note_info_edit_title').val();
        var secretType = $('#j_note_info_edit_secret').val();
        var keyword = $('#j_note_info_edit_keyword').val();
        var contentId = $('#j_curr_note_detail_id').val();
        var content = vankiEditor.getMarkdown();

        var params = {
            "note.id": noteId,
            "note.title": title,
            "note.secret": secretType,
            "note.password": currPwd,
            "note.keyword": keyword,
            "noteDetailList[0].id": contentId,
            "noteDetailList[0].content": content
        };
        var fnSucc = function (countNoteCount) {
            vankiEditor.previewing();
            hideMarkdownCloseIcon();
            vankiMsgAlertAutoClose("保存成功");

            /**
             * 更新树节点的信息
             */
            var node = tree.getSelectedNodes()[0];
            noteSecretTypeJson[noteId] = secretType;
            node.name = title;

            tree.updateNode(node);
            updateViewTitle(title);
            $('#j_note_info_keyword').html(keyword);

            a_note_content_json[noteId] = countNoteCount;
            updateDiyDom(node, 0);
        };
        vankiAjax(ConstAjaxUrl.Note.updateById, params, fnSucc);
    });

    $('#j_note_info_edit_secret').bind('change', function () {
        var val = $(':checked').val();
        if (val == ConstDB.Note.secretPwd) {
            currPwd = prompt("请输入密码", currPwd);
            if (currPwd == null) {  // 取消恢复原来的type
                if (a_secretType != null || a_secretType != undefined) $('#j_note_info_edit_secret').val(a_secretType);
                return;
            }
        }
        var viewSecretStr = buildViewSecretStr(val, currPwd);
        $('#j_note_info_secret').html(viewSecretStr);
    })
});


function buildMarkdownEdit(val, heightDiff) {
    if (vankiEditor) vankiEditor.editor.remove();
    if ((!c_noteUserId || !c_myUserId || c_myUserId != c_noteUserId) && !val) return;

    $('#j_vanki-editormd-dynamic').append('<div id="vanki-editormd-edit-note"></div>');

    var infoHeight = $('#j_note_info_div').height();
    if (!infoHeight) infoHeight = 0;

    heightDiff = heightDiff ? heightDiff : 105 + infoHeight;
    var height = $(window).height() - heightDiff;
    vankiEditor = editormd("vanki-editormd-edit-note", {
        width: "100%",
        height: height,
        fontSize: "14px",
        tocm: true,
        emoji: false,
        taskList: true,
        tex: true,  // 默认不解析
        flowChart: true,  // 默认不解析
        sequenceDiagram: true,  // 默认不解析
        htmlDecode: "style,script,iframe,form",  // you can filter tags decode
        syncScrolling: "single",
        path: "/statics/third/markdown/lib/",
        toolbarIcons: [
            "undo", "redo", "|",
            "bold", "del", "italic", "quote", "ucwords", "uppercase", "lowercase", "|",
            "h1", "h2", "h3", "h4", "h5", "h6", "|",
            "list-ul", "list-ol", "hr", "|",
            "code", "preformatted-text", "code-block", "table", "datetime", "html-entities", "pagebreak", "|",
            "goto-line", "watch", "preview", "fullscreen", "clear", "search", "info"
            , "|", "addImage", "|", "saveNoteContent"
        ],
        toolbarIconTexts: {
            addImage: '<span style="font-size: 14px; font-weight: 700">添加图片</span>',
            saveNoteContent: '<span style="font-size: 14px; font-weight: 700">保存内容</span>'
        },
        toolbarHandlers: {
            addImage: function () {
                createAddImagePop();
            },
            saveNoteContent: function () {
                var noteId = $('#j_curr_note_id').val();
                var contentId = $('#j_curr_note_detail_id').val();
                var content = vankiEditor.getMarkdown();

                var params = {
                    "note.id": noteId,
                    "noteDetailList[0].id": contentId,
                    "noteDetailList[0].content": content
                };
                var fnSucc = function () {
                    vankiMsgAlertAutoClose("保存成功");
                };
                vankiAjax(ConstAjaxUrl.Note.updateById, params, fnSucc);
            }
        },
        onload: function () {
            this.setMarkdown(val);
            this.previewing();
            hideMarkdownCloseIcon();
        },
        onpreviewed: function () {
            showEdit();
        },
        onpreviewing: function () {
            showView();
        }
    });
}

var currPwd;    // 密码
function viewNote(noteId, msgIfNeedPwd, isNeedPwd) {
    if (noteId == ConstDB.defaultParentId) return;

    var secretType = noteSecretTypeJson[noteId];
    var password;
    if (c_myUserId != c_noteUserId && (secretType == ConstDB.Note.secretPwd || isNeedPwd)) {
        if (((password = openedPwdJson[noteId]) == undefined) || isNeedPwd) {
            password = prompt(msgIfNeedPwd);
            if (password == null) return;
        }
    }
    var idLink = noteIdAndNoteIdLinkJson[noteId];
    var params = {
        "idOrIdLink": idLink,
        "password": password,
        "is_pop": false,
    };
    var context;
    var fnSucc = function (data) {
        var noteDetailList = data['noteDetailList'];
        var val = "";
        if (noteDetailList && noteDetailList[0]) {
            $('#j_curr_note_detail_id').val(noteDetailList[0]['id']);

            if (!(val = noteDetailList[0]['content'])) val = "";
        } else {
            $('#j_curr_note_detail_id').val("");
        }
        buildViewNoteCommonInfo(val, data['note']);

        if (data['note']['secret'] == ConstDB.Note.secretLink) {
            history.pushState(null, null, "/note/" + data['note']['idLink']);
        } else {
            history.pushState(null, null, "/note/" + data['note']['id'] + ".html");
        }

        context = true;
    };
    var fnFail = function (data) {
        if (data['code'] == ConstStatusCode.CODE_1120[0]) {
            viewNote(noteId,"密码错误, 请重新输入", true)
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

    //if (!vankiEditor.state.preview) vankiEditor.previewing();
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

    // 私密
    var pwd = note['password'];
    var secret = note['secret'];
    var secretStr = buildViewSecretStr(secret, pwd);

    $('#j_note_info_secret').html(secretStr);
    $('#j_note_info_edit_secret').val(secret);

    // 关键词
    var keyword = note['keyword'];
    if (!keyword) keyword = "";
    $('#j_note_info_keyword').html(keyword);
    $('#j_note_info_edit_keyword').val(keyword);

    // 浏览数
    var viewNum = note['viewNum'];
    viewNum = numToHumanView(viewNum, null, 1);
    $('#j_note_info_viewNum').html(viewNum);
    // $('#j_note_info_edit_viewNum').html(viewNum);

    $('#j_note_info_status').html(buildStatusStr(note['status'], note['statusDescription']));
    buildMarkdownEdit(noteContentVal);
}

/**
 * 显示标题更新
 * @param originTitle
 */
function updateViewTitle(originTitle) {
    var title = originTitle;
    if (originTitle.length > 48) title = originTitle.substring(0, 48) + "...";
    $('#j_note_info_title').html('<label title="' + originTitle + '">' + title + '</label>');
    $('#j_note_info_edit_title').val(originTitle);
}

var a_secretType;
function buildViewSecretStr(secretType, pwd) {
    var secretStr = "";
    currPwd = undefined;
    switch (Number(secretType)) {
        case ConstDB.Note.secretPwd:
            secretStr = '密码访问 <i class="fa fa-question-circle-o" style="cursor: pointer;" title="密码：(' + pwd + ')"></i>';
            currPwd = pwd;
            break;
        case ConstDB.Note.secretClose:
            secretStr = "仅自己可见";
            break;
        case ConstDB.Note.secretLink:
            secretStr = '链接访问';
            break;
        default:
            secretStr = "公开";
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
            statusStr = '待审核 <i class="fa fa-question-circle-o" style="cursor: pointer;" title="笔记待审核' + statusDescription + '"></i>';
            break;
        case ConstDB.Note.statusNoPass:
            statusStr = '不通过 <i class="fa fa-question-circle-o" style="cursor: pointer;" title="审核不通过' + statusDescription + '"></i>';
            break;
        case ConstDB.Note.statusPass:
            statusStr = '全站访问 <i class="fa fa-question-circle-o" style="cursor: pointer;" title="知道链接即可访问' + statusDescription + '"></i>';
            break;
        default:
            statusStr = '不通过 <i class="fa fa-question-circle-o" style="cursor: pointer;" title="审核不通过' + statusDescription + '"></i>';
    }
    return statusStr;
}

function showView() {
    $('.note_content_common_edit').hide();
    $('.note_content_common_view').show();
}

function showEdit() {
    $('.note_content_common_view').hide();
    $('.note_content_common_edit').show();
}

function hideMarkdownCloseIcon() {
    $('.fa-close').hide();
}
