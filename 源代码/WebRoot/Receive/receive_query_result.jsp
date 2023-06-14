<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/receive.css" /> 

<div id="receive_manage"></div>
<div id="receive_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="receive_manage_tool.edit();">修改</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="receive_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="receive_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="receive_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="receive_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="receiveQueryForm" method="post">
			领用的资源：<input class="textbox" type="text" id="resourceObj_resourceNo_query" name="resourceObj.resourceNo" style="width: auto"/>
			领用用户：<input class="textbox" type="text" id="userObj_user_name_query" name="userObj.user_name" style="width: auto"/>
			领用时间：<input type="text" id="receiveTime" name="receiveTime" class="easyui-datebox" editable="false" style="width:100px">
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="receive_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="receiveEditDiv">
	<form id="receiveEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">领用id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="receive_receiveId_edit" name="receive.receiveId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">领用的资源:</span>
			<span class="inputControl">
				<input class="textbox"  id="receive_resourceObj_resourceNo_edit" name="receive.resourceObj.resourceNo" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">领用用户:</span>
			<span class="inputControl">
				<input class="textbox"  id="receive_userObj_user_name_edit" name="receive.userObj.user_name" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">领用数量:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="receive_receiveNum_edit" name="receive.receiveNum" style="width:80px" />

			</span>

		</div>
		<div>
			<span class="label">领用时间:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="receive_receiveTime_edit" name="receive.receiveTime" />

			</span>

		</div>
		<div>
			<span class="label">领用用途:</span>
			<span class="inputControl">
				<textarea id="receive_purpose_edit" name="receive.purpose" rows="8" cols="60"></textarea>

			</span>

		</div>
	</form>
</div>
<script type="text/javascript" src="Receive/js/receive_manage.js"></script> 
