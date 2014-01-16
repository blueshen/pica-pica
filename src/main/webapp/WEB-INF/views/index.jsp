<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/common.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <script type="text/javascript" src="${ctx}/static/js/fileupload.js"></script>
    <title> Pica Pica 图像对比 </title>
</head>
<body>
<div class="page-header">
    <div class="row">
        <div class="col-md-10">
            <h1>Pica Pica - Image Compare</h1>
        </div>
        <div class="col-md-1 col-offset-1">
            <img src="${ctx}/static/logo/picapica.png" class="img-circle" width="100px" height="80px"/>
        </div>

    </div>
</div>
<div class="container">
    <div class="row">
        <form class="form-horizontal" action="${ctx}/imagediff" method="post"
              enctype="multipart/form-data">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label class="control-label" for="sourcefile">源图片：</label>
                        <input id="sourcefile" type="file" class="input-medium" name="sourceFile" lang="zh_CN" accept="image/*"/>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label class="control-label" for="candidatefile">要对比的图片：</label>
                        <input id="candidatefile" type="file" class="input-medium" name="candidateFile" lang="zh_CN" accept="image/*"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 col-md-offset-6">
                    <div class="form-group">
                        <input type="submit" id="uploadBtn" class="btn btn-info" value="对比"/>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <br>
    <br>

    <div class="row">
        <div class="span12">
            <c:if test="${result.match=='false'}">
                <p>红色标识不同部分：</p>

                <p>耗时:${durationTime}ms</p>
                <img src="${ctx}/upload/${result.diffImageId}.png" class="img-thumbnail">
            </c:if>
            <c:if test="${result.match=='true'}">
                <p>pica pica认为图片是没有什么不同的。</p>
            </c:if>
        </div>

    </div>
</div>
</body>
</html>