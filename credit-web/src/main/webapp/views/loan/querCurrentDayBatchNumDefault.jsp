<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<title>债权导出供第三方</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
			                  ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					 urlJs.push(global.contextPath + '/resources/js/loan/querCurrentDayBatchNumDefault.js?v1'); 					
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				});
			});
		</script>		
	</head>
	<body class="easyui-layout">
		<jsp:include page="/views/common/initPageMast.jsp" />
		<div data-options="border:false,region:'center',noheader:true">
			<div id="selGrantMoney" style="padding-bottom: 0px;margin-bottom: 0px;">已选放款总金额：<span id="selGrantMoneyCount">0</span>；已选合同总金额：<span id="selPactMoneyCount">0</span></div> 
			<input   value="${org}" id="orgTemp" type="hidden"/>
			<table id="querCurrentDayBatchNumDetailDataGrid" class="easyui-datagrid" data-options="toolbar:'#selGrantMoney'" ></table>
		</div>
	</body>
</html>
