<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/borrow.css" /> 

<div id="borrow_manage"></div>
<div id="borrow_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="borrow_manage_tool.edit();">归还</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="borrow_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="borrow_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="borrow_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="borrow_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="borrowQueryForm" method="post">
			借阅的资源：<input class="textbox" type="text" id="resourceObj_resourceNo_query" name="resourceObj.resourceNo" style="width: auto"/>
			借阅用户：<input class="textbox" type="text" id="userObj_user_name_query" name="userObj.user_name" style="width: auto"/>
			借阅时间：<input type="text" id="borrowTime" name="borrowTime" class="easyui-datebox" editable="false" style="width:100px">
			归还时间：<input type="text" id="returnTime" name="returnTime" class="easyui-datebox" editable="false" style="width:100px">
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="borrow_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="borrowEditDiv">
	<form id="borrowEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">借阅id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="borrow_borrowId_edit" name="borrow.borrowId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">借阅的资源:</span>
			<span class="inputControl">
				<input class="textbox"  id="borrow_resourceObj_resourceNo_edit" name="borrow.resourceObj.resourceNo" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">借阅用户:</span>
			<span class="inputControl">
				<input class="textbox"  id="borrow_userObj_user_name_edit" name="borrow.userObj.user_name" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">借阅数量:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="borrow_borrowNum_edit" name="borrow.borrowNum" style="width:80px" />

			</span>

		</div>
		<div>
			<span class="label">借阅时间:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="borrow_borrowTime_edit" name="borrow.borrowTime" />

			</span>

		</div>
		<div>
			<span class="label">归还时间:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="borrow_returnTime_edit" name="borrow.returnTime" />

			</span>

		</div>
		<div>
			<span class="label">附加信息:</span>
			<span class="inputControl">
				<textarea id="borrow_memo_edit" name="borrow.memo" rows="8" cols="60"></textarea>

			</span>

		</div>
	</form>
</div>
<script type="text/javascript" src="Borrow/js/borrow_manage.js"></script> 
