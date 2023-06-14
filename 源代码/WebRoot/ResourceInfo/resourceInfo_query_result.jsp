<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/resourceInfo.css" /> 

<div id="resourceInfo_manage"></div>
<div id="resourceInfo_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="resourceInfo_manage_tool.edit();">修改</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="resourceInfo_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="resourceInfo_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="resourceInfo_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="resourceInfo_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="resourceInfoQueryForm" method="post">
			资源编号：<input type="text" class="textbox" id="resourceNo" name="resourceNo" style="width:110px" />
			资源类型：<input class="textbox" type="text" id="resourceTypeObj_typeId_query" name="resourceTypeObj.typeId" style="width: auto"/>
			资源名称：<input type="text" class="textbox" id="name" name="name" style="width:110px" />
			加入日期：<input type="text" id="addDate" name="addDate" class="easyui-datebox" editable="false" style="width:100px">
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="resourceInfo_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="resourceInfoEditDiv">
	<form id="resourceInfoEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">资源编号:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="resourceInfo_resourceNo_edit" name="resourceInfo.resourceNo" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">资源类型:</span>
			<span class="inputControl">
				<input class="textbox"  id="resourceInfo_resourceTypeObj_typeId_edit" name="resourceInfo.resourceTypeObj.typeId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">资源名称:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="resourceInfo_name_edit" name="resourceInfo.name" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">资源图片:</span>
			<span class="inputControl">
				<img id="resourceInfo_resourcePhotoImg" width="200px" border="0px"/><br/>
    			<input type="hidden" id="resourceInfo_resourcePhoto" name="resourceInfo.resourcePhoto"/>
				<input id="resourcePhotoFile" name="resourcePhotoFile" type="file" size="50" />
			</span>
		</div>
		<div>
			<span class="label">数量限制:</span>
			<span class="inputControl"> 
				<select  id="resourceInfo_numberLimit_edit" name="resourceInfo.numberLimit" >
					<option value="是">是</option>
					<option value="否">否</option>
				</select>
			</span>

		</div>
		<div>
			<span class="label">资源库存:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="resourceInfo_resourceNum_edit" name="resourceInfo.resourceNum" style="width:80px" />

			</span>

		</div>
		<div>
			<span class="label">加入日期:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="resourceInfo_addDate_edit" name="resourceInfo.addDate" />

			</span>

		</div>
		<div>
			<span class="label">资源描述:</span>
			<span class="inputControl">
				<textarea id="resourceInfo_resourceDesc_edit" name="resourceInfo.resourceDesc" rows="8" cols="60"></textarea>

			</span>

		</div>
	</form>
</div>
<script type="text/javascript" src="ResourceInfo/js/resourceInfo_manage.js"></script> 
