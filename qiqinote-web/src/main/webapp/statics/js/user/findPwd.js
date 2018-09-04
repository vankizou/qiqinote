$(function () {

    /*$('#j_find_pwd_account').blur(function () {
        buildQuestionsOfFindPwd();
    });*/

    $('#j_find_pwd_account').keyup(function (event) {
        if (event.keyCode != 13) return;
        buildQuestionsOfFindPwd();
    });

    $('#j_find_pwd_question_submit').click(function () {
        updatePwdByQuestions()
    });
});

function updatePwdByQuestions() {
    var account = $('#j_find_pwd_account').val();

    var answer1 = $('#j_find_pwd_question_answer_1').val();
    var answerId1 = $('#j_find_pwd_question_1').attr('db_id');

    var answer2 = $('#j_find_pwd_question_answer_2').val();
    var answerId2 = $('#j_find_pwd_question_2').attr('db_id');

    var answer3 = $('#j_find_pwd_question_answer_3').val();
    var answerId3 = $('#j_find_pwd_question_3').attr('db_id');

    var newPwd = $('#j_find_pwd_question_new_pwd').val();
    var newPwd2 = $('#j_find_pwd_question_new_pwd2').val();

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
        vankiLayerMsgSuccGou("密码找回成功");
    };
    vankiAjax(ConstAjaxUrl.User.updatePwdByQuestions, params, fnSucc)
}

function buildQuestionsOfFindPwd() {
    $('.find_pwd_answer').hide();
    $('.find_pwd_no_question_alt').hide();

    var account = $('#j_find_pwd_account').val()
    if (!account) {
        vankiLayerMsgFailTou("请输入需要找回密码的帐号")
        return;
    }

    var fnSucc = function (data) {
        if (data.length != 3) {
            $('.find_pwd_no_question_alt').show();
            return;
        }

        for (var i in data) {
            var seq = Number(i) + 1;

            $('#j_find_pwd_question_' + seq).html(data[i]['question']);
            $('#j_find_pwd_question_' + seq).attr("db_id", data[i]['id']);
        }
        $('.find_pwd_answer').show();
    };

    vankiAjax(ConstAjaxUrl.User.listOfPwdQuestion, {account: account}, fnSucc)
}