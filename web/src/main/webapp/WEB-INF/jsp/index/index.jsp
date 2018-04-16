<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!doctype html lang="zh-CN">
<html>
<head>
    <base href="<%=basePath%>">
    <meta name="baidu-site-verification" content="vzLAJqujvV"/>
    <meta charset="utf-8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Expires" content="0"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <title>主页-奇奇笔记</title>

    <jsp:include page="../common/common.jsp"></jsp:include>
    <link rel="stylesheet" href="/statics/css/index/index.css">
</head>
<body>
<script type="text/javascript" src="/statics/third/popwindow/jquery.js"></script>
<jsp:include page="../common/top.jsp"></jsp:include>
<script type="text/javascript" src="/statics/js/index/index.js"></script>

<script type="text/javascript">
    $(function () {
        $("#copyright").show().css("margin-top", "80px");
    });
</script>

<div class="c_body">
    <div class="c_index_body">
        <div class="panel panel-info" id="j_note_list">
            <!-- Default panel contents -->
            <div class="panel-heading" style="height: 42px;">
                <div class="panel-title">
                    <div class="col-xs-10">大神笔记列表</div>
                    <div class="col-xs-2">
                        <input type="text" class="form-control" style="height: 26px;" id="j_note_title_like"
                               placeholder="搜索..."/>
                    </div>
                </div>
            </div>
            <%--<div class="panel-body">
            </div>--%>

            <!-- Table -->
            <table class="table table-hover">
                <thead>
                <tr>
                    <td class="text-center">标题</td>
                    <td>所属</td>
                    <td>作者</td>
                    <td>创建时间</td>
                    <td>浏览量</td>
                </tr>
                </thead>
                <tbody id="j_note_list_table">
                <c:forEach var="vo" items="${data.data}">
                    <tr>
                        <td><a href="/note/${vo.note.idLink}" target="_blank">${vo.note.title}</a></td>
                        <td>
                            <c:if test="${vo.parentNote != null}">${vo.parentNote.title}</c:if>
                        </td>
                        <td><a href="/${vo.user.name}" target="_blank">${vo.user.alias}</a></td>
                        <td>${vo.note.createDatetime}</td>
                        <td>${vo.note.viewNum}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <nav aria-label="Page navigation" class="text-center">
                <ul class="pagination">
                    <li><a href="javascript:;" style="font-size: 14px;" id="j_page_info">1/${data.endPage}</a></li>
                    <li title="上一页">
                        <a href="javascript:;" aria-label="Previous"
                           id="j_page_previous" class="j_page_prev_next" val="1">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <li title="下一页">
                        <a href="javascript:;" aria-label="Next" id="j_page_next" class="j_page_prev_next" val="2">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                    <li class="form-inline">
                        &nbsp;<input type="text" class="form-control" size="2" id="j_page_jump_val" value="4"/>
                        <button class="btn btn_info1" id="j_page_jump">跳转</button>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</div>
<jsp:include page="../common/footer.jsp"></jsp:include>
</body>
</html>
