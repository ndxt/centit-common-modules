<%--
  Created by IntelliJ IDEA.
  User: guo_jh
  Date: 2018/6/27
  Time: 19:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType = "text/html;charset=UTF-8" language = "java" %>
<html >
<head >
    <title >企业日历</title >
    <link rel = "stylesheet" href = "${pageContext.request.contextPath}/ui/layui/css/layui.css" media = "all" >
    <script src = "${pageContext.request.contextPath}/ui/js/jquery/jquery-1.11.2.min.js" charset = "utf-8" ></script >
    <script src = "${pageContext.request.contextPath}/ui/layui/layui.js" charset = "utf-8" ></script >

    <link rel = "stylesheet"
          href = "${pageContext.request.contextPath}/modules/enterprisecalendar/css/enterprisecalendar.css"
          media = "all" >
    <script src = "${pageContext.request.contextPath}/modules/enterprisecalendar/ctrl/enterprisecalendar.js"
            charset = "utf-8" ></script >
    <script >
        var basepath = "${pageContext.request.contextPath}";
    </script >
</head >
<body >
<div class = "layui-form-item layui-diy-laydate" id = "enterpriseCalendar" ></div >
</body >
</html >
