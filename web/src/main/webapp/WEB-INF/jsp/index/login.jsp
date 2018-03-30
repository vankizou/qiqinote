<%--
  Created by IntelliJ IDEA.
  User: vanki
  Date: 2017/4/23
  Time: 03:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>登录注册-奇奇笔记</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <link href="/statics/css/index/login.css" rel="stylesheet" type="text/css" media="all"/>
    <jsp:include page="../common/common.jsp"></jsp:include>
    <script type="text/javascript" src="/statics/third/popwindow/jquery.blockUI.js"></script>
    <script type="text/javascript" src="/statics/js/common/top/loginRegister.js"></script>
</head>
<body>
<div id="j_loginRegister" class="login">
    <h2>欢&nbsp;&nbsp;迎</h2>
    <div id="j_login">
        <div class="login-top">
            <h1>登录</h1>
            <form>
                <input id="j_login_id" type="text" placeholder="ID/登录名"/>
                <input id="j_login_pwd" type="password" placeholder="密码"/>
            </form>
            <div class="forgot">
                <label class="btn btn-default" style="background: #E1E1E1; color: #818181; border: none;">
                    <input id="j_login_is_remember" type="checkbox" checked="checked"/> 自动登录
                </label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <a href="#">忘记密码？</a>
                <input id="j_login_submit" type="submit" value="登录"/>
            </div>
        </div>
        <div class="login-bottom">
            <h3>新朋友 &nbsp;<a class="j_change" href="javascript:;">注册</a></h3>
        </div>
    </div>

    <div id="j_register" style="display: none;">
        <div class="login-top">
            <h1>注册</h1>
            <form>
                <input id="j_reg_username" type="text" placeholder="唯一用户名" title="登录用户名"/>
                &nbsp;<i style="color:black;" class="fa fa-question-circle" title="登录用户名"></i>
                <input id="j_reg_pwd" type="password" placeholder="登录密码" title="登录密码"/>
                &nbsp;<i style="color:black;" class="fa fa-question-circle" title="登录密码"></i>
                <input id="j_reg_confirm_pwd" type="password" placeholder="确认密码" title="确认密码"/>
                &nbsp;<i style="color:black;" class="fa fa-question-circle" title="确认密码"></i>
                <div>
                    <input class="c_imagecode" id="j_reg_imagecode" placeholder="验证码计算值"/>
                    <img class="c_imagecode_img" id="j_reg_imagecode_img" src=""/>
                </div>
            </form>

            <div class="forgot">
                <input id="j_register_submit" type="submit" value="注册"/>
            </div>
        </div>
        <div class="login-bottom">
            <h3>老朋友 &nbsp;<a class="j_change" href="javascript:;">登录</a></h3>
        </div>
    </div>
</div>
<div class="copyright">
    <%--<p>Copyright &copy; 2015.Company name All rights reserved.<a target="_blank" href="http://www.cssmoban.com/">&#x7F51;&#x9875;&#x6A21;&#x677F;</a> - More Templates <a href="http://www.cssmoban.com/" target="_blank" title="模板之家">模板之家</a></p>--%>
</div>
</body>
</html>