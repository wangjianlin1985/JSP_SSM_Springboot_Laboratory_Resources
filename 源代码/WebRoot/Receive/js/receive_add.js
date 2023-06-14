$(function () {
	$("#receive_resourceObj_resourceNo").combobox({
	    url:'ResourceInfo/listAll',
	    valueField: "resourceNo",
	    textField: "name",
	    panelHeight: "auto",
        editable: false, //不允许手动输入
        required : true,
        onLoadSuccess: function () { //数据加载完毕事件
            var data = $("#receive_resourceObj_resourceNo").combobox("getData"); 
            if (data.length > 0) {
                $("#receive_resourceObj_resourceNo").combobox("select", data[0].resourceNo);
            }
        }
	});
	$("#receive_userObj_user_name").combobox({
	    url:'UserInfo/listAll',
	    valueField: "user_name",
	    textField: "name",
	    panelHeight: "auto",
        editable: false, //不允许手动输入
        required : true,
        onLoadSuccess: function () { //数据加载完毕事件
            var data = $("#receive_userObj_user_name").combobox("getData"); 
            if (data.length > 0) {
                $("#receive_userObj_user_name").combobox("select", data[0].user_name);
            }
        }
	});
	$("#receive_receiveNum").validatebox({
		required : true,
		validType : "integer",
		missingMessage : '请输入领用数量',
		invalidMessage : '领用数量输入不对',
	});

	$("#receive_receiveTime").datetimebox({
	    required : true, 
	    showSeconds: true,
	    editable: false
	});

	//单击添加按钮
	$("#receiveAddButton").click(function () {
		//验证表单 
		if(!$("#receiveAddForm").form("validate")) {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
		} else {
			$("#receiveAddForm").form({
			    url:"Receive/add",
			    onSubmit: function(){
					if($("#receiveAddForm").form("validate"))  { 
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
                        $("#receiveAddForm").form("clear");
                    }else{
                        $.messager.alert("消息",obj.message);
                    }
			    }
			});
			//提交表单
			$("#receiveAddForm").submit();
		}
	});

	//单击清空按钮
	$("#receiveClearButton").click(function () { 
		$("#receiveAddForm").form("clear"); 
	});
});
