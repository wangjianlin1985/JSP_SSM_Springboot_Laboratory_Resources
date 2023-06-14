$(function () {
	$.ajax({
		url : "ResourceInfo/" + $("#resourceInfo_resourceNo_edit").val() + "/update",
		type : "get",
		data : {
			//resourceNo : $("#resourceInfo_resourceNo_edit").val(),
		},
		beforeSend : function () {
			$.messager.progress({
				text : "正在获取中...",
			});
		},
		success : function (resourceInfo, response, status) {
			$.messager.progress("close");
			if (resourceInfo) { 
				$("#resourceInfo_resourceNo_edit").val(resourceInfo.resourceNo);
				$("#resourceInfo_resourceNo_edit").validatebox({
					required : true,
					missingMessage : "请输入资源编号",
					editable: false
				});
				$("#resourceInfo_resourceTypeObj_typeId_edit").combobox({
					url:"../ResourceType/listAll",
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
				$("#resourceInfo_resourcePhotoImg").attr("src", "../" +　resourceInfo.resourcePhoto);
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

	$("#resourceInfoModifyButton").click(function(){ 
		if ($("#resourceInfoEditForm").form("validate")) {
			$("#resourceInfoEditForm").form({
			    url:"ResourceInfo/" +  $("#resourceInfo_resourceNo_edit").val() + "/update",
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
                	var obj = jQuery.parseJSON(data);
                    if(obj.success){
                        $.messager.alert("消息","信息修改成功！");
                        location.href="frontlist";
                    }else{
                        $.messager.alert("消息",obj.message);
                    } 
			    }
			});
			//提交表单
			$("#resourceInfoEditForm").submit();
		} else {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
		}
	});
});
