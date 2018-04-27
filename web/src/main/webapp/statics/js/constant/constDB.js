/**
 * Created by vanki on 16/10/28.
 */

var ConstDB = {
    defaultParentId: -1,

    Picture: {
        useTypeNote: 1,
        useTypeAvatar: 2,
    },

    Note: {
        typeNormal: 1,
        typeMarkdown: 2,

        secretOpen: 0,
        secretPwd: 1,
        secretClose: 2,
        secretLink: 3,

        isNeedPwdYes: 1,
        isNeedPwdNo: 0,

        statusExaming: -1,
        statusNoPass: 0,
        statusPass: 1,
    },

    NoteContent: {
        typeNormal: 1,
        typeMarkdown: 2
    },
    
    User: {
    }
};

var defaultAvatar = "/statics/images/common/avatar/default.jpg";