$(function () {
    /**
     * 配置菜单导航坐标
     */
    var md = $("#vanki-editormd-view-note");
    var mdOffset = md.offset();
    $("#j_toc_menu_content").hide();

    var initOffsetTop = mdOffset.top - 34;
    var initOffsetLeft = $(window).width() - mdOffset.left - md.width() - 30;
    $("#j_toc_menu").css("margin-top", initOffsetTop);
    $("#j_toc_menu").css("margin-right", initOffsetLeft);

    $("#j_aaa").dblclick(function () {
        $(".editormd-markdown-toc").slideToggle("slow");
    });

    $("#j_toc_menu .mfb-component__wrap").hover(function () {
        $("#j_toc_menu_content").show()
    });

    $("#j_toc_menu").mouseleave(function () {
        $("#j_toc_menu_content").hide()
    });

    $(window).scroll(function (event) {
        var scrollTop = $(document).scrollTop();
        var offset = scrollTop;
        if (scrollTop <= initOffsetTop) {
            offset = initOffsetTop - scrollTop;
        } else {
            offset = 0;
        }
        if (offset < 10) offset = 10;
        $("#j_toc_menu").css("margin-top", offset);
    });
});