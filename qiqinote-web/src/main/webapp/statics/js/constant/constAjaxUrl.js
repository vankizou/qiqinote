/**
 * Created by vanki on 16/10/28.
 */

var ConstAjaxUrl = {

    Exception: {
        error404_html: ["/404.html"],
        error500_html: ["/500.html"]
    },

    Comment: {
        create: ["/comment/create.sjson", "POST", "JSON"],
        delete: ["/comment/delete.sjson", "POST", "JSON"],
        countRoot: ["/comment/countRoot.json", "GET", "JSON"],
        listOfTarget: ["/comment/listOfTarget.json", "GET", "JSON"],
        pageOfUnread: ["/comment/pageOfUnread.sjson", "GET", "JSON"],
        unreadNum: ["/comment/unreadNum.json", "GET", "JSON"]
    },

    Config: {
        upsertNoteTreeConfig: ["/config/upsertNoteTreeConfig.sjson", "POST", "JSON"]
    },

    Image: {
        uploadMulti: ["/image/uploadMulti.sjson"],
        page: ["/image/page.sjson"]
    },

    ImageCode: {
        getImageCode: ["/imagecode/getImageCode.json"]
    },

    Index: {
        root_html: ["/"],
        userHome_html: ["/{idOrName}", "{idOrName}"],
        signUp: ["/signUp.json", "POST", "JSON"],
        signIn: ["/signIn.json", "POST", "JSON"],
        signOut: ["/signOut.json", "POST", "JSON"],
        signOut_html: ["/signOut.html"]
    },

    Note: {
        view_html: ['/note/${id}', '${id}'],
        preDownload: ['/note/preDownload.json'],

        add: ['/note/add.sjson', 'POST', 'JSON'],
        updateById: ['/note/updateById.sjson', 'POST', 'JSON'],
        closeNote: ['/note/closeNote.json', 'GET', 'JSON'],
        openNote: ['/note/openNote.json', 'GET', 'JSON'],
        deleteById: ['/note/deleteById.sjson', 'POST', 'JSON'],
        getNoteVOById: ['/note/getNoteVOById.json', 'GET', 'JSON'],
        pageOfHome: ['/note/pageOfHome.json', 'GET', 'JSON'],
        listOfNoteTreeVO: ['/note/listOfNoteTreeVO.json', 'GET', 'JSON'],
        totalNote: ['/note/totalNote.json', 'GET', 'JSON']
    },

    User: {
        setting_html: ['/user/setting.shtml'],
        info: ['/user/info.sjson', 'GET', 'JSON'],
        updateInfo: ['/user/updateInfo.sjson', 'POST', 'JSON'],
        updateSecurityQuestions: ['/user/updateSecurityQuestions.sjson', 'POST', 'JSON'],
        updatePwdByOldPwd: ['/user/updatePwdByOldPwd.json', 'POST', 'JSON'],
        updatePwdByQuestions: ['/user/updatePwdByQuestions.json', 'POST', 'JSON'],
        listOfPwdQuestion: ['/user/listOfPwdQuestion.json', 'POST', 'JSON']
    }
};