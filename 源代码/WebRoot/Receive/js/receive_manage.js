var receive_manage_tool = null; 
$(function () { 
	initReceiveManageTool(); //建立Receive管理对象
	receive_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#receive_manage").datagrid({
		url : 'Receive/list',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "receiveId",
		sortOrder : "desc",
		toolbar : "#receive_manage_tool",
		columns : [[
			{
				field : "resourceObj",
				title : "领用的资源",
				width : 140,
			},
			{
				field : "userObj",
				title : "领用用户",
				width : 140,
			},
			{
				field : "receiveNum",
				title : "领用数量",
				width : 70,
			},
			{
				field : "receiveTime",
				title : "领用时间",
				width : 140,
			},
		]],
	});

	$("#receiveEditDiv").dialog({
		title : "修改管理",
		top: "50px",
		width : 700,
		height : 515,
		modal : true,
		closed : true,
		iconCls : "icon-edit-new",
		buttons : [{
			text : "提交",
			iconCls : "icon-edit-new",
			handler : function () {
				if ($("#receiveEditForm").form("validate")) {
					//验证表单 
					if(!$("#receiveEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#receiveEditForm").form({
						    url:"Receive/" + $("#receive_receiveId_edit").val() + "/update",
						    onSubmit: function(){
								if($("#receiveEditForm").form("validate"))  {
				                	$.messager.progress({
										text : "正在提交数据中...",
									});
				                	return true;
				                } else { 
				                    return false; 
				                }
						    },
						    success:function(data){
						    	$.messager.progress("close");
						    	console.log(data);
			                	var obj = jQuery.parseJSON(data);
			                    if(obj.success){
			                        $.messager.alert("消息","信息修改成功！");
			                        $("#receiveEditDiv").dialog("close");
			                        receive_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#receiveEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#receiveEditDiv").dialog("close");
				$("#receiveEditForm").form("reset"); 
			},
		}],
	});
});

function initReceiveManageTool() {
	receive_manage_tool = {
		init: function() {
			$.ajax({
				url : "ResourceInfo/listAll",
				type : "post",
				success : function (data, response, status) {
					$("#resourceObj_resourceNo_query").combobox({ 
					    valueField:"resourceNo",
					    textField:"name",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{resourceNo:"",name:"不限制"});
					$("#resourceObj_resourceNo_query").combobox("loadData",data); 
				}
			});
			$.ajax({
				url : "UserInfo/listAll",
				type : "post",
				success : function (data, response, status) {
					$("#userObj_user_name_query").combobox({ 
					    valueField:"user_name",
					    textField:"name",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{user_name:"",name:"不限制"});
					$("#userObj_user_name_query").combobox("loadData",data); 
				}
			});
		},
		reload : function () {
			$("#receive_manage").datagrid("reload");
		},
		redo : function () {
			$("#receive_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#receive_manage").datagrid("options").queryParams;
			queryParams["resourceObj.resourceNo"] = $("#resourceObj_resourceNo_query").combobox("getValue");
			queryParams["userObj.user_name"] = $("#userObj_user_name_query").combobox("getValue");
			queryParams["receiveTime"] = $("#receiveTime").datebox("getValue"); 
			$("#receive_manage").datagrid("options").queryParams=queryParams; 
			$("#receive_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#receiveQueryForm").form({
			    url:"Receive/OutToExcel",
			});
			//提交表单
			$("#receiveQueryForm").submit();
		},
		remove : function () {
			var rows = $("#receive_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var receiveIds = [];
						for (var i = 0; i < rows.length; i ++) {
							receiveIds.push(rows[i].receiveId);
						}
						$.ajax({
							type : "POST",
							url : "Receive/deletes",
							data : {
								receiveIds : receiveIds.join(","),
							},
							beforeSend : function () {
								$("#receive_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#receive_manage").datagrid("loaded");
									$("#receive_manage").datagrid("load");
									$("#receive_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#receive_manage").datagrid("loaded");
									$("#receive_manage").datagrid("load");
									$("#receive_manage").datagrid("unselectAll");
									$.messager.alert("消息",data.message);
								}
							},
						});
					}
				});
			} else {
				$.messager.alert("提示", "请选择要删除的记录！", "info");
			}
		},
		edit : function () {
			var rows = $("#receive_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "Receive/" + rows[0].receiveId +  "/update",
					type : "get",
					data : {
						//receiveId : rows[0].receiveId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (receive, response, status) {
						$.messager.progress("close");
						if (receive) { 
							$("#receiveEditDiv").dialog("open");
							$("#receive_receiveId_edit").val(receive.receiveId);
							$("#receive_receiveId_edit").validatebox({
								required : true,
								missingMessage : "请输入领用id",
								editable: false
							});
							$("#receive_resourceObj_resourceNo_edit").combobox({
								url:"ResourceInfo/listAll",
							    valueField:"resourceNo",
							    textField:"name",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#receive_resourceObj_resourceNo_edit").combobox("select", receive.resourceObjPri);
									//var data = $("#receive_resourceObj_resourceNo_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#receive_resourceObj_resourceNo_edit").combobox("select", data[0].resourceNo);
						            //}
								}
							});
							$("#receive_userObj_user_name_edit").combobox({
								url:"UserInfo/listAll",
							    valueField:"user_name",
							    textField:"name",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#receive_userObj_user_name_edit").combobox("select", receive.userObjPri);
									//var data = $("#receive_userObj_user_name_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#receive_userObj_user_name_edit").combobox("select", data[0].user_name);
						            //}
								}
							});
							$("#receive_receiveNum_edit").val(receive.receiveNum);
							$("#receive_receiveNum_edit").validatebox({
								required : true,
								validType : "integer",
								missingMessage : "请输入领用数量",
								invalidMessage : "领用数量输入不对",
							});
							$("#receive_receiveTime_edit").datetimebox({
								value: receive.receiveTime,
							    required: true,
							    showSeconds: true,
							});
							$("#receive_purpose_edit").val(receive.purpose);
						} else {
							$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
						}
					}
				});
			} else if (rows.length == 0) {
				$.messager.alert("警告操作！", "编辑记录至少选定一条数据！", "warning");
			}
		},
	};
}
