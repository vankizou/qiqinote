$(document).ready(function() {
    $('#main').addClass('loaded'); //开场拉幕效果
    // setCurrentNavMenu();

    setTimeout(function(){	$('#loader-wrapper').remove();},1000);

    $('.slide .icon li').not('.up,.down').mouseenter(function(){
        $('.slide .info').addClass('hover');
        $('.slide .info li').hide();
        $('.slide .info li.'+$(this).attr('class')).show();//.slide .info li.qq
    });
    $('.slide').mouseleave(function(){
        $('.slide .info').removeClass('hover');
    });

    $('#btn').click(function(){
        $('.slide').toggle();
        if($(this).hasClass('index_cy')){
            $(this).removeClass('index_cy');
            $(this).addClass('index_cy2');
        }else{
            $(this).removeClass('index_cy2');
            $(this).addClass('index_cy');
        }

    });
});

/**
 * 设置当前导航菜单
 */
function setCurrentNavMenu() {
    var url = location.pathname, navMenus = $(".nav-menu-box li");
    $.each(navMenus, function(p1, p2) {
        if (url.indexOf('/b/' + p1) >= 0){
            navMenus.eq(p1).addClass("am-active");
        }
    });

    //关于菜单高亮
    if(url.indexOf('/about') >= 0){
        navMenus.last().addClass("am-active");
    }
}