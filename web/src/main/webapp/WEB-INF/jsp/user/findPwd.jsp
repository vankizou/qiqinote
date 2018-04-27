<%--
  Created by IntelliJ IDEA.
  User: vanki
  Date: 2018/4/23
  Time: 14:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>找回密码-奇奇笔记</title>
    <jsp:include page="../common/common.jsp"></jsp:include>

    <link rel="stylesheet" href="/statics/css/user/findPwd.css">
    <script type="text/javascript" src="/statics/js/user/findPwd.js"></script>

</head>
<body class="homepage">
<jsp:include page="../common/top.jsp"></jsp:include>

<div style="margin: 50px auto; width: 100px; font-size: 24px; font-weight: bold;">找回密码</div>

<div class="findPwdContent">
    <table class="table">
        <tbody>
        <tr>
            <td class="textRightAndVMiddle">帐号：</td>
            <td>
                <input id="j_find_pwd_account" type="text" class="form-control"
                       placeholder="请输入 ID 或 用户名">
            </td>
        </tr>

        <tr class="find_pwd_no_question_alt">
            <td colspan="2"><span>未找到相关密保信息！</span></td>
        </tr>
        <tr class="find_pwd_no_question_alt">
            <td colspan="2"><span>请联系管理员：vankizou@163.com</span></td>
        </tr>

        <tr class="find_pwd_answer">
            <td class="textRightAndVMiddle" id="j_find_pwd_question_1" db_id=""></td>
            <td>
                <input id="j_find_pwd_question_answer_1" type="text" class="form-control"
                       placeholder="请输入答案（至少回答2个）">
            </td>
        </tr>
        <tr class="find_pwd_answer">
            <td class="textRightAndVMiddle" id="j_find_pwd_question_2" db_id=""></td>
            <td>
                <input id="j_find_pwd_question_answer_2" type="text" class="form-control"
                       placeholder="请输入答案（至少回答2个）">
            </td>
        </tr>
        <tr class="find_pwd_answer">
            <td class="textRightAndVMiddle" id="j_find_pwd_question_3" db_id=""></td>
            <td>
                <input id="j_find_pwd_question_answer_3" type="text" class="form-control"
                       placeholder="请输入答案（至少回答2个）">
            </td>
        </tr>
        <tr class="find_pwd_answer">
            <td class="textRightAndVMiddle">新密码：</td>
            <td>
                <input id="j_find_pwd_question_new_pwd" type="password" class="form-control"
                       placeholder="新密码">
            </td>
        </tr>
        <tr class="find_pwd_answer">
            <td class="textRightAndVMiddle">确认新密码：</td>
            <td>
                <input id="j_find_pwd_question_new_pwd2" type="password" class="form-control"
                       placeholder="再次输入新密码">
            </td>
        </tr>
        <tr class="find_pwd_answer">
            <td></td>
            <td class="text-right">
                <button class="btn btn-info" id="j_find_pwd_question_submit">提交</button>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<jsp:include page="../common/footer.jsp"></jsp:include>
</body>

</html>
