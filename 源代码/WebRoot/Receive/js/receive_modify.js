$(function () {
	$.ajax({
		url : "Receive/" + $("#receive_receiveId_edit").val() + "/update",
		type : "get",
		data : {
			//receiveId : $("#receive_receiveId_edit").val(),
		},
		beforeSend : function () {
			$.messager.progress({
				text : "正在获取中...",
			});
		},
		success : function (receive, response, status) {
			$.messager.progress("close");
			if (receive) { 
				$("#receive_receiveId_edit").val(receive.receiveId);
				$("#receive_receiveId_edit").validatebox({
					required : true,
					missingMessage : "请输入领用id",
					editable: false
				});
				$("#receive_resourceObj_resourceNo_edit").combobox({
					url:"../ResourceInfo/listAll",
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
					url:"../UserInfo/listAll",
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

	$("#receiveModifyButton").click(function(){ 
		if ($("#receiveEditForm").form("validate")) {
			$("#receiveEditForm").form({
			    url:"Receive/" +  $("#receive_receiveId_edit").val() + "/update",
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
			$("#receiveEditForm").submit();
		} else {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
		}
	});
});
