$(function () {
	$("#resourceInfo_resourceNo").validatebox({
		required : true, 
		missingMessage : '请输入资源编号',
	});

	$("#resourceInfo_resourceTypeObj_typeId").combobox({
	    url:'ResourceType/listAll',
	    valueField: "typeId",
	    textField: "typeName",
	    panelHeight: "auto",
        editable: false, //不允许手动输入
        required : true,
        onLoadSuccess: function () { //数据加载完毕事件
            var data = $("#resourceInfo_resourceTypeObj_typeId").combobox("getData"); 
            if (data.length > 0) {
                $("#resourceInfo_resourceTypeObj_typeId").combobox("select", data[0].typeId);
            }
        }
	});
	$("#resourceInfo_name").validatebox({
		required : true, 
		missingMessage : '请输入资源名称',
	});

	$("#resourceInfo_numberLimit").validatebox({
		required : true, 
		missingMessage : '请输入数量限制',
	});

	$("#resourceInfo_resourceNum").validatebox({
		required : true,
		validType : "integer",
		missingMessage : '请输入资源库存',
		invalidMessage : '资源库存输入不对',
	});

	$("#resourceInfo_addDate").datebox({
	    required : true, 
	    showSeconds: true,
	    editable: false
	});

	//单击添加按钮
	$("#resourceInfoAddButton").click(function () {
		//验证表单 
		if(!$("#resourceInfoAddForm").form("validate")) {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
		} else {
			$("#resourceInfoAddForm").form({
			    url:"ResourceInfo/add",
			    onSubmit: function(){
					if($("#resourceInfoAddForm").form("validate"))  { 
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
                    //此处data={"Success":true}是字符串
                	var obj = jQuery.parseJSON(data); 
                    if(obj.success){ 
                        $.messager.alert("消息","保存成功！");
                        $("#resourceInfoAddForm").form("clear");
                    }else{
                        $.messager.alert("消息",obj.message);
                    }
			    }
			});
			//提交表单
			$("#resourceInfoAddForm").submit();
		}
	});

	//单击清空按钮
	$("#resourceInfoClearButton").click(function () { 
		$("#resourceInfoAddForm").form("clear"); 
	});
});
