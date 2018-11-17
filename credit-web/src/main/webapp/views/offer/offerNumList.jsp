<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>划扣次数配置</title>
<%
	String contextPath = request.getContextPath();
	session.setAttribute("path", contextPath);
%>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css"  rel="stylesheet"  href="<%=contextPath %>/resources/css/sysIcon.css"/>
<script type="text/javascript">
	importPluginsExt([ 'panel', 'combobox', 'window', 'layout', 'datagrid',
			'pagination', 'form', 'tooltip', 'validatebox' ], 'business',
			function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
		            urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
					urlJs.push(global.contextPath + '/resources/js/offer/offerNum.js');
					importJSExt(urlJs, function() {
						/** 脚本加载成功回调方法 **/
					});
				});
			});
</script>
<style type="text/css">
</style>
</head>
<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<div class="easyui-panel" title="查询条件"
		style="padding: 5px; height: 75px; margin: 0px;"
		data-options="region:'north'">
		<form id="searchForm">
			<table cellpadding="5">
				<tr>
					<td>姓名:</td>
					<td><input class="easyui-textbox" type="text" name="name"></input></td>
					<td>工号:</td>
					<td><input class="easyui-textbox" type="text" name="userCode"></input></td>
				</tr>
			</table>
		</form>
	</div>
	<div class="easyui-panel" style="padding: 0px; margin: 0px;"
		data-options="region:'center'">
		<table id="testDataGrid" class="easyui-datagrid" data-options=""></table>
		<!-- 表格标签 -->
		<div id="tb" style="padding: 3px;">
			<div style="margin-bottom: 0px">
			<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true">查询</a>
			<a href="javascript:void(0)" id="clearCondition" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<%-- <sec:authorize ifAnyGranted="/offerNum/insert"> --%>
					<a href="#" class="easyui-linkbutton" id="addBut" iconCls="icon-add" plain="true">新增</a>
				<%-- </sec:authorize>
				<sec:authorize ifAnyGranted="/offerNum/update"> --%>
					<a href="#" class="easyui-linkbutton" id="updateBut" iconCls="icon-save" plain="true">修改</a>
				<%-- </sec:authorize>
				<sec:authorize ifAnyGranted="/offerNum/delete"> --%>
					<a href="#" class="easyui-linkbutton" id="deleteBut" iconCls="icon-remove" plain="true">删除</a>
				<%-- </sec:authorize> --%>
			</div>
		</div>
	</div>
	
	<!--新增用户 -->
	<div id="insertOfferNumPanel" class="easyui-window easyui-layout"
		title="新增用户划扣次数" style="padding: 20px;"
		data-options="width:400,height:250">
		<div>
			<form method="post" id="dataForm">
				<table style="text-align: right;">
					<tr>
						<td>工号：</td>
						<td><input class="easyui-textbox"  name="userCode"  id="userCode" data-options="required:true" /></td>
					</tr>
					<tr>
						<td>划扣次数：</td>
						<td><input class="easyui-textbox" name="offerCount"  id="offerCount"  data-options="required:true" /></td>
					</tr>
					<tr><td><label style="color:red;">注：-1为无限次</label></td></tr>
				</table>
			</form>
			<br />
			<div style="text-align: center; padding: 5px">
				<a href="#" class="easyui-linkbutton"  id="offerNumSubmit" iconCls="icon-ok"  plain="true">提交</a> 
				<a href="#" class="easyui-linkbutton commonCloseBut"  id="offerNumColse"  iconCls="icon-clear"  plain="true">取消</a>
			</div>
		</div>
	</div>
</body>
</html>
