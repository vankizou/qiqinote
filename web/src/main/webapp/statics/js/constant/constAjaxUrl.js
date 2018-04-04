/**
 * Created by vanki on 16/10/28.
 */

var ConstAjaxUrl = {

    Exception: {
        error404_html: ["/404.html"],
        error500_html: ["/500.html"]
    },

    Image: {
        uploadMulti: ["/image/uploadMulti.sjson"],
        page: ["/image/page.sjson"]
    },

    ImageCode: {
        getImageCode: ["/imagecode/getImageCode.json"],
    },

    Index: {
        root_html: ["/"],
        userHome_html: ["/{idOrName}", "{idOrName}"],
        signUp: ["/signUp.json", "POST", "JSON"],
        signIn: ["/signIn.json", "POST", "JSON"],
        signOut: ["/signOut.json", "POST", "JSON"],
        signOut_html: ["/signOut.html"],
    },

    Note: {
        view_html: ['/note/${id}', '${id}'],
        download: ['/note/download.json'],
        doDownload: ['/note/doDownload.json'],

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
}