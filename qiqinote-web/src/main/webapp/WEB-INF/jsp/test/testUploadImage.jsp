<%--
  Created by IntelliJ IDEA.
  User: vanki
  Date: 2017/6/21
  Time: 14:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <jsp:include page="../common/common.jsp"></jsp:include>

    <script type="text/javascript">
        $(function () {
            var options = {
                beforeSubmit: function () {return false;},
                url: "/image/uploadMulti.json",
                type: "post",
                dataType: "json",
                clearForm: true,
                resetForm: true,
                success: function () {alert("success")},
                error: function() {alert("error")}
            }
            $("#j_uploadForm").ajaxForm(options);//.submit(functi)


        });
    </script>
</head>
<body>

<form id="j_uploadForm" enctype="multipart/form-data">
    <input type="file" name="images" accept="image/png,image/jpg,image/jpeg,image/gif" multiple><br/>
    <input type="submit" value="upload">
</form>

</body>
</html>
