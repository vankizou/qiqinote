<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: vanki
  Date: 2017/2/21
  Time: 23:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!doctype html>
<html>
<head>
    <meta name="keywords" content="${noteVO.note.keyword}">
    <%--<base href="<%=basePath%>">--%>
    <title>${noteVO.note.title}-奇奇笔记</title>
    <jsp:include page="../common/common.jsp"></jsp:include>
    <jsp:include page="../common/markdown-preview.jsp"></jsp:include>
    <script type="text/javascript" src="/statics/js/note/view.js"></script>

    <link rel="stylesheet" href="/statics/css/note/view.css">

    <script type="text/javascript">
        var c_noteId = '${noteVO.note.id}';
        var c_isNeedPwd = '${noteVO.needPwd}';

        $(function() {
            changeFooterMarginTop(10);
        });
    </script>
</head>
<body>
<jsp:include page="../common/top.jsp"></jsp:include>
<div class="container c_body row c_all">
    <div class="col-xs-1 text-center c_body_left"></div>
    <div class="col-xs-10">
        <div id="j_empty_content" class="text-center"
             style="display: none; margin: 50px auto; font-size:18px; font-weight:bold;">
            笔记内容为空!&nbsp; <a href="/${noteVO.user.name == null ? noteVO.note.userId : noteVO.user.name}" target="_blank" class="btn btn_info1"> 去看看TA的笔记&nbsp;>></a>
        </div>
        <div id="layout">
            <h1 class="text-center c_title j_note_info">${noteVO.note.title}&nbsp;&nbsp;
                <small
                        id="j_note_info_parent_title">${noteVO.parentNote.title}</small>
            </h1>
            <div id="vanki-editormd-view-note">
                <textarea id="j_content" style="display: none;"><c:if
                        test="${!empty noteVO.noteDetailList && !empty noteVO.noteDetailList[0].content}">${noteVO.noteDetailList[0].content}
                </c:if></textarea>
            </div>
        </div>
    </div>
    <div class="col-xs-1 text-center c_body_right j_note_info">
        <div>
            <table class="table" style="border: 0px solid transparent !important;">
                <tbody>
                <tr>
                    <td class="col-md-4 text-center">
                        <span class="label btn_info1">作者：</span>
                    </td>
                    <td class="col-md-8 text-left">
                        <span id="j_note_info_user_alias"
                              class="label label_info1">${noteVO.user.alias}</span>
                    </td>
                </tr>
                <tr>
                    <td class="col-md-4 text-center">
                        <span class="label btn_info1">创建：</span></td>
                    <td class="col-md-8 text-left">
                        <span id="j_note_info_create_datetime"
                              class="label label_info1">${noteVO.createDatetimeStr}</span>
                    </td>
                </tr>
                <tr>
                    <td class="col-md-4 text-center">
                        <span class="label btn_info1">更新：</span></td>
                    <td class="col-md-8 text-left">
                        <span id="j_note_info_update_datetime"
                              class="label label_info1">${noteVO.updateDatetimeStr}</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div>
            <a id="j_note_info_user_url" href="/${noteVO.user.name == null ? noteVO.note.userId : noteVO.user.name}" target="_blank" class="btn btn_info1"
               style="margin-left: 8px;">去看看TA的其他笔记&nbsp;>></a>
        </div>
    </div>
</div>
<jsp:include page="../common/footer.jsp"></jsp:include>
</body>
</html>