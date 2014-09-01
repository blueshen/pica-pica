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
<ol class="breadcrumb">
    <li><a href="${ctx}/">Home</a></li>
</ol>
<div class="container">
    <div class="">
        <div class="row">
            <h2>Pica Pica - Image Compare</h2>
        </div>
        <div class="row">
            <div class="col-md-1">
                <label>阈值</label>
                <input class="form-control" name="similarityThreshold" value="${imageForm.similarityThreshold}"
                       placeholder="相似度阈值"/>
            </div>
            <div class="col-md-2">
                <label>行</label>
                <select class="form-control" name="row" >
                    <option value="1"  ${imageForm.row =='1'?'selected':""}>1行</option>
                    <option value="5"  ${imageForm.row =='5'?'selected':""}>5行</option>
                    <option value="10" ${imageForm.row =='10'?'selected':""}>10行</option>
                    <option value="15" ${imageForm.row =='15'?'selected':""}>15行</option>
                    <option value="20"  ${imageForm.row =='20'?'selected':""}>20行</option>
                </select>
            </div>
            <div class="col-md-2">
                <label>列</label>
                <select class="form-control" name="col">
                    <option value="1"  ${imageForm.col =='1'?'selected':""}>1列</option>
                    <option value="5" ${imageForm.col =='5'?'selected':""}>5列</option>
                    <option value="10" ${imageForm.col =='10'?'selected':""}>10列</option>
                    <option value="15" ${imageForm.col =='15'?'selected':""}>15列</option>
                    <option value="20" ${imageForm.col =='20'?'selected':""}>20列</option>
                </select>
            </div>
        </div>
        <hr>
        <div class="row">
            <form class="form" action="${ctx}/image/compare" method="post"
                  enctype="multipart/form-data">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="sourcefile">源图片：</label>
                            <input id="sourcefile" type="file" class="input-medium" name="sourceFile"
                                   value="${imageForm.sourceFile}" lang="zh_CN" accept="image/*"/>
                        </div>
                        <div class="form-group">
                            <label for="candidatefile">要对比的图片：</label>
                            <input id="candidatefile" type="file" class="input-medium" name="candidateFile"
                                   value="${imageForm.candidateFile}" lang="zh_CN" accept="image/*"/>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 col-md-offset-6">
                        <div class="form-group">
                            <input type="submit" id="compareBtn" class="btn btn-info" value="对比"/>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <br>

    <div class="row">
        <div class="col-md-12">
            <c:if test="${result.match=='false'}">
                <div class="alert alert-danger">
                    <p>红色标识不同部分：</p>

                    <p>耗时:${durationTime}ms</p>
                </div>
                <%--<a href="${ctx}/api/image/${result.diffImageId}">查看</a>--%>
                <img src="${ctx}/upload/${result.diffImageId}.png" class="img-thumbnail">
            </c:if>
            <c:if test="${result.match=='true'}">
                <div class="alert alert-success">
                    <p>pica pica认为图片是没有什么不同的。</p>
                </div>
            </c:if>
        </div>

    </div>
</div>
</body>
</html>