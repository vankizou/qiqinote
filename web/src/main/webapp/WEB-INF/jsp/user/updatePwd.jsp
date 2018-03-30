<%--
  Created by IntelliJ IDEA.
  User: vanki
  Date: 2017/5/25
  Time: 14:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改密码-奇奇笔记</title>
    <jsp:include page="../common/common.jsp"></jsp:include>

    <%--<link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400" rel="stylesheet" />--%>
    <link href="/statics/css/kapian/5grid/core.css" rel="stylesheet" />
    <link href="/statics/css/kapian/5grid/core-1200px.css" rel="stylesheet" />
    <link href="/statics/css/kapian/5grid/core-desktop.css" rel="stylesheet" />
    <link href="/statics/css/kapian/style.css" rel="stylesheet" />
    <link href="/statics/css/kapian/style-desktop.css" rel="stylesheet" />

    <script type="text/javascript" src="/statics/js/kapian/jquery-1.9.1min.js"></script>
    <script type="text/javascript" src="/statics/js/kapian/5grid/init.js"></script>
    <script type="text/javascript" src="/statics/js/kapian/jquery.formerize-1.1.js"></script>
    <script type="text/javascript" src="/statics/js/kapian/init.js"></script>

</head>
<body class="homepage">
<jsp:include page="../common/top.jsp"></jsp:include>

<!-- Wrapper-->
<div id="wrapper">

    <!-- Nav -->
    <nav id="nav">
        <a href="#me" class="icon icon-user active" style="color: blue;"><span>Me</span></a>
        <a href="#work" class="icon icon-folder" style="color: blue;"><span>Work</span></a>
        <a href="#email" class="icon icon-mail" style="color: blue;"><span>Email Me</span></a>
        <a href="#" class="icon icon-twitter" style="color: blue;"><span>Twitter</span></a>
    </nav>
    <!-- Main -->
    <div id="main">

        <!-- Me -->
        <article id="me" class="panel">
            <header>
                <h1>Jane Doe</h1>
                <span class="byline">Senior Astral Projectionist</span>
            </header>
            <a href="#work" class="jumplink pic">
                <span class="jumplink arrow icon icon-right"><span>See my work</span></span>
                <img src="images/me.jpg" alt=""/>
            </a>
        </article>

        <!-- Work -->
        <article id="work" class="panel">
            <header>
                <h2>Work</h2>
            </header>
            <p>
                Phasellus enim sapien, blandit ullamcorper elementum eu, condimentum eu elit.
                Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia
                luctus elit eget interdum.
            </p>
            <section class="5grid is-gallery">
                <div class="row">
                    <div class="4u">
                        <a href="http://flypixel.com/knob/3495684098026313" class="image image-full"><img
                                src="images/1.jpg" alt=""></a>
                    </div>
                    <div class="4u">
                        <a href="http://flypixel.com/contact-login-form/8829734266026310" class="image image-full"><img
                                src="images/2.jpg" alt=""></a>
                    </div>
                    <div class="4u">
                        <a href="http://flypixel.com/wallpaper-pack/3755398966026313" class="image image-full"><img
                                src="images/3.jpg" alt=""></a>
                    </div>
                </div>
                <div class="row">
                    <div class="4u">
                        <a href="http://flypixel.com/44-shades-of-free-icons/8268508056006318" class="image image-full"><img
                                src="images/4.jpg" alt=""></a>
                    </div>
                    <div class="4u">
                        <a href="http://flypixel.com/flip-clock/3655053956026317" class="image image-full"><img
                                src="images/5.jpg" alt=""></a>
                    </div>
                    <div class="4u">
                        <a href="http://flypixel.com/dropdown/5290304620795313" class="image image-full"><img
                                src="images/6.jpg" alt=""></a>
                    </div>
                </div>
                <div class="row">
                    <div class="4u">
                        <a href="http://flypixel.com/pie-charts/1759361594616314" class="image image-full"><img
                                src="images/7.jpg" alt=""></a>
                    </div>
                    <div class="4u">
                        <a href="http://flypixel.com/sliding-selector-bar/7042935581006315"
                           class="image image-full"><img src="images/8.jpg" alt=""></a>
                    </div>
                    <div class="4u">
                        <a href="http://flypixel.com/dark-as-hell-ui/5418809286006313" class="image image-full"><img
                                src="images/9.jpg" alt=""></a>
                    </div>
                </div>
                <div class="row">
                    <div class="4u">
                        <a href="http://flypixel.com/cityscape/9803996277226316" class="image image-full"><img
                                src="images/10.jpg" alt=""></a>
                    </div>
                    <div class="4u">
                        <a href="http://flypixel.com/dropdown/8259263378026313" class="image image-full"><img
                                src="images/11.jpg" alt=""></a>
                    </div>
                    <div class="4u">
                        <a href="http://flypixel.com/wood-ui-kit/3574765984616310" class="image image-full"><img
                                src="images/12.jpg" alt=""></a>
                    </div>
                </div>
                <div class="row">
                    <div class="4u">
                        <a href="http://flypixel.com/upload-button/9737964647895311" class="image image-full"><img
                                src="images/13.jpg" alt=""></a>
                    </div>
                    <div class="4u">
                        <a href="http://flypixel.com/collapsible-menunavigation/5743165610406316"
                           class="image image-full"><img src="images/14.jpg" alt=""></a>
                    </div>
                    <div class="4u">
                        <a href="http://flypixel.com/3d-button/5506786525606318" class="image image-full"><img
                                src="images/15.jpg" alt=""></a>
                    </div>
                </div>
            </section>
        </article>

        <!-- Email -->
        <article id="email" class="panel">
            <header>
                <h2>Email Me</h2>
            </header>
            <form action="#" method="post">
                <div class="5grid">
                    <div class="row">
                        <div class="6u">
                            <input type="text" class="text" name="name" placeholder="Name"/>
                        </div>
                        <div class="6u">
                            <input type="text" class="text" name="email" placeholder="Email"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="12u">
                            <input type="text" class="text" name="subject" placeholder="Subject"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="12u">
                            <textarea name="message" placeholder="Message"></textarea>
                        </div>
                    </div>
                    <div class="row">
                        <div class="12u">
                            <input type="submit" class="button" value="Send Message"/>
                        </div>
                    </div>
                </div>
            </form>
        </article>

    </div>

    <!-- Footer -->
    <%--<div id="footer">
        <ul class="links">
            <li>&copy; Jane Doe</li>
            <li>Images : <a href="#">fotogrph</a></li>
            <li>collect from: <a href="http://www.cssmoban.com/" title="模板之家">模板之家</a></li>
        </ul>
    </div>--%>

</div>
<jsp:include page="../common/footer.jsp"></jsp:include>
</body>

</html>
