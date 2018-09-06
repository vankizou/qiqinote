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
<html>
<head>
    <meta name="keywords" content="${noteVO.note.keyword}">
    <%--<base href="<%=basePath%>">--%>
    <title>${noteVO.note.title}-奇奇笔记</title>
    <jsp:include page="../common/common.jsp"></jsp:include>
    <jsp:include page="../common/markdown-preview.jsp"></jsp:include>

    <script type="text/javascript" src="/statics/js/note/view.js"></script>
    <script type="text/javascript" src="/statics/js/note/view_comment.js"></script>
    <link rel="stylesheet" href="/statics/css/note/view.css">

    <%-- 悬浮 --%>
    <script type="text/javascript" src="/statics/third/float/dist/lib/modernizr.touch.js"></script>
    <script type="text/javascript" src="/statics/third/float/dist/mfb.js"></script>
    <script type="text/javascript" src="/statics/js/note/view_menu.js"></script>
    <link rel="stylesheet" href="/statics/third/float/dist/mfb.css">
    <link rel="stylesheet" href="/statics/css/note/view_menu.css">
    <%--<link rel="stylesheet" href="http://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">--%>
    <%--<link rel="stylesheet" href="/statics/third/float/css/index.css">--%>

    <script type="text/javascript">
        var c_noteUserId = '${noteVO.note.userId}';
        var c_noteId = '${noteVO.note.id}';
        var c_noteIdLink = '${noteVO.note.idLink}';
        var c_isNeedPwd = '${noteVO.needPwd}';
        var c_pwd = '${noteVO.note.password}';

        $(function () {
            changeFooterMarginTop(10);
        });
    </script>
</head>

<body>
<jsp:include page="../common/top.jsp"></jsp:include>

<div class="container row c_all" style="margin: 10px auto; width: 94%; padding: 10px;">
    <div id="j_empty_content" class="text-center"
         style="display: none; margin: 50px auto; font-size:18px; font-weight:bold;">
        笔记内容为空!&nbsp; <a href="/${noteVO.user.name == null ? noteVO.note.userId : noteVO.user.name}" target="_blank"
                         class="btn_info1"> ${isMe ? "我" : "TA"}的笔记树&nbsp;>></a>
    </div>

    <div id="j_note_info" <c:if test="${empty noteVO.noteDetailList}">style="display: none;"</c:if>>
        <div class="col-xs-9">
            <div id="layout">
                <h1 class="text-center c_title j_note_info">${noteVO.note.title}&nbsp;&nbsp;
                    <small id="j_note_info_parent_title">${noteVO.parentNote.title}</small>
                </h1>

                <div id="vanki-editormd-view-note">
                    <textarea id="j_content" style="display: none;"><c:if
                            test="${!empty noteVO.noteDetailList && !empty noteVO.noteDetailList[0].content}">${noteVO.noteDetailList[0].content}
                    </c:if></textarea>
                </div>
            </div>
            <div id="j_note_comment" style="margin: 50px 0px 0px 20px; width: 94%; padding: 10px;">
                <div class="note_comment">
                    <div id="j_note_comment_view">
                        <ul id="j_comment_view_ul" class="comment_view_ul">
                            <li id="j_comment_view_ul_li_default" style="display: none;">
                                <a class="comment_view_user_info" title="访问主页" target="_blank">
                                    <img src="" onerror="this.src='/statics/images/common/avatar/default.jpg'"
                                         class="img-circle center-block" width="50" height="50"
                                         style="border: 1px solid lightgrey"/>
                                </a>
                                <div>
                                    <div class="comment_view_title">
                                        <span class="comment_view_user_alias" style="margin-right: 0px;"></span>
                                        <span class="badge"
                                              style="margin-right: 0px;background-color: lightsalmon;display: none">作者</span>
                                        <span class="comment_view_time" style="margin-left: 8px"></span>
                                        <code class="comment_view_reply">
                                            <span class="comment_view_replyNum"></span>回复
                                        </code>
                                    </div>
                                    <div class="comment_view_content">
                                        <div class="comment_view_content_data"></div>
                                        <div>
                                            <code class="comment_view_content_reply">回复</code>
                                            <code class="comment_view_content_delete">删除</code>
                                        </div>
                                        <div class="comment_view_content_sub">
                                            <ul class="comment_view_ul_sub">
                                                <li id="j_comment_view_ul_sub_li_default">
                                                    <a class="comment_view_user_info_sub" title="访问主页"
                                                       target="_blank">
                                                        <img src=""
                                                             onerror="this.src='/statics/images/common/avatar/default.jpg'"
                                                             class="img-circle center-block" width="45" height="45"
                                                             style="border: 1px solid lightgrey"/>
                                                    </a>
                                                    <div>
                                                        <div class="comment_view_title_sub">
                                                            <span class="comment_view_user_alias_sub"
                                                                  style="margin-right: 0px;"></span>
                                                            <span class="badge"
                                                                  style="margin-right: 0px;background-color: lightsalmon;display: none">作者</span>
                                                            <span class="comment_view_time_sub"
                                                                  style="margin-left: 8px"></span>
                                                        </div>
                                                        <div class="comment_view_content_sub">
                                                            <div class="comment_view_content_data_sub"></div>
                                                            <div>
                                                                <code class="comment_view_content_reply_sub">回复</code>
                                                                <code class="comment_view_content_delete_sub">删除</code>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </li>
                                            </ul>
                                            <div class="comment_view_load_more">
                                                <code>加载更多(<span class="comment_view_load_more_left"></span>)</code>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </ul>
                        <div id="comment_view_load_more_root" class="comment_view_load_more_root">
                            <code>加载更多(<span class="comment_view_load_more_root_left"></span>)</code>
                        </div>
                    </div>
                    <div style="margin-top: 30px;">
                        <textarea id="j_note_comment_content" class="form-control" rows="5"
                                  placeholder="说点什么..."></textarea>
                        <div class="text-right" style="margin-top: 10px">
                            <code id="j_note_comment_retry_cancel">取消回复</code>
                            <button id="j_note_comment_create_btn" type="button" class="btn btn-success"
                                    style="width: 160px">发表评论
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xs-3 c_body_right j_note_info">
            <div>
                <div style="margin-bottom: 10px;">
                    <div id="j_user_info" class="text-center" style="margin-bottom: 10px; cursor: pointer;"
                         title="作者主页"
                         onclick="window.open('/${noteVO.user.name == null ? noteVO.note.userId : noteVO.user.name}');">
                        <div>
                            <img id="j_user_info_avatar"
                                 src="${noteVO.user.avatar != null ? noteVO.user.avatar.path : "/statics/images/common/avatar/default.jpg"}"
                                 onerror="this.src='/statics/images/common/avatar/default.jpg'"
                                 class="img-responsive img-circle center-block" width="150" height="150"
                                 style="border: 1px solid lightgrey"/>
                        </div>
                        <div style="margin: 8px 0; font-size: 18px;">
                            <span id="j_user_info_alias">${noteVO.user.alias}</span>
                        </div>
                    </div>
                    <pre class="text-center"
                         id="j_user_info_motto"
                         style="padding: 15px 12px;">${noteVO.user.motto == null || "".equals(noteVO.user.motto) ? "空的！" : noteVO.user.motto}</pre>
                </div>
                <table class="table" style="border: 0px solid transparent !important; margin-top: 20px;">
                    <tbody>
                    <tr>
                        <td class="text-left">
                            <span class="btn_info1">浏览：</span>
                            <span id="j_note_info_view_num"
                                  class="label_info1">${noteVO.note.viewNum}</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-left">
                            <span class="btn_info1">创建：</span>
                            <span id="j_note_info_create_datetime"
                                  class="label_info1">${noteVO.createDatetimeStr}</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-left">
                            <span class="btn_info1">更新：</span>
                            <span id="j_note_info_update_datetime"
                                  class="label_info1">${noteVO.updateDatetimeStr}</span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <%--<div>
                <a id="j_note_info_user_url" href="/${noteVO.user.name == null ? noteVO.note.userId : noteVO.user.name}"
                   target="_blank" class="btn btn_info1"
                   style="margin-left: 8px;">${isMe ? "我" : "TA"}的笔记树&nbsp;>></a>
            </div>--%>

            <div>
                <section class="widget paddingall">
                    <h5 class="widget-title">${isMe ? "我" : "TA"}的最新笔记</h5>
                    <ul class="widget-list list-unstyled">
                        <c:if test="${newest != null}">
                            <c:forEach var="note" items="${newest}">
                                <li>
                                    <a href="/note/${note.secret==3 ? note.idLink : "".concat(note.id).concat(".html")}"><i
                                            class="fa fa-book">&nbsp; </i>${note.title}</a></li>
                            </c:forEach>
                        </c:if>
                    </ul>
                </section>
            </div>
            <div>
                <section class="widget paddingall">
                    <h5 class="widget-title">${isMe ? "我" : "TA"}的最热笔记</h5>
                    <ul class="widget-list list-unstyled">
                        <c:if test="${hottest != null}">
                            <c:forEach var="note" items="${hottest}">
                                <li>
                                    <a href="/note/${note.secret==3 ? note.idLink : "".concat(note.id).concat(".html")}"><i
                                            class="fa fa-book">&nbsp; </i>${note.title}</a></li>
                            </c:forEach>
                        </c:if>
                    </ul>
                </section>
            </div>
        </div>

        <ul id="menu" class="mfb-component--bl mfb-zoomin" data-mfb-toggle="click" style="display: none;">
            <li class="mfb-component__wrap">
                <a class="mfb-component__button--main" href="#">
                    <i class="fa fa-arrow-up"></i>
                </a>
                <%--<ul class="mfb-component__list">
                    <li>
                        <a href="#" data-mfb-label="View on Github" class="mfb-component__button--child">
                            <i class="mfb-component__child-icon ion-social-github"></i>
                        </a>
                    </li>
                    <li>
                        <a href="#" data-mfb-label="Follow me on Github" class="mfb-component__button--child">
                            <i class="mfb-component__child-icon ion-social-octocat"></i>
                        </a>
                    </li>

                    <li>
                        <a href="#" data-mfb-label="Share on Twitter" class="mfb-component__button--child">
                            <i class="mfb-component__child-icon ion-social-twitter"></i>
                        </a>
                    </li>
                </ul>--%>
            </li>
            <li class="mfb-component__wrap">
                <a class="mfb-component__button--main" title="评论">
                    <i class="fa fa-commenting"></i>
                </a>
            </li>
            <li class="mfb-component__wrap">
                <a class="mfb-component__button--main" title="下载">
                    <i class="fa fa-cloud-download"></i>
                </a>
            </li>

            <c:if test="${isMe}">
                <li class="mfb-component__wrap">
                    <a class="mfb-component__button--main" title="修改" href="${searchLink}" target="_blank">
                            <%--<i class="fa fa-font"></i>--%>
                        <i class="fa fa-pencil"></i>
                    </a>
                </li>
            </c:if>
        </ul>
    </div>
</div>

<jsp:include page="../common/footer.jsp"></jsp:include>

</body>
</html>
