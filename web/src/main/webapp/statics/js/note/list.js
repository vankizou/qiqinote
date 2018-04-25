/**
 * Created by vanki on 16/10/27.
 */

var setting = {
    view: {
        addDiyDom: addDiyDom
    },
    edit: {
        enable: true,
        showRemoveBtn: false,
        showRenameBtn: false
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    callback: {
        beforeDrag: beforeDrag,
        beforeDrop: beforeDrop,
        beforeRename: beforeRename,
        onRightClick: onRightClick,
        beforeCollapse: beforeCollapse,
        beforeExpand: beforeExpand,
        onClick: onDblClick,
        // onDblClick: onDblClick
    }
};

var setting_noLogin = {
    view: {
        showTitle: true,
        addDiyDom: addDiyDom
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    callback: {
        beforeCollapse: beforeCollapse,
        beforeExpand: beforeExpand,
        // onDblClick: onDblClick,
        onClick: onDblClick,
        onRightClick: onRightClick,
    }
}

// 树，右键菜单，笔记目录下的笔记数量，笔记私密类型
var tree, rMenu, childNoteNumJson = {}, noteSecretTypeJson = {}, noteIdAndNoteIdLinkJson = {};
$(document).ready(function () {
    init();

    $('#j_note_tree_title_like').keyup(function (event) {
        if (event.keyCode != 13) return;
        tree.destroy();
        init();
    });

});

function init() {
    $('#noteTree').css('height', $(window).height() - 110);
    var rootName = (c_noteUserAlias ? c_noteUserAlias : "TA") + "的笔记";
    if (c_myUserId && c_noteUserId == c_myUserId) {
        rootName = '我的笔记';
    }
    var noteTreeNodes = [{
        id: ConstDB.defaultParentId,
        pId: "ROOT",
        name: rootName,
        open: true,
        isParent: true
    }];
    if (c_myUserId == c_noteUserId) {
        $.fn.zTree.init($("#noteTree"), setting, noteTreeNodes);
    } else {
        $.fn.zTree.init($("#noteTree"), setting_noLogin, noteTreeNodes);
    }
    tree = $.fn.zTree.getZTreeObj("noteTree");
    $("#noteTree_1_span").append('<span id="j_note_tree_root_name"></span>');
    rMenu = $('#rMenu');
    buildNoteTreeNodes();
}

/**
 * 添加节点
 * @param treeId
 * @param treeNode
 */
function addNote() {
    hideRMenu();
    var parentNode = tree.getSelectedNodes()[0];
    var pId = parentNode.id;
    var pId = parentNode ? parentNode.id : ConstDB.defaultParentId;

    var params = {
        "note.parentId": pId,
        "note.title": "新笔记",
        "note.userId": c_myUserId
    };
    var fnSucc = function (data) {
        if (!data) {
            vankiMsgAlertAutoClose("添加失败");
            return false;
        }
        var secret = data['secret'];
        noteSecretTypeJson[data['id']] = secret;
        noteIdAndNoteIdLinkJson[data['id']] = secret == ConstDB.Note.secretLink ? data['idLink'] : data['id'];
        updateDiyDom(parentNode, 1);
        tree.addNodes(parentNode, {id: data["id"], pId: pId, name: data["title"]});
    };
    vankiAjax(ConstAjaxUrl.Note.add, params, fnSucc);
};

/**
 * 为节点添加自定义属性。
 * 添加目录下的笔记数量
 * @param treeId
 * @param treeNode
 */
function addDiyDom(treeId, treeNode) {
    if (treeNode.id < 0) return;
    var num = childNoteNumJson[treeNode.id];

    var spanId = 'j_diy_span_' + treeNode.id;
    $('#' + spanId).remove();

    var flag = false;

    var s = '<span id="' + spanId + '">';
    if (num && num > 0) {  // 有数量
        s += '（' + num + '）';
        flag = true;
    }
    // 私密
    var secret = noteSecretTypeJson[treeNode.id];
    if (secret) {
        var secretIId = 'j_diy_secret';

        if (secret == ConstDB.Note.secretPwd) {
            s += '<b style="color:green; font-size: 10px;" title="密码访问"> &nbsp;<i style="color: #0593d3; font-weight: 700;" class="fa fa-key"></i></b>';
            flag = true;
        } else if (secret == ConstDB.Note.secretClose) {
            s += '<b style="color:green; font-size: 10px;" title="不公开"> &nbsp;<i style="color: #0593d3; font-weight: 700;" class="fa fa-user-o"></i></b>';
            flag = true;
        } else if (secret == ConstDB.Note.secretLink) {
            s += '<b style="color:green; font-size: 10px;" title="链接访问"> &nbsp;<i style="color: #0593d3; font-weight: 700;" class="fa fa-link"></i></b>';
            flag = true;
        }
    }
    s += '</span>';
    if (flag) {
        var aObj = $("#" + treeNode.tId + "_span");
        aObj.append(s);
    }
}

/**
 * 拖拽
 * @param treeId
 * @param treeNodes
 * @returns {boolean}
 */
var beforeDragPNode;

function beforeDrag(treeId, treeNodes) {
    if (treeNodes[0].id == "-1") return false;  // 根不能拖
    beforeDragPNode = treeNodes[0].getParentNode();
    return true;
}

/**
 * 拖拽，放下
 * @param treeId
 * @param treeNodes
 * @param targetNode
 * @param moveType
 * @returns {boolean}
 */
function beforeDrop(treeId, treeNodes, targetNode, moveType) {
    if ('inner' != moveType || c_noteUserId != c_myUserId) return false;

    var srcNode = treeNodes[0];    // 一次只能拖一个

    var params = {
        "note.id": srcNode.id,
        "note.parentId": targetNode.id
    };
    var fnSucc = function () {
        updateDiyDom(targetNode, 1);    // 新笔记父节点的笔记数量加1
        if (beforeDragPNode && beforeDragPNode.id > -1) { // 旧的笔记目录中笔记数量要减一
            updateDiyDom(beforeDragPNode, -1);
        }
    }
    vankiAjax(ConstAjaxUrl.Note.updateById, params, fnSucc);
    // buildNoteTreeNodes(null, targetNode);
    return true;
}

/**
 * 更新笔记右边的固定信息
 * @param treeNode
 * @param num
 */
function updateDiyDom(treeNode, num) {
    if (!treeNode) return;
    if (!num) num = 0;
    var oldNum = childNoteNumJson[treeNode.id];
    if (!oldNum) oldNum = 0;
    oldNum += num;
    if (oldNum < 0) oldNum = 0;
    childNoteNumJson[treeNode.id] = oldNum;
    addDiyDom(null, treeNode);
}

/**
 * 修改标题触发任务
 */
function updateNoteTitle() {
    hideRMenu();
    var node = tree.getSelectedNodes()[0];
    setTimeout(function () {
        setTimeout(function () {
            tree.editName(node);
        }, 0);
    }, 0);
}

/**
 * 修改标题(工作者)
 * @param treeId
 * @param treeNode
 * @param newName
 * @param isCancel
 * @returns {boolean}
 */
function beforeRename(treeId, treeNode, newName, isCancel) {
    if (newName.length == 0 || newName.length > 200) {
        setTimeout(function () {
            tree.cancelEditName();
        }, 0);
        vankiMsgAlertAutoClose(ConstStatusCode.CODE_1102[1]);
        return false;
    }
    var params = {
        "note.id": treeNode.id,
        "note.title": newName
    };
    var fnSucc = function () {
        /**
         * 修改右侧信息
         */
        updateViewTitle(newName);
        addDiyDom(null, treeNode);
    };
    vankiAjax(ConstAjaxUrl.Note.updateById, params, fnSucc);
    return true;
}

function editNote() {
    hideRMenu();
    var node = tree.getSelectedNodes()[0];
}

function setSecretOpen() {
    setSecretCommon(ConstDB.Note.secretOpen);
}

function setSecretLink() {
    setSecretCommon(ConstDB.Note.secretLink);
}

function setSecretPwd() {
    setSecretCommon(ConstDB.Note.secretPwd);
}

function setSecretPrivate() {
    setSecretCommon(ConstDB.Note.secretClose);
}

function setSecretCommon(secretType) {
    if (secretType == undefined || secretType == null) return;
    hideRMenu();

    var node = tree.getSelectedNodes()[0];

    var params = {
        "note.id": node.id,
        "note.secret": secretType
    };

    if (secretType == ConstDB.Note.secretPwd) {
        var pwd = prompt("设置密码");
        if (pwd == null) return false;
        params['note.password'] = pwd;
    }

    var fnSucc = function () {
        noteSecretTypeJson[node.id] = secretType;
        updateDiyDom(node);

        /**
         * 修改右侧信息
         */
        var secretStr = buildViewSecretStr(secretType, params['note.password']);
        $('#j_note_info_secret').html(secretStr);
        $('#j_note_info_edit_secret').val(secretType);
    }
    vankiAjax(ConstAjaxUrl.Note.updateById, params, fnSucc);
}

/**
 * 删除笔记
 */
function deleteNote() {
    hideRMenu();
    var node = tree.getSelectedNodes()[0];
    var name = "«" + node.name + "»";
    if (node.isParent) {
        name += "及该目录下所有笔记"
    }
    if (!confirm("确定删除" + name + "？")) return;

    var params = {
        "id": node.id
    };
    var fnSucc = function () {
        vankiMsgAlertAutoClose("删除成功");
        tree.removeNode(node);
        updateDiyDom(node.getParentNode(), -1);
    };
    vankiAjax(ConstAjaxUrl.Note.deleteById, params, fnSucc);
}

/**
 * 双击打开笔记
 * @param event
 * @param treeId
 * @param treeNode
 */
function onDblClick(event, treeId, treeNode) {
    if (!treeNode) return;

    if (treeNode.id != ConstDB.defaultParentId && treeNode.isParent) {
        if (treeNode.open) {
            tree.expandNode(treeNode, false, null, null, null);
            beforeCollapse(treeId, treeNode)
            return;
        } else {
            tree.expandNode(treeNode, true, null, null, null);
            beforeExpand(treeId, treeNode)
        }
    }
    openNote(treeNode.id, treeNode);
}

function openNote(noteId, treeNode) {
    if (!noteId) {
        var node = tree.getSelectedNodes()[0];
        noteId = node.id;
        if (!noteId) return;
    }

    var flag = viewNote(noteId, "请输入密码", false);
    if (flag) {
        buildNoteTreeNodes(treeNode.id, treeNode);
    }
}

function beforeCollapse(treeId, treeNode) {
    var nodeId = treeNode.id;
    if (c_noteUserId == c_myUserId) {
        var params = {id: nodeId, noteUserId: c_noteUserId};
        vankiAjax(ConstAjaxUrl.Note.closeNote, params);
    }
    return (nodeId != ConstDB.defaultParentId && treeNode.collapse != false);
}

// 保存已经打开过的节点的密码
var openedPwdJson = {};

function beforeExpand(treeId, treeNode) {
    if (c_noteUserId == c_myUserId) {
        var params = {id: treeNode.id, noteUserId: c_noteUserId};
        vankiAjax(ConstAjaxUrl.Note.openNote, params);
    }
    return (treeNode.expand !== false);
}

/**
 * 追加笔记节点
 * @param parentId
 * @param parentNode
 */
function buildNoteTreeNodes(parentId, parentNode) {
    if (!parentId) parentId = ConstDB.defaultParentId;
    if (!parentNode) {
        parentNode = tree.getNodeByParam("id", parentId);
    }
    var existsNodeList = parentNode['children'];

    var existsNodeIdArr = [];
    if (existsNodeList) {
        for (var index in existsNodeList) {
            existsNodeIdArr.push(existsNodeList[index].id);
        }
    }

    var params = {
        "userId": c_noteUserId,
        "parentId": parentId,
        "titleLike": $('#j_note_tree_title_like').val()
    }
    var fnSucc = function (data) {
        var noteTreeNodes = [];
        buildNodeJson(data['notes'], noteTreeNodes, existsNodeIdArr);
        if (noteTreeNodes.length == 0) return;
        if (data["total"]) {
            $('#j_note_tree_root_name').html("（" + data["total"] + "）")
        }
        tree.addNodes(parentNode, noteTreeNodes);
    };
    vankiAjax(ConstAjaxUrl.Note.listOfNoteTreeVO, params, fnSucc);
}

/**
 * 构建新节点的json数据
 * @param data
 * @param noteTreeNodes
 * @param existsNodeIdArr
 */
function buildNodeJson(data, noteTreeNodes, existsNodeIdArr) {
    if (!data) return;
    for (var i in data) {
        var note = data[i]['note'];

        var noteId = note['id'];
        var noteSecret = note['secret'];

        noteSecretTypeJson[noteId] = noteSecret;
        noteIdAndNoteIdLinkJson[noteId] = noteSecret == ConstDB.Note.secretLink ? note['idLink'] : note['id'];

        if (existsNodeIdArr.indexOf(noteId) != -1) continue;    // 节点已存在

        var noteCountNote = note["noteNum"];

        if (noteCountNote > 0) childNoteNumJson[note['id']] = noteCountNote;
        var subNoteVOList = data[i]['subNoteVOList'];
        var title = note['title'];
        if (title.length > 32) title = title.substring(0, 32) + "...";

        noteTreeNodes.push({
            id: noteId,
            pId: note['parentId'],
            name: title,
            open: noteCountNote > 0 && subNoteVOList && subNoteVOList.length > 0 ? true : false,
            isParent: noteCountNote > 0 ? true : false
        });
        var countNoteContent = note['noteContentNum'];
        if (countNoteContent > 0) {
            a_note_content_json[noteId] = countNoteContent;
        } else {
            delete a_note_content_json[noteId];
        }
        if (subNoteVOList && subNoteVOList.length > 0) buildNodeJson(subNoteVOList, noteTreeNodes, existsNodeIdArr);
    }

}

/**
 * 冻结根节点
 * @param treeId
 * @param treeNode
 * @returns {boolean}
 */
function dblClickExpand(treeId, treeNode) {
    return treeNode.level > -2;
}

/*================右键菜单==================*/
function onRightClick(event, treeId, treeNode) {
    if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
        tree.cancelSelectedNode();
        showRMenu(treeNode.id, "root", event.pageX, event.pageY);
    } else if (treeNode.id == ConstDB.defaultParentId) {
        tree.selectNode(treeNode);
        showRMenu(treeNode.id, 'myRoot', event.pageX, event.pageY);
    } else if (treeNode && !treeNode.noR) {
        tree.selectNode(treeNode);
        showRMenu(treeNode.id, "node", event.pageX, event.pageY);
    }
}

function showRMenu(noteId, type, x, y) {
    // $("#rMenu ul").show();

    if (type == 'myRoot') {
        if (!c_myUserId || c_noteUserId != c_myUserId) {
            hideRMenu();
            return;
        }
        $('#m_add').show();

        $('#m_open').hide();
        $('#m_updateTitle').hide();
        $('#m_editInCurrPage').hide();
        $('#m_del').hide();

        $('#m_hr1').hide();
        $('#m_secret_open').hide();
        $('#m_secret_link').hide();
        $('#m_secret_pwd').hide();
        $('#m_secret_private').hide();

        $('#m_hr2').hide();
        $('#m_download').hide();
    } else if (type == 'node') {
        if (c_noteUserId == c_myUserId) {
            $('#m_open').show();
            $('#m_add').show();
            $('#m_updateTitle').show();
            $('#editNote').show();
            $('#m_del').show();

            $('#m_hr1').show();
            $('#m_secret_open').show();
            $('#m_secret_link').show();
            $('#m_secret_pwd').show();
            $('#m_secret_private').show();

            // 有笔记内容的才能导出下载
            if (a_note_content_json[noteId] && a_note_content_json[noteId] > 0) {
                $('#m_hr2').show();
                $('#m_download').show();
            } else {
                $('#m_hr2').hide();
                $('#m_download').hide();
            }
        } else {
            $('#m_open').show();
            $('#m_add').hide();
            $('#m_updateTitle').hide();
            $('#m_editInCurrPage').hide();
            $('#m_del').hide();

            $('#m_hr1').hide();
            $('#m_secret_open').hide();
            $('#m_secret_link').hide();
            $('#m_secret_pwd').hide();
            $('#m_secret_private').hide();

            $('#m_hr2').hide();
            $('#m_download').hide();

            if (a_note_content_json[noteId] && a_note_content_json[noteId] > 0) {
                $('#m_hr2').show();
                $('#m_download').show();
            } else {
                $('#m_hr2').hide();
                $('#m_download').hide();
            }
        }
    } else {
        hideRMenu();
        return;
    }
    rMenu.css({"top": y + "px", "left": x + "px", "visibility": "visible"});
    $("body").bind("mousedown", onBodyMouseDown);
}

function hideRMenu() {
    if (rMenu) rMenu.css({"visibility": "hidden"});
    $("body").unbind("mousedown", onBodyMouseDown);
}

function onBodyMouseDown(event) {
    if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length > 0)) {
        rMenu.css({"visibility": "hidden"});
    }
}

function downloadNote(password) {
    var node = tree.getSelectedNodes()[0];
    if (!node) return;
    var noteId = node.id;

    password = password ? password : openedPwdJson[noteId];
    var params = {
        is_pop: false,
        id: noteId,
        password: password
    };
    var fnSucc = function (data) {
        console.info("noteId = " + noteId + ", currPwd = " + password)
        openedPwdJson[noteId] = password;
        window.location = "/note/doDownload.json?id=" + noteId + "&password=" + password;
    };
    var fnFail = function (data) {
        if (data['code'] != ConstStatusCode.CODE_1100[0]) return;
        var tempPwd = prompt("请输入密码");
        if (tempPwd == null) return false;
        downloadNote(tempPwd);
    };
    vankiAjax(ConstAjaxUrl.Note.download, params, fnSucc, fnFail);
}

/*================右键菜单==================*/