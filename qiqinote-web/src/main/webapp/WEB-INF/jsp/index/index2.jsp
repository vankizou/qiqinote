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
    <jsp:include page="../common/markdown-preview.jsp"></jsp:include>
    <link rel="stylesheet" href="/statics/css/index/index2.css">
</head>
<body>
<jsp:include page="../common/top.jsp"></jsp:include>
<script type="text/javascript" src="/statics/js/index/index2.js"></script>

<script type="text/javascript">
    $(function () {
        $("#copyright").show().css("margin-top", "80px");
    });
</script>

<div class="c_body">
    <div class="container" style="width: 90%;">

        <div class="col-xs-9">
            <div id="j_home_note1" class="panel panel-default home_note">
                <div class="panel-body">
                    <div id="j_home_note1_title" class="home_note_title" title="阅读全文"></div>
                    <div id="vanki-editormd-home-note1" class="home_note_editor"></div>
                </div>
                <div class="panel-footer home_note_footer">
                    <ul class="list-unstyled list-inline home_note_meta" style="height: 10px;">
                        <li id="j_home_note1_user" class="home_note_cursor"><i class="fa fa-user-circle-o"></i>&nbsp;<span></span></li>
                        <%--<li id="j_home_note1_user" class="home_note_cursor">
                            <img src="/statics/images/common/avatar/default.jpg"
                                 onerror="this.src='/statics/images/common/avatar/default.jpg'"
                                 class="img-responsive img-circle center-block" width="21" height="21"
                                 style="float: left;">
                            &nbsp;<span></span>
                        </li>--%>
                        <li><i class="fa fa-book"></i>&nbsp;<span id="j_home_note1_parent" target="_blank"></span></li>
                        <li><i class="fa fa-calendar"></i>&nbsp;<span id="j_home_note1_date"></span></li>
                        <li><i class="fa fa fa-eye"></i>&nbsp;<span id="j_home_note1_view_num"></span>次浏览</li>
                    </ul>
                </div>
            </div>

            <div id="j_home_note2" class="panel panel-default home_note">
                <div class="panel-body">
                    <div id="j_home_note2_title" class="home_note_title" title="阅读全文"></div>
                    <div id="vanki-editormd-home-note2" class="home_note_editor"></div>
                </div>
                <div class="panel-footer home_note_footer">
                    <ul class="list-unstyled list-inline home_note_meta" style="height: 10px;">
                        <li id="j_home_note2_user" class="home_note_cursor"><i class="fa fa-user-circle-o"></i>&nbsp;<span></span></li>
                        <%--<li id="j_home_note2_user" class="home_note_cursor">
                            <img src="/statics/images/common/avatar/default.jpg"
                                 onerror="this.src='/statics/images/common/avatar/default.jpg'"
                                 class="img-responsive img-circle center-block" width="21" height="21"
                                 style="float: left;">
                            &nbsp;<span></span>
                        </li>--%>
                        <li><i class="fa fa-book"></i>&nbsp;<span id="j_home_note2_parent" target="_blank"></span></li>
                        <li><i class="fa fa-calendar"></i>&nbsp;<span id="j_home_note2_date"></span></li>
                        <li><i class="fa fa fa-eye"></i>&nbsp;<span id="j_home_note2_view_num"></span>次浏览</li>
                    </ul>
                </div>
            </div>

            <div id="j_home_note3" class="panel panel-default home_note">
                <div class="panel-body">
                    <div id="j_home_note3_title" class="home_note_title" title="阅读全文"></div>
                    <div id="vanki-editormd-home-note3" class="home_note_editor"></div>
                </div>
                <div class="panel-footer home_note_footer">
                    <ul class="list-unstyled list-inline home_note_meta" style="height: 10px;">
                        <li id="j_home_note3_user" class="home_note_cursor"><i class="fa fa-user-circle-o"></i>&nbsp;<span></span></li>
                        <%--<li id="j_home_note3_user" class="home_note_cursor">
                            <img src="/statics/images/common/avatar/default.jpg"
                                 onerror="this.src='/statics/images/common/avatar/default.jpg'"
                                 class="img-responsive img-circle center-block" width="21" height="21"
                                 style="float: left;">
                            &nbsp;<span></span>
                        </li>--%>
                        <li><i class="fa fa-book"></i>&nbsp;<span id="j_home_note3_parent" target="_blank"></span></li>
                        <li><i class="fa fa-calendar"></i>&nbsp;<span id="j_home_note3_date"></span></li>
                        <li><i class="fa fa-eye"></i>&nbsp;<span id="j_home_note3_view_num"></span>次浏览</li>
                    </ul>
                </div>
            </div>

            <div id="j_home_note4" class="panel panel-default home_note">
                <div class="panel-body">
                    <div id="j_home_note4_title" class="home_note_title" title="阅读全文"></div>
                    <div id="vanki-editormd-home-note4" class="home_note_editor"></div>
                </div>
                <div class="panel-footer home_note_footer">
                    <ul class="list-unstyled list-inline home_note_meta" style="height: 10px;">
                        <li id="j_home_note4_user" class="home_note_cursor"><i class="fa fa-user-circle-o"></i>&nbsp;<span></span></li>
                        <%--<li id="j_home_note4_user" class="home_note_cursor">
                            <img src="/statics/images/common/avatar/default.jpg"
                                 onerror="this.src='/statics/images/common/avatar/default.jpg'"
                                 class="img-responsive img-circle center-block" width="21" height="21"
                                 style="float: left;">
                            &nbsp;<span></span>
                        </li>--%>
                        <li><i class="fa fa-book"></i>&nbsp;<span id="j_home_note4_parent" target="_blank"></span></li>
                        <li><i class="fa fa-calendar"></i>&nbsp;<span id="j_home_note4_date"></span></li>
                        <li><i class="fa fa fa-eye"></i>&nbsp;<span id="j_home_note4_view_num"></span>次浏览</li>
                    </ul>
                </div>
            </div>

            <div id="j_home_note5" class="panel panel-default home_note">
                <div class="panel-body">
                    <div id="j_home_note5_title" class="home_note_title" title="阅读全文"></div>
                    <div id="vanki-editormd-home-note5" class="home_note_editor"></div>
                </div>
                <div class="panel-footer home_note_footer">
                    <ul class="list-unstyled list-inline home_note_meta" style="height: 10px;">
                        <li id="j_home_note5_user" class="home_note_cursor"><i class="fa fa-user-circle-o"></i>&nbsp;<span></span></li>
                        <%--<li id="j_home_note5_user" class="home_note_cursor">
                            <img src="/statics/images/common/avatar/default.jpg"
                                 onerror="this.src='/statics/images/common/avatar/default.jpg'"
                                 class="img-responsive img-circle center-block" width="21" height="21"
                                 style="float: left;">
                            &nbsp;<span></span>
                        </li>--%>
                        <li><i class="fa fa-book"></i>&nbsp;<span id="j_home_note5_parent" target="_blank"></span></li>
                        <li><i class="fa fa-calendar"></i>&nbsp;<span id="j_home_note5_date"></span></li>
                        <li><i class="fa fa fa-eye"></i>&nbsp;<span id="j_home_note5_view_num"></span>次浏览</li>
                    </ul>
                </div>
            </div>

            <nav aria-label="Page navigation" class="text-center">
                <ul class="pagination">
                    <li><a href="javascript:void(0);" style="font-size: 14px;" id="j_page_info">0/0</a></li>
                    <li title="上一页">
                        <a href="javascript:void(0);" aria-label="Previous"
                           id="j_page_previous" class="j_page_prev_next" val="1">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <li title="下一页">
                        <a href="javascript:void(0);" aria-label="Next" id="j_page_next" class="j_page_prev_next"
                           val="2">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                    <li class="form-inline">
                        &nbsp;<input type="text" class="form-control" size="2" id="j_page_jump_val" value="4"/>
                        <button class="btn btn-info btn_info1" id="j_page_jump">跳转</button>
                    </li>
                </ul>
            </nav>
        </div>

        <div class="col-xs-3">
            <div>
                <input id="j_note_title_like" type="text" class="form-control" placeholder="搜索..."
                       style="width: 100%; min-width: 280px; margin-bottom: 10px;">
            </div>
            <div>
                <section class="widget paddingall">
                    <h5 class="widget-title">最热笔记</h5>
                    <ul class="widget-list list-unstyled">
                        <c:if test="${hottest != null}">
                            <c:forEach var="note" items="${hottest}">
                                <li>
                                    <a target="_blank"
                                       href="/note/${note.secret==3 ? note.idLink : "".concat(note.id).concat(".html")}"><i
                                            class="fa fa-book">&nbsp; </i>${note.title}</a></li>
                            </c:forEach>
                        </c:if>
                    </ul>
                </section>
            </div>
            <div>
                <section class="widget paddingall">
                    <h5 class="widget-title">最新笔记</h5>
                    <ul class="widget-list list-unstyled">
                        <c:if test="${newest != null}">
                            <c:forEach var="note" items="${newest}">
                                <li>
                                    <a target="_blank"
                                       href="/note/${note.secret==3 ? note.idLink : "".concat(note.id).concat(".html")}"><i
                                            class="fa fa-book">&nbsp; </i>${note.title}</a></li>
                            </c:forEach>
                        </c:if>
                    </ul>
                </section>
            </div>
        </div>

    </div>
</div>
<jsp:include page="../common/footer.jsp"></jsp:include>
</body>
</html>
