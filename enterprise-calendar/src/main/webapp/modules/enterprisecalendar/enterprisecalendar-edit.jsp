<%--
  Created by IntelliJ IDEA.
  User: guo_jh
  Date: 2018/6/27
  Time: 19:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType = "text/html;charset=UTF-8" language = "java" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge,IE=10,chrome=1">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html >
<head >
    <title >企业日历</title >
    <link rel = "stylesheet" href = "${pageContext.request.contextPath}/ui/layui/css/layui.css" media = "all" >
    <script src = "${pageContext.request.contextPath}/ui/js/jquery/jquery-1.11.2.min.js" charset = "utf-8" ></script >
    <script src = "${pageContext.request.contextPath}/ui/layui/layui.js" charset = "utf-8" ></script >

    <script src = "${pageContext.request.contextPath}/modules/enterprisecalendar/ctrl/enterprisecalendar-edit.js"
            charset = "utf-8" ></script >
    <script >
        var basepath = "${pageContext.request.contextPath}";
    </script >
</head >
<body >
<div style = "margin:20px;" >
    <form class = "layui-form" action = "" >
        <div class = "layui-form-item" >
            <label class = "layui-form-label" >选中日期</label >
            <div class = "layui-input-block" >
                <input type = "text" name = "workDay" required lay-verify = "required" placeholder = "请输入标题"
                       value = "<fmt:formatDate pattern="yyyy-MM-dd" value="${day.workDay}" />"
                       autocomplete = "off" class = "layui-input" disabled >
            </div >
        </div >
        <div class = "layui-form-item" >
            <label class = "layui-form-label" >日期类型</label >
            <div class = "layui-input-block" >
                <input type = "radio" name = "dayType" value = "A" title = "放假" <c:if test="${day.dayType eq 'A'}">checked</c:if>/>
                <input type = "radio" name = "dayType" value = "B" title = "上班" <c:if test="${day.dayType eq 'B'}">checked</c:if>>
                <input type = "radio" name = "dayType" value = "0" title = "默认" <c:if test="${day.dayType eq '0' or empty day.dayType}">checked</c:if> >
            </div >
        </div >
        <div class = "layui-form-item layui-form-text" >
            <label class = "layui-form-label" >说明</label >
            <div class = "layui-input-block" >
                <textarea name = "workDayDesc" placeholder = "请输入内容" class = "layui-textarea" >${day.workDayDesc}</textarea >
            </div >
        </div >
        <div class = "layui-form-item" >
            <div class = "layui-input-block" >
                <button class = "layui-btn" lay-submit lay-filter = "submitBtn" type = "button" >提交</button >
                <button type = "button" class = "layui-btn layui-btn-primary" onclick = "CloseWin()" >取消</button >
            </div >
        </div >
    </form >
</div >
</body >
</html >
