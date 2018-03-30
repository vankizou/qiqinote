/**
 * Created by vanki on 2017/6/19.
 */

/*$(function () {
    changeFooterMarginTop();
});*/

function changeFooterMarginTop(correctHeight) {
    if (!correctHeight) correctHeight = 0;

    $('#copyright').css('display', 'block');

    var viewHeight = $(window).height();    // 可视区域高
    var totalHeight = $(document).height();    // 总高
    var distTopHeight = $('#copyright').offset().top;   // copyright与顶部高
    var height = $('#copyright').outerHeight(true);     // copyright高

    if (totalHeight > viewHeight) {
        $('#copyright').css("margin-top", 0);
        return;
    }

    var oldMarginTop = $("#copyright").css("margin-top");
    if (!oldMarginTop) oldMarginTop = 0;
    oldMarginTop = oldMarginTop.replace('px', '');

    var currHeight = viewHeight - distTopHeight - height + Number(oldMarginTop) + correctHeight
    $('#copyright').css("margin-top", currHeight);
}
