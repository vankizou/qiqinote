<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: vanki
  Date: 2018/5/25
  Time: 17:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <meta name="keywords" content="${noteVO.note.keyword}">
    <%--<base href="<%=basePath%>">--%>
    <title>未读评论-奇奇笔记</title>
    <jsp:include page="../common/common.jsp"></jsp:include>

    <link rel="stylesheet" href="/statics/css/comment/unread.css">

    <script type="text/javascript">
        $(function () {
            changeFooterMarginTop(10);
        });
    </script>
</head>

<body>
<jsp:include page="../common/top.jsp"></jsp:include>
<div class="container row c_all" style="margin: 10px auto; width: 88%; padding: 10px;">
    <ul id="j_comment_view_ul" class="comment_view_ul">
        <li id="j_comment_view_ul_li_default" style="display: none;">
            <a class="comment_view_user_info" title="访问主页" target="_blank">
                <img src="" onerror="this.src='/statics/images/common/avatar/default.jpg'"
                     class="img-circle center-block" width="50" height="50"
                     style="border: 1px solid lightgrey"/>
            </a>
            <div>
                <div class="comment_view_title">
                    <span class="comment_view_user_alias"></span>
                    <span class="comment_view_time"></span>
                    <span><a class="comment_view_title"></a></span>
                    <span class="comment_view_type_str"></span>
                </div>
                <div class="comment_view_content">
                    <div class="comment_view_content_data"></div>
                </div>
            </div>
        </li>
    </ul>
    <div id="comment_view_load_more">
        <code>继续加载</code>
    </div>
</div>

<jsp:include page="../common/footer.jsp"></jsp:include>
<script type="text/javascript" src="/statics/js/comment/unread.js"></script>

</body>
</html>
