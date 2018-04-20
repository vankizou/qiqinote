<%--
  Created by IntelliJ IDEA.
  User: vanki
  Date: 2017/5/25
  Time: 14:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>设置-奇奇笔记</title>
    <jsp:include page="../common/common.jsp"></jsp:include>

    <link rel="stylesheet" href="/statics/css/user/setting.css">
    <script type="text/javascript" src="/statics/js/user/setting.js"></script>
</head>
<body>
<jsp:include page="../common/top.jsp"></jsp:include>

<div style="padding: 20px 10% 0px;">

    <!-- 选项卡菜单-->
    <ul id="j_settingTab" class="nav nav-tabs nav-justified" role="tablist" style="width: 100%">
        <c:if test="${suc != null}">
            <li><a href="#userInfo" role="tab" data-toggle="tab">基本信息</a></li>
            <li><a href="#questions" role="tab" data-toggle="tab">密保问题</a></li>
        </c:if>
        <li><a href="#updatePwd" role="tab" data-toggle="tab">修改密码</a></li>
    </ul>
    <!-- 选项卡面板 -->
    <div id="j_settingTabContent" class="tab-content">
        <div class="tab-pane fade" id="userInfo">
            <table class="table">
                <tbody>
                <tr>
                    <td class="textRightAndVMiddle">昵称：</td>
                    <td><input id="j_user_alias" type="text" class="form-control" placeholder="昵称"></td>
                </tr>
                <tr>
                    <td class="textRightAndVMiddle">性别：</td>
                    <td>
                        &nbsp;&nbsp;
                        <input type="radio" name="j_user_gender" value="1"> 男
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="radio" name="j_user_gender" value="2"> 女

                        <span style="color: red; font-size: 10px; display: none;" id="j_user_gender_alt">&nbsp;&nbsp;* 只能更改一次</span>
                    </td>
                </tr>
                <tr>
                    <td class="textRightAndVMiddle">手机：</td>
                    <td><input id="j_user_phone" type="number" class="form-control" placeholder="手机号"></td>
                </tr>
                <tr>
                    <td class="textRightAndVMiddle">邮箱：</td>
                    <td><input id="j_user_email" type="email" class="form-control" placeholder="邮箱"></td>
                </tr>
                <tr>
                    <td class="textRightAndVMiddle">Q Q：</td>
                    <td><input id="j_user_qq" type="number" class="form-control" placeholder="QQ号"></td>
                </tr>
                <tr>
                    <td class="textRightAndVMiddle">微信：</td>
                    <td><input id="j_user_weixin" type="text" class="form-control" placeholder="微信号"></td>
                </tr>
                <tr>
                    <td class="textRightAndVMiddle">微博：</td>
                    <td><input id="j_user_weibo" type="text" class="form-control" placeholder="微博号"></td>
                </tr>
                <tr>
                    <td class="textRightAndVMiddle">格言：</td>
                    <td>
                        <textarea id="j_user_motto" class="form-control" placeholder="人生格言"></textarea>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td class="text-right">
                        <button class="btn btn-info" id="j_user_submit">提交</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="tab-pane fade" id="questions">
            <table class="table">
                <tbody>
                <tr>
                    <td class="col-xs-7 form-inline">
                        <input id="j_question_1" type="text" class="form-control" placeholder="问题一" size="30" db_id="">
                        <button class="btn btn-info j_question_random" v="1">随机</button>
                    </td>
                    <td class="col-xs-5">
                        <input id="j_answer_1" type="text" class="form-control" placeholder="答案一">
                    </td>
                </tr>
                <tr>
                    <td class="col-xs-7 form-inline">
                        <input id="j_question_2" type="text" class="form-control" placeholder="问题二" size="30" db_id="">
                        <button class="btn btn-info j_question_random" v="2">随机</button>
                    </td>
                    <td class="col-xs-5">
                        <input id="j_answer_2" type="text" class="form-control" placeholder="答案二">
                    </td>
                </tr>
                <tr>
                    <td class="col-xs-7 form-inline">
                        <input id="j_question_3" type="text" class="form-control" placeholder="问题三" size="30" db_id="">
                        <button class="btn btn-info j_question_random" v="3">随机</button>
                    </td>
                    <td class="col-xs-5">
                        <input id="j_answer_3" type="text" class="form-control" placeholder="答案三">
                    </td>
                </tr>
                <tr>
                    <td>
                        <input id="j_question_login_password" type="password" class="form-control"
                               placeholder="操作请输入登录密码" size="30">
                    </td>
                    <td class="text-right">
                        <button class="btn btn-info" id="j_question_view_answer">查看答案</button>
                        <button class="btn btn-info" id="j_question_submit">提交新答案</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="tab-pane fade" id="updatePwd">
            <div>
                <table class="table">
                    <tbody>
                    <c:if test="${suc == null}">
                        <tr>
                            <td class="textRightAndVMiddle">帐号：</td>
                            <td>
                                <input id="j_update_pwd_account" type="text" class="form-control"
                                       placeholder="ID 或 用户名">
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td class="textRightAndVMiddle">原密码：</td>
                        <td>
                            <input id="j_update_pwd_old_pwd" type="password" class="form-control" placeholder="原密码">
                        </td>
                    </tr>
                    <tr>
                        <td class="textRightAndVMiddle">新密码：</td>
                        <td>
                            <input id="j_update_pwd_new_pwd" type="password" class="form-control" placeholder="新密码">
                        </td>
                    </tr>
                    <tr>
                        <td class="textRightAndVMiddle">确认新密码：</td>
                        <td>
                            <input id="j_update_pwd_new_pwd2" type="password" class="form-control"
                                   placeholder="再次输入新密码">
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td class="text-right">
                            <button class="btn btn-info" id="j_update_pwd_submit">提交</button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="j_update_pwd_question" style="display: none;">
                <div class="text-with-hr">
                    <span>或 回答密保问题修改（至少答对两题）</span>
                </div>
                <div>
                    <table class="table">
                        <tbody>
                        <tr>
                            <td class="textRightAndVMiddle" id="j_update_pwd_question_1" db_id=""></td>
                            <td>
                                <input id="j_update_pwd_question_answer_1" type="text" class="form-control"
                                       placeholder="请输入答案（至少回答2个）">
                            </td>
                        </tr>
                        <tr>
                            <td class="textRightAndVMiddle" id="j_update_pwd_question_2" db_id=""></td>
                            <td>
                                <input id="j_update_pwd_question_answer_2" type="text" class="form-control"
                                       placeholder="请输入答案（至少回答2个）">
                            </td>
                        </tr>
                        <tr>
                            <td class="textRightAndVMiddle" id="j_update_pwd_question_3" db_id=""></td>
                            <td>
                                <input id="j_update_pwd_question_answer_3" type="text" class="form-control"
                                       placeholder="请输入答案（至少回答2个）">
                            </td>
                        </tr>
                        <tr>
                            <td class="textRightAndVMiddle">新密码：</td>
                            <td>
                                <input id="j_update_pwd_question_new_pwd" type="password" class="form-control"
                                       placeholder="新密码">
                            </td>
                        </tr>
                        <tr>
                            <td class="textRightAndVMiddle">确认新密码：</td>
                            <td>
                                <input id="j_update_pwd_question_new_pwd2" type="password" class="form-control"
                                       placeholder="再次输入新密码">
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="text-right">
                                <button class="btn btn-info" id="j_update_pwd_question_submit">提交</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

</div>

<jsp:include page="../common/footer.jsp"></jsp:include>
</body>

</html>
