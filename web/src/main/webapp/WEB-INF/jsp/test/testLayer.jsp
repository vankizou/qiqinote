<%--
  Created by IntelliJ IDEA.
  User: vanki
  Date: 2017/6/21
  Time: 16:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <jsp:include page="../common/common.jsp"></jsp:include>
    <script>
        $(function () {
            layer.open({
                type: 1,
                title: false,
                closeBtn: 1,
                shade: false,
                shadeClose: true,
//                skin: 'yourclass',
                content: $("#j_pop")
            });
        });
    </script>
</head>
<body>

    <div id="j_pop" style="display:none;">
        <input value="sdsdf"/>
    </div>

</body>
</html>
