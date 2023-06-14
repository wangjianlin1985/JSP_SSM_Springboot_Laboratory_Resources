var resourceInfo_manage_tool = null; 
$(function () { 
	initResourceInfoManageTool(); //建立ResourceInfo管理对象
	resourceInfo_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#resourceInfo_manage").datagrid({
		url : 'ResourceInfo/list',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "resourceNo",
		sortOrder : "desc",
		toolbar : "#resourceInfo_manage_tool",
		columns : [[
			{
				field : "resourceNo",
				title : "资源编号",
				width : 140,
			},
			{
				field : "resourceTypeObj",
				title : "资源类型",
				width : 140,
			},
			{
				field : "name",
				title : "资源名称",
				width : 140,
			},
			{
				field : "resourcePhoto",
				title : "资源图片",
				width : "70px",
				height: "65px",
				formatter: function(val,row) {
					return "<img src='" + val + "' width='65px' height='55px' />";
				}
 			},
			{
				field : "numberLimit",
				title : "数量限制",
				width : 140,
			},
			{
				field : "resourceNum",
				title : "资源库存",
				width : 70,
			},
			{
				field : "addDate",
				title : "加入日期",
				width : 140,
			},
		]],
	});

	$("#resourceInfoEditDiv").dialog({
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
				if ($("#resourceInfoEditForm").form("validate")) {
					//验证表单 
					if(!$("#resourceInfoEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#resourceInfoEditForm").form({
						    url:"ResourceInfo/" + $("#resourceInfo_resourceNo_edit").val() + "/update",
						    onSubmit: function(){
								if($("#resourceInfoEditForm").form("validate"))  {
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
			                        $("#resourceInfoEditDiv").dialog("close");
			                        resourceInfo_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#resourceInfoEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#resourceInfoEditDiv").dialog("close");
				$("#resourceInfoEditForm").form("reset"); 
			},
		}],
	});
});

function initResourceInfoManageTool() {
	resourceInfo_manage_tool = {
		init: function() {
			$.ajax({
				url : "ResourceType/listAll",
				type : "post",
				success : function (data, response, status) {
					$("#resourceTypeObj_typeId_query").combobox({ 
					    valueField:"typeId",
					    textField:"typeName",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{typeId:0,typeName:"不限制"});
					$("#resourceTypeObj_typeId_query").combobox("loadData",data); 
				}
			});
		},
		reload : function () {
			$("#resourceInfo_manage").datagrid("reload");
		},
		redo : function () {
			$("#resourceInfo_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#resourceInfo_manage").datagrid("options").queryParams;
			queryParams["resourceNo"] = $("#resourceNo").val();
			queryParams["resourceTypeObj.typeId"] = $("#resourceTypeObj_typeId_query").combobox("getValue");
			queryParams["name"] = $("#name").val();
			queryParams["addDate"] = $("#addDate").datebox("getValue"); 
			$("#resourceInfo_manage").datagrid("options").queryParams=queryParams; 
			$("#resourceInfo_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#resourceInfoQueryForm").form({
			    url:"ResourceInfo/OutToExcel",
			});
			//提交表单
			$("#resourceInfoQueryForm").submit();
		},
		remove : function () {
			var rows = $("#resourceInfo_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var resourceNos = [];
						for (var i = 0; i < rows.length; i ++) {
							resourceNos.push(rows[i].resourceNo);
						}
						$.ajax({
							type : "POST",
							url : "ResourceInfo/deletes",
							data : {
								resourceNos : resourceNos.join(","),
							},
							beforeSend : function () {
								$("#resourceInfo_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#resourceInfo_manage").datagrid("loaded");
									$("#resourceInfo_manage").datagrid("load");
									$("#resourceInfo_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#resourceInfo_manage").datagrid("loaded");
									$("#resourceInfo_manage").datagrid("load");
									$("#resourceInfo_manage").datagrid("unselectAll");
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
			var rows = $("#resourceInfo_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "ResourceInfo/" + rows[0].resourceNo +  "/update",
					type : "get",
					data : {
						//resourceNo : rows[0].resourceNo,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (resourceInfo, response, status) {
						$.messager.progress("close");
						if (resourceInfo) { 
							$("#resourceInfoEditDiv").dialog("open");
							$("#resourceInfo_resourceNo_edit").val(resourceInfo.resourceNo);
							$("#resourceInfo_resourceNo_edit").validatebox({
								required : true,
								missingMessage : "请输入资源编号",
								editable: false
							});
							$("#resourceInfo_resourceTypeObj_typeId_edit").combobox({
								url:"ResourceType/listAll",
							    valueField:"typeId",
							    textField:"typeName",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#resourceInfo_resourceTypeObj_typeId_edit").combobox("select", resourceInfo.resourceTypeObjPri);
									//var data = $("#resourceInfo_resourceTypeObj_typeId_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#resourceInfo_resourceTypeObj_typeId_edit").combobox("select", data[0].typeId);
						            //}
								}
							});
							$("#resourceInfo_name_edit").val(resourceInfo.name);
							$("#resourceInfo_name_edit").validatebox({
								required : true,
								missingMessage : "请输入资源名称",
							});
							$("#resourceInfo_resourcePhoto").val(resourceInfo.resourcePhoto);
							$("#resourceInfo_resourcePhotoImg").attr("src", resourceInfo.resourcePhoto);
							$("#resourceInfo_numberLimit_edit").val(resourceInfo.numberLimit);
							$("#resourceInfo_numberLimit_edit").validatebox({
								required : true,
								missingMessage : "请输入数量限制",
							});
							$("#resourceInfo_resourceNum_edit").val(resourceInfo.resourceNum);
							$("#resourceInfo_resourceNum_edit").validatebox({
								required : true,
								validType : "integer",
								missingMessage : "请输入资源库存",
								invalidMessage : "资源库存输入不对",
							});
							$("#resourceInfo_addDate_edit").datebox({
								value: resourceInfo.addDate,
							    required: true,
							    showSeconds: true,
							});
							$("#resourceInfo_resourceDesc_edit").val(resourceInfo.resourceDesc);
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
