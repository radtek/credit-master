$(function() {
	$.repayTrailNormal = {
			/** 表格数据源地址 **/
			dataGridUrl : global.contextPath + '/repay/repayInfo/listRepayTrailNormal',
			/** 客户信息表格 **/
			dataGrid : $('#listRepayTrailNormalDataGrid'),
			/** 分页控件 **/
			pager : undefined,
			/** 查询条件数据项表单实例 **/
			/** 每页显示的记录条数，默认为10 **/
			pageSize : 10,
			/** 设置每页记录条数的列表 **/
			pageSizeList : [10,20,30,40,50],
			/** 加载表格数据 **/
			reloadDataGrid : function() {
				var name=parent.$('#name').val();
				var mphone=parent.$('#mphone').val();
				var idnum=parent.$('#idnum').val();
				var querySelectvalue = parent.$('#repayType').combobox('getValue');
				var grantMoneyDateStart=parent.$("#grantMoneyDateStart").datebox("getValue");
				var contractNum=parent.$("#contractNum").val();
				if ($.isEmpty(grantMoneyDateStart)){
					$.messager.alert('提示信息','时间选项必填','warning');
					return;
				}
				if ($.isEmpty(idnum) && $.isEmpty(contractNum)){
					$.messager.alert('提示信息','请输入身份证号码或者合同编号进行查询!','warning');
					return;
				} 			
				var params={};
				var url = global.contextPath;			
				params.name=name;
				params.mphone=mphone;
				params.idnum=idnum;
				var queryString="?name="+name+"&mphone="+mphone+"&idnum="+idnum+"&repayType="+querySelectvalue+"&grantMoneyDateStart="+grantMoneyDateStart+"&contractNum="+contractNum;	
				var	queryParam ={};
				queryParam.url=$.repayTrailNormal.dataGridUrl+queryString;				
				$.repayTrailNormal.dataGrid.datagrid('reloadData',queryParam);
			}
		}
		/** 分页参数（page:当前第N页，rows:一页N行） **/
		$.repayTrailNormal.pg = {
			'page' : 1,
			'rows' : $.repayTrailNormal.pageSize
		}
	
	$.repayTrailNormal.dataGrid.datagrid({
		pg : $.repayTrailNormal.pg,
		/** 提交方式 **/
		method : 'get',
		/** 是否显示行号 **/
		rownumbers : true,
		/** 是否单选 **/
		singleSelect : true,
		/** 是否可折叠的 **/
		collapsible : false,
		/** 自适应列宽 **/
		fitColumns : false,
		fit : true,
		//height : '100%',
		/** 是否开启分页 **/
		pagination : true,
		loadMsg : '数据加载中,请稍等...',
		columns : [[
				      /** 列定义 **/
				      {field : 'name',title : '借款人' ,width : "7%"},
				      {field : 'loanType',title : '借款类型' ,width : "7%"},
				      {field : 'idNum',title : '身份证号' ,width : "8%"},
				      {field : 'pactMoney',title : '合同金额（元）' ,width : "7%" ,vType : 'rmb' },
				      {field : 'overAMT',title : '逾期本息和' ,width : "7%" ,vType : 'rmb',
				    	  	formatter:function(value,row,index){
					    	return  row.overInterest+row.overCorpus;
					    	}
				      },
				      {field : 'currAMT',title : '当期应还' ,width : "7%",vType : 'rmb',
				    	  formatter:function(value,row,index){
				    		  if(row.requestState=='未申请'){
						    		return 	row.currInterest+row.currCorpus;
						    		}
						    	
						    }
				      },
				      {field : 'fine',title : '罚息' ,width : "7%",vType : 'rmb',
				    	  formatter:function(value,row,index){
			    		  if(row.requestState=='未申请'){
					    		return 	row.fine;
					    		}
					    	
					    }},
				      {field : 'penalty',title : '违约金' ,width : "7%",vType : 'rmb',
				    	  formatter:function(value,row,index){
				    		  if(row.requestState=='未申请'){
						    		return 	0;
						    		}
						    	
						    }
				      },
				      {field : 'inerest',title : '利息' ,width : "7%",vType : 'rmb',
				    	  formatter:function(value,row,index){
						    	if(row.requestState=='未申请'){//一次性还款
						    		return row.overInterest+row.currInterest;
						    		}
						    	
						    }
				      
				      },
				      {field : 'corpus',title : '本金' ,width : "7%",vType : 'rmb',
				    	  formatter:function(value,row,index){
				    		  if(row.requestState=='未申请'){
						    		return 	row.overCorpus+row.currCorpus ;
						    		}
						    	
						    }
				      
				      
				      },
				      {field : 'giveBackRate',title : '退费' ,width : "6%",vType : 'rmb',
				    	  formatter:function(value,row,index){
						    	if(row.requestState=='未申请'){//一次性还款
						    		return 	0;
						    		}						    
						    }
				      
				      },
				      {field : 'accAmount',title : '挂账金额' ,width : "5%",vType : 'rmb', 
				    	  formatter:function(value,row,index){
			    		  return row.accAmount;
					    }},
				      {field : 'overAccRepayAmt',title : '逾期应还' ,width : "5%",vType : 'rmb',
				    	  formatter:function(value,row,index){
				    		  return row.overInterest+row.overCorpus+row.fine;
						    }
				      
				      },
				      {field : 'acctTotalAMT',title : '应还总额' ,width : "5%",vType : 'rmb',
				    	  formatter:function(value,row,index){						    
						    	if(row.requestState=='未申请'){
						    		return 	row.currAmount;
						    		}
						    }  
				      
				      },
				      {field : 'contractNum',title : '合同编号' ,width : "7%"}
				]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.repayTrailNormal.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.repayTrailNormal.pageSizeList,
		toolbar : '#tb',
		/** 自定义行样式 **/
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		}
		
		
	});
	
	
	$.repayTrailNormal.pager = $.repayTrailNormal.dataGrid.datagrid('getPager');
	$.repayTrailNormal.pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.repayTrailNormal.pg.page = pageNumber;
			$.repayTrailNormal.pg.rows = pageSize;
			$.repayTrailNormal.reloadDataGrid();
		}
	});
	
	$.repayTrailNormal.reloadDataGrid();	
});