<%--
  Created by IntelliJ IDEA.
  User: vanki
  Date: 2017/2/21
  Time: 22:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!doctype html>
<html lang="zh">
<head>
    <%--<base href="<%=basePath%>">--%>
    <meta charset="utf-8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>笔记列表-奇奇笔记</title>
    <jsp:include page="../common/common.jsp"></jsp:include>
    <jsp:include page="../common/ztree.jsp"></jsp:include>
    <jsp:include page="../common/markdown.jsp"></jsp:include>
    <jsp:include page="../common/markdown-preview.jsp"></jsp:include>

    <link href="/statics/third/layer/layui/css/layui.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/statics/third/layer/layui/layui.js"></script>

    <link href="/statics/css/note/list.css" rel="stylesheet" type="text/css">
    <link href="/statics/css/note/imagePop.css" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="/statics/js/note/imagePop.js"></script>
    <script type="text/javascript" src="/statics/js/note/list_right.js"></script>
    <script type="text/javascript" src="/statics/js/note/list.js"></script>

    <%-- 悬浮 --%>
    <script type="text/javascript" src="/statics/third/float/dist/lib/modernizr.touch.js"></script>
    <script type="text/javascript" src="/statics/third/float/dist/mfb.js"></script>
    <script type="text/javascript" src="/statics/js/note/list_menu.js"></script>
    <link rel="stylesheet" href="/statics/third/float/dist/mfb.css">
    <link rel="stylesheet" href="/statics/css/note/list_menu.css">

    <script type="text/javascript">
        var c_myUserId = '${suc.user.id}';
        var c_noteUserId = '${userId}';
        var c_noteUserName = '${userName}';
        var c_noteUserAlias = '${userAlias}';
        var c_search = '${search}';
        var basePath = '<%=basePath%>';

        var a_curr_note_id;

        $(function () {
            if (c_myUserId && c_myUserId == c_noteUserId) {
                $(".myNoteUrl").addClass("topActive");
            }
        });
    </script>
</head>
<body>
<jsp:include page="../common/top.jsp"></jsp:include>

<div class="container c_body row" style="width: 100%; min-width: 1000px;">
    <div id="j_note_tree"
         class="<c:choose><c:when test="${empty treeCssCls}">col-xs-3</c:when><c:otherwise>${treeCssCls}</c:otherwise></c:choose>"
         tips="笔记树">
        <c:if test="${suc.user.id == userId}">
            <div class="input-group" style="margin: 0 10px 8px 10px; z-index: 0;">
                <input type="text" class="form-control"
                       style="height: 28px;"
                       id="j_note_tree_title_like" placeholder="搜索..."
                       value="${search}"/>
                <span class="input-group-btn">
                    <button id="j_note_tree_title_like_clear" class="btn btn-default" type="button"
                            style="height: 28px; line-height: 14px; color: #0592d3;" title="清空">
                        <i class="fa fa-times-circle"></i>
                    </button>
                    <button id="j_note_tree_title_like_go" class="btn btn-default" type="button"
                            style="height: 28px; line-height: 14px; color: #0592d3;" title="搜索">
                        <i class="fa fa-search"></i>
                    </button>
                </span>
            </div>
        </c:if>
        <ul id="noteTree" class="ztree" style="overflow: auto;"></ul>
    </div>
    <div id="j_note_content"
         class="<c:choose><c:when test="${empty contentCssCls}">col-xs-9</c:when><c:otherwise>${contentCssCls}</c:otherwise></c:choose>">
        <div tips="笔记内容">
            <div class="note_common">
                <div id="note_content_edit"><i class="fa fa-edit"></i></div>
                <div class="note_common2 row">
                    <span id="j_common_title" class="textOverflow" contenteditable="true"></span>
                    <ul class="common_meta">
                        <li title="管理员审核状态" class="default_cursor">
                            <i class="fa fa-bell-o"></i> <span id="j_note_info_status"></span>
                        </li>
                        <li title="私密状态" class="default_cursor" id="j_common_secret"></li>
                        <li title="浏览量" class="default_cursor">
                            <i class="fa fa-eye"></i> <span id="j_note_info_viewNum"></span><span
                                style="margin-left: 0">次浏览</span>
                        </li>
                        <li title="创建时间" class="default_cursor">
                            <i class="fa fa-calendar"></i> <span id="j_note_info_create_datetime"></span>
                        </li>
                        <li title="关键词">
                            <i class="fa fa-keyboard-o"></i>
                            <span id="j_note_info_keyword" class="textOverflow" contenteditable="true"></span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div id="j_vanki-editormd-dynamic"></div>
        <input id="j_curr_note_detail_id" value="" style="display: none">
        <input id="j_curr_note_id" value="" style="display: none">
    </div>

</div>

<div id="rMenu">
    <ul>
        <li id="m_add" onclick="addNote();"><i class="fa fa-plus"></i> 添加笔记</li>
        <li id="m_open" onclick="openNote();"><i class="fa fa-eye"></i> 查看内容</li>
        <li id="m_updateTitle" onclick="updateNoteTitle();"><i class="fa fa-pencil"></i> 修改标题</li>
        <li id="m_editInCurrPage" onclick="editNote();"><i class="fa fa-edit"></i> 编辑笔记</li>
        <li id="m_del" onclick="deleteNote();"><i class="fa fa-trash-o"></i> 删除笔记</li>
        <hr id="m_hr1" style="height:1px;border:none;border-top:1px solid #555555;"/>
        <li id="m_secret_open" onclick="setSecretOpen();" title="所有人可见（不关联下级）"><i class="fa fa-share"></i> 设置公开</li>
        <li id="m_secret_pwd" onclick="setSecretPwd();" title="密码访问内容（不关联下级）"><i class="fa fa-key"></i> 设置密码</li>
        <li id="m_secret_private" onclick="setSecretPrivate();" title="只能自己访问（不关联下级）"><i class="fa fa-user-o"></i> 设置私密
        </li>
        <li id="m_secret_link" onclick="setSecretLink();" title="知道链接即可访问（不关联下级）"><i class="fa fa-link"></i> 设置访链</li>
        <hr id="m_hr2" style="height:1px;border:none;border-top:1px solid #555555;"/>
        <li id="m_download" onclick="downloadNote();"><i class="fa fa-download"></i> 下载笔记</li>
    </ul>
</div>

<div id="j_imagePop">
    <div id="j_imageUploadDiv">
        <form id="j_imageUploadForm" enctype="multipart/form-data">
            <input id="j_images" type="file" class="c_upload_file" name="images"
                   accept="image/png,image/jpg,image/jpeg,image/gif"
                   multiple>
        </form>
    </div>
    <div id="j_historyImage">
        <div class="panel">
            <div class="panel-heading">
                <h3 class="panel-title">历史图片</h3>
            </div>
            <div id="j_historyImageData" class="panel-body">
            </div>
            <nav aria-label="Page navigation" class="text-center">
                <ul class="pagination">
                    <li><a href="javascript:;" style="font-size: 14px;" id="j_page_info"></a></li>
                    <li title="上一页">
                        <a href="javascript:;" aria-label="Previous"
                           id="j_page_previous" class="j_page_prev_next" val="">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <li title="下一页">
                        <a href="javascript:;" aria-label="Next" id="j_page_next" class="j_page_prev_next" val="">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                    <li class="form-inline">
                        &nbsp;<input type="text" class="form-control" size="2" id="j_page_jump_val"/>
                        <button class="btn btn_info1" id="j_page_jump">跳转</button>
                    </li>
                </ul>
            </nav>
        </div>

    </div>
</div>

<c:if test="${isMine}">
    <ul id="ratio_menu" class="menu mfb-component--bl mfb-zoomin" data-mfb-toggle="hover">
        <li class="mfb-component__wrap">
            <a id="j_tree_content_ratio" class="mfb-component__button--main" title="调整比例">
                <i class="fa fa-arrows-h"></i>
            </a>
        </li>
    </ul>
</c:if>
</body>
</html>