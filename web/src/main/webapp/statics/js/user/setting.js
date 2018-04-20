$(function () {
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        // 获取已激活的标签页的名称
        var activeTab = $(e.target).text();

        if (activeTab == '基本信息') {
            buildUserInfo();
        } else if (activeTab == '密保问题') {
            buildQuestions();
        } else if (activeTab == '修改密码') {
            buildQuestionsOfUpdatePwd();
        }
    });

    // 选中第一个选项卡
    $('#j_settingTab a:first').tab('show');


    /**
     * 基本信息
     */
    $('#j_user_submit').click(function () {
        updateUserInfo();
    });


    /**
     * 密保问题
     */
    $('.j_question_random').click(function () {
        buildRandomQuestion($(this).attr("v"))
    });

    $('#j_question_view_answer').click(function () {
        buildQuestions(true);
    });

    $('#j_question_submit').click(function () {
        updateQuestions();
    });


    /**
     * 修改密码
     */
    $('#j_update_pwd_submit').click(function () {
        updatePwdByOldPwd();
    });

    $('#j_update_pwd_question_submit').click(function () {
        updatePwdByQuestions();
    });

    $('#j_update_pwd_account').blur(function () {
        buildQuestionsOfUpdatePwd($('#j_update_pwd_account').val());
    });
});

/************************** 修改密码 **************************/
function updatePwdByQuestions() {
    var account = $('#j_update_pwd_account').val();

    var answer1 = $('#j_update_pwd_question_answer_1').val();
    var answerId1 = $('#j_update_pwd_question_1').attr('db_id');

    var answer2 = $('#j_update_pwd_question_answer_2').val();
    var answerId2 = $('#j_update_pwd_question_2').attr('db_id');

    var answer3 = $('#j_update_pwd_question_answer_3').val();
    var answerId3 = $('#j_update_pwd_question_3').attr('db_id');

    var newPwd = $('#j_update_pwd_question_new_pwd').val();
    var newPwd2 = $('#j_update_pwd_question_new_pwd2').val();

    var answerCount = 0;
    if (answer1 && answerId1) answerCount++;
    if (answer2 && answerId2) answerCount++;
    if (answer3 && answerId3) answerCount++;
    if (answerCount < 2) {
        vankiLayerMsgFailTou("密保回答数量不够哦")
        return
    }
    if (!newPwd || !newPwd2) {
        vankiLayerMsgFailTou("新密码和确认密码不能为空")
        return
    }
    if (newPwd != newPwd2) {
        vankiLayerMsgFailTou("两次新密码输入不一致")
        return
    }

    var params = {
        "account": account,
        "password": newPwd,
        "questions[0].id": answerId1,
        "questions[0].answer": answer1,
        "questions[1].id": answerId2,
        "questions[1].answer": answer2,
        "questions[2].id": answerId3,
        "questions[2].answer": answer3
    };
    
    var fnSucc = function () {
        vankiLayerMsgSuccGou("密码修改成功")
        window.location = "/login.html";
    };
    vankiAjax(ConstAjaxUrl.User.updatePwdByQuestions, params, fnSucc)
}

function buildQuestionsOfUpdatePwd(account) {
    $('#j_update_pwd_question').hide();

    if (!account) account = a_loginUserId
    if (!account) return

    var fnSucc = function (data) {
        if (data.length != 3) {
            return;
        }

        for (var i in data) {
            var seq = Number(i) + 1;

            $('#j_update_pwd_question_' + seq).html(data[i]['question']);
            $('#j_update_pwd_question_' + seq).attr("db_id", data[i]['id']);
        }
        $('#j_update_pwd_question').show();
    };

    vankiAjax(ConstAjaxUrl.User.listOfPwdQuestion, {account: account}, fnSucc)
}

function updatePwdByOldPwd() {
    var account = $('#j_update_pwd_account').val();
    var oldPwd = $('#j_update_pwd_old_pwd').val();
    var newPwd = $('#j_update_pwd_new_pwd').val();
    var newPwd2 = $('#j_update_pwd_new_pwd2').val();

    if (!oldPwd || !newPwd || !newPwd2) {
        vankiLayerMsgFailCha("所有数据都为必填");
        return
    }
    if (newPwd != newPwd2) {
        vankiLayerMsgFailCha("两次新密码输入不一致");
        return
    }

    var params = {
        account: account,
        oldPassword: oldPwd,
        newPassword: newPwd
    };

    var fnSucc = function () {
        vankiLayerMsgSuccGou("密码修改成功")
        window.location = "/login.html";
    };

    vankiAjax(ConstAjaxUrl.User.updatePwdByOldPwd, params, fnSucc)
}


/************************** 密保问题 **************************/

function updateQuestions() {
    var q1 = $('#j_question_1').val();
    var q1Id = $('#j_question_1').attr("db_id");
    var a1 = $('#j_answer_1').val();

    var q2 = $('#j_question_2').val();
    var q2Id = $('#j_question_2').attr("db_id");
    var a2 = $('#j_answer_2').val();

    var q3 = $('#j_question_3').val();
    var q3Id = $('#j_question_3').attr("db_id");
    var a3 = $('#j_answer_3').val();

    if (!q1 || !a1 || !q2 || !a2 || !q3 || !a3) {
        vankiLayerMsgFailCha("所有数据都为必填");
        return
    }

    if (q1 == q2 || q1 == q3 || q2 == q3) {
        vankiLayerMsgFailTou("密保问题相同会给以后带来麻烦哦");
        return
    }

    var params = {
        "password": $('#j_question_login_password').val(),
        "questions[0].id": q1Id,
        "questions[0].question": q1,
        "questions[0].answer": a1,
        "questions[1].id": q2Id,
        "questions[1].question": q2,
        "questions[1].answer": a2,
        "questions[2].id": q3Id,
        "questions[2].question": q3,
        "questions[2].answer": a3
    };

    var fnSucc = function () {
        vankiLayerMsgSuccGou("密保问题修改成功")
    };

    vankiAjax(ConstAjaxUrl.User.updateSecurityQuestions, params, fnSucc)
}

var curr_question_temp_index = 0,
    a_question_temp = ["我父亲的名字？", "我母亲的名字？", "我配偶的名字？", "我小学的名称？", "我中学的名称？", "我大学的名称？", "我的职业？", "我有过几任女友？", "我初恋的名字？"];

function buildRandomQuestion(questionSeq) {
    if (curr_question_temp_index >= a_question_temp.length) curr_question_temp_index = 0;
    var randomQuestion = a_question_temp[curr_question_temp_index++];
    if (randomQuestion == $('#j_question_' + questionSeq).val()) {
        buildRandomQuestion(questionSeq)
    } else {
        $('#j_question_' + questionSeq).val(randomQuestion)
    }
}

function buildQuestions(isPopPwdError) {
    var params = {
        password: $('#j_question_login_password').val()
    };
    var fnSucc = function (data) {
        var seq = 1;
        var answerIsNull = false;
        for (var i in data) {
            $('#j_question_' + seq).val(data[i]['question']);
            $('#j_question_' + seq).attr('db_id', data[i]['id']);
            $('#j_answer_' + seq).val(data[i]['answer']);

            if (isPopPwdError && !answerIsNull && !data[i]['answer']) answerIsNull = true;
            seq++;
        }
        if (answerIsNull) {
            vankiLayerMsgFailTou("密码错误")
        }
    };
    vankiAjax(ConstAjaxUrl.User.listOfPwdQuestion, params, fnSucc)
}


/************************** 基本信息 **************************/

function updateUserInfo() {
    var alias = $('#j_user_alias').val();
    var gender = $('input[name="j_user_gender"]:checked').val();
    var phone = $('#j_user_phone').val();
    var email = $('#j_user_email').val();
    var qq = $('#j_user_qq').val();
    var weixin = $('#j_user_weixin').val();
    var weibo = $('#j_user_weibo').val();
    var motto = $('#j_user_motto').val();

    if (motto && motto.length > 120) {
        vankiLayerMsgFailTou("话在于精，不在于多，格言再精简精简")
        return
    }

    var param = {
        alias: alias,
        gender: gender,
        phone: phone,
        email: email,
        qq: qq,
        weixin: weixin,
        weibo: weibo,
        motto: motto
    };

    var fnSucc = function () {
        vankiLayerMsgSuccGou("修改成功");
        buildUserInfo()
    };
    vankiAjax(ConstAjaxUrl.User.updateInfo, param, fnSucc)
}

function buildUserInfo() {
    var fnSucc = function (data) {
        var alias = data['alias'];
        var gender = data['gender'];
        var phone = data['phone'];
        var email = data['email'];
        var qq = data['qq'];
        var weixin = data['weixin'];
        var weibo = data['weibo'];
        var motto = data['motto'];

        if (alias) {
            $('#j_user_alias').val(alias)
        }
        if (gender) {
            $('input[name="j_user_gender"]').attr('disabled', true);
            $('input[name="j_user_gender"][value="' + gender + '"]').attr('checked', true);
            $('#j_user_gender_alt').hide()
        } else {
            $('#j_user_gender_alt').show()
        }
        if (phone) {
            $('#j_user_phone').val(phone)
        }
        if (email) {
            $('#j_user_email').val(email)
        }
        if (qq) {
            $('#j_user_qq').val(qq)
        }
        if (weixin) {
            $('#j_user_weixin').val(weixin)
        }
        if (weibo) {
            $('#j_user_weibo').val(weibo)
        }
        if (motto) {
            $('#j_user_motto').val(motto)
        }
    };
    vankiAjax(ConstAjaxUrl.User.info, {}, fnSucc)
}