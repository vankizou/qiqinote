<%--
  Created by IntelliJ IDEA.
  User: vanki
  Date: 16/10/28
  Time: 12:05
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>

<link rel="shortcut icon" type="image/x-icon" href="/statics/images/common/logo/logo.ico"/>
<script type="text/javascript" src="/statics/third/jquery/jquery-3.1.1.min.js"></script>

<script type="text/javascript" src="/statics/third/jqueryForm/jquery.form.min.js"></script>

<%-- dialog --%>
<link rel="stylesheet" href="/statics/third/dialog/css/ui-dialog.css">
<script type="text/javascript" src="/statics/third/dialog/dist/dialog-min.js"></script>
<%------------%>
<script type="text/javascript" src="/statics/third/layer/layer.js"></script>

<script type="text/javascript" src="/statics/third/bootstrap/js/bootstrap.min.js"></script>
<link href="/statics/third/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="/statics/third/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css">
<link href="/statics/third/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css">


<link href="/statics/third/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<link href="/statics/css/common/common.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/statics/js/common/stringUtil.js"></script>
<script type="text/javascript" src="/statics/js/common/varUtil.js"></script>
<script type="text/javascript" src="/statics/js/common/msgUtil.js"></script>
<script type="text/javascript" src="/statics/js/common/vankiAjax.js"></script>
<script type="text/javascript" src="/statics/js/constant/constAjaxUrl.js"></script>
<script type="text/javascript" src="/statics/js/constant/constDB.js"></script>
<script type="text/javascript" src="/statics/js/constant/constStatusCode.js"></script>
<script type="text/javascript" src="/statics/js/common/vankiUploadImage.js"></script>


<!-- Piwik -->
<script type="text/javascript">
    var _paq = _paq || [];
    /* tracker methods like "setCustomDimension" should be called before "trackPageView" */
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function() {
        var u="//piwik.istimer.com/";
        _paq.push(['setTrackerUrl', u+'piwik.php']);
        _paq.push(['setSiteId', '2']);
        var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
        g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
    })();
</script>
<!-- End Piwik Code -->

<%-- 百度统计 --%>
<script>
    var _hmt = _hmt || [];
    (function() {
        var hm = document.createElement("script");
        hm.src = "https://hm.baidu.com/hm.js?2a12de40c5d7a21b012acccb2287d222";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    })();
</script>

<%-- 百度站长 --%>
<script>
    (function(){
        var bp = document.createElement('script');
        var curProtocol = window.location.protocol.split(':')[0];
        if (curProtocol === 'https') {
            bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';
        }
        else {
            bp.src = 'http://push.zhanzhang.baidu.com/push.js';
        }
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(bp, s);
    })();
</script>