<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html lang="zh-CN">
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>奇奇笔记-未找到资源</title>

    <jsp:include page="common.jsp"></jsp:include>

    <link href="/statics/css/common/404.css" rel="stylesheet"/>
    <script src="/statics/js/common/404/countUp.js"></script>
    <script src="/statics/js/common/404/404.js"></script>
</head>
<body>

<div class="container">
    <div class="row pad-top text-center">
        <div class="col-md-6 col-md-offset-3 text-center">
            <h1> 资源未找到 </h1>
            <span id="error-link"></span>
        </div>
    </div>

    <div class="row text-center">
        <div class="col-md-8 col-md-offset-2">
            <h3><i class="fa fa-frown-o fa-5x"></i></h3>
            <a href="/" class="btn btn-primary">去首页看看 >></a>
        </div>
    </div>

</div>
</body>
</html>
