<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.ResourceInfo" %>
<%@ page import="com.chengxusheji.po.ResourceType" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<ResourceInfo> resourceInfoList = (List<ResourceInfo>)request.getAttribute("resourceInfoList");
    //获取所有的resourceTypeObj信息
    List<ResourceType> resourceTypeList = (List<ResourceType>)request.getAttribute("resourceTypeList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    String resourceNo = (String)request.getAttribute("resourceNo"); //资源编号查询关键字
    ResourceType resourceTypeObj = (ResourceType)request.getAttribute("resourceTypeObj");
    String name = (String)request.getAttribute("name"); //资源名称查询关键字
    String addDate = (String)request.getAttribute("addDate"); //加入日期查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>资源查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
<body style="margin-top:70px;"> 
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="col-md-9 wow fadeInLeft">
		<ul class="breadcrumb">
  			<li><a href="<%=basePath %>index.jsp">首页</a></li>
  			<li><a href="<%=basePath %>ResourceInfo/frontlist">资源信息列表</a></li>
  			<li class="active">查询结果显示</li>
  			<a class="pull-right" href="<%=basePath %>ResourceInfo/resourceInfo_frontAdd.jsp" style="display:none;">添加资源</a>
		</ul>
		<div class="row">
			<%
				/*计算起始序号*/
				int startIndex = (currentPage -1) * 5;
				/*遍历记录*/
				for(int i=0;i<resourceInfoList.size();i++) {
            		int currentIndex = startIndex + i + 1; //当前记录的序号
            		ResourceInfo resourceInfo = resourceInfoList.get(i); //获取到资源对象
            		String clearLeft = "";
            		if(i%4 == 0) clearLeft = "style=\"clear:left;\"";
			%>
			<div class="col-md-3 bottom15" <%=clearLeft %>>
			  <a  href="<%=basePath  %>ResourceInfo/<%=resourceInfo.getResourceNo() %>/frontshow"><img class="img-responsive" src="<%=basePath%><%=resourceInfo.getResourcePhoto()%>" /></a>
			     <div class="showFields">
			     	<div class="field">
	            		资源编号:<%=resourceInfo.getResourceNo() %>
			     	</div>
			     	<div class="field">
	            		资源类型:<%=resourceInfo.getResourceTypeObj().getTypeName() %>
			     	</div>
			     	<div class="field">
	            		资源名称:<%=resourceInfo.getName() %>
			     	</div>
			     	<div class="field">
	            		数量限制:<%=resourceInfo.getNumberLimit() %>
			     	</div>
			     	<div class="field">
	            		资源库存:<%=resourceInfo.getResourceNum() %>
			     	</div>
			     	<div class="field">
	            		加入日期:<%=resourceInfo.getAddDate() %>
			     	</div>
			        <a class="btn btn-primary top5" href="<%=basePath %>ResourceInfo/<%=resourceInfo.getResourceNo() %>/frontshow">详情</a>
			        <a class="btn btn-primary top5" onclick="resourceInfoEdit('<%=resourceInfo.getResourceNo() %>');" style="display:none;">修改</a>
			        <a class="btn btn-primary top5" onclick="resourceInfoDelete('<%=resourceInfo.getResourceNo() %>');" style="display:none;">删除</a>
			     </div>
			</div>
			<%  } %>

			<div class="row">
				<div class="col-md-12">
					<nav class="pull-left">
						<ul class="pagination">
							<li><a href="#" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
							<%
								int startPage = currentPage - 5;
								int endPage = currentPage + 5;
								if(startPage < 1) startPage=1;
								if(endPage > totalPage) endPage = totalPage;
								for(int i=startPage;i<=endPage;i++) {
							%>
							<li class="<%= currentPage==i?"active":"" %>"><a href="#"  onclick="GoToPage(<%=i %>,<%=totalPage %>);"><%=i %></a></li>
							<%  } %> 
							<li><a href="#" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);"><span aria-hidden="true">&raquo;</span></a></li>
						</ul>
					</nav>
					<div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>资源查询</h1>
		</div>
		<form name="resourceInfoQueryForm" id="resourceInfoQueryForm" action="<%=basePath %>ResourceInfo/frontlist" class="mar_t15">
			<div class="form-group">
				<label for="resourceNo">资源编号:</label>
				<input type="text" id="resourceNo" name="resourceNo" value="<%=resourceNo %>" class="form-control" placeholder="请输入资源编号">
			</div>
            <div class="form-group">
            	<label for="resourceTypeObj_typeId">资源类型：</label>
                <select id="resourceTypeObj_typeId" name="resourceTypeObj.typeId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(ResourceType resourceTypeTemp:resourceTypeList) {
	 					String selected = "";
 					if(resourceTypeObj!=null && resourceTypeObj.getTypeId()!=null && resourceTypeObj.getTypeId().intValue()==resourceTypeTemp.getTypeId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=resourceTypeTemp.getTypeId() %>" <%=selected %>><%=resourceTypeTemp.getTypeName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="name">资源名称:</label>
				<input type="text" id="name" name="name" value="<%=name %>" class="form-control" placeholder="请输入资源名称">
			</div>
			<div class="form-group">
				<label for="addDate">加入日期:</label>
				<input type="text" id="addDate" name="addDate" class="form-control"  placeholder="请选择加入日期" value="<%=addDate %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
</div>
<div id="resourceInfoEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;资源信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="resourceInfoEditForm" id="resourceInfoEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="resourceInfo_resourceNo_edit" class="col-md-3 text-right">资源编号:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="resourceInfo_resourceNo_edit" name="resourceInfo.resourceNo" class="form-control" placeholder="请输入资源编号" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="resourceInfo_resourceTypeObj_typeId_edit" class="col-md-3 text-right">资源类型:</label>
		  	 <div class="col-md-9">
			    <select id="resourceInfo_resourceTypeObj_typeId_edit" name="resourceInfo.resourceTypeObj.typeId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="resourceInfo_name_edit" class="col-md-3 text-right">资源名称:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="resourceInfo_name_edit" name="resourceInfo.name" class="form-control" placeholder="请输入资源名称">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="resourceInfo_resourcePhoto_edit" class="col-md-3 text-right">资源图片:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="resourceInfo_resourcePhotoImg" border="0px"/><br/>
			    <input type="hidden" id="resourceInfo_resourcePhoto" name="resourceInfo.resourcePhoto"/>
			    <input id="resourcePhotoFile" name="resourcePhotoFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="resourceInfo_numberLimit_edit" class="col-md-3 text-right">数量限制:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="resourceInfo_numberLimit_edit" name="resourceInfo.numberLimit" class="form-control" placeholder="请输入数量限制">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="resourceInfo_resourceNum_edit" class="col-md-3 text-right">资源库存:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="resourceInfo_resourceNum_edit" name="resourceInfo.resourceNum" class="form-control" placeholder="请输入资源库存">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="resourceInfo_addDate_edit" class="col-md-3 text-right">加入日期:</label>
		  	 <div class="col-md-9">
                <div class="input-group date resourceInfo_addDate_edit col-md-12" data-link-field="resourceInfo_addDate_edit" data-link-format="yyyy-mm-dd">
                    <input class="form-control" id="resourceInfo_addDate_edit" name="resourceInfo.addDate" size="16" type="text" value="" placeholder="请选择加入日期" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="resourceInfo_resourceDesc_edit" class="col-md-3 text-right">资源描述:</label>
		  	 <div class="col-md-9">
			    <textarea id="resourceInfo_resourceDesc_edit" name="resourceInfo.resourceDesc" rows="8" class="form-control" placeholder="请输入资源描述"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#resourceInfoEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxResourceInfoModify();">提交</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script>
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.resourceInfoQueryForm.currentPage.value = currentPage;
    document.resourceInfoQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.resourceInfoQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.resourceInfoQueryForm.currentPage.value = pageValue;
    documentresourceInfoQueryForm.submit();
}

/*弹出修改资源界面并初始化数据*/
function resourceInfoEdit(resourceNo) {
	$.ajax({
		url :  basePath + "ResourceInfo/" + resourceNo + "/update",
		type : "get",
		dataType: "json",
		success : function (resourceInfo, response, status) {
			if (resourceInfo) {
				$("#resourceInfo_resourceNo_edit").val(resourceInfo.resourceNo);
				$.ajax({
					url: basePath + "ResourceType/listAll",
					type: "get",
					success: function(resourceTypes,response,status) { 
						$("#resourceInfo_resourceTypeObj_typeId_edit").empty();
						var html="";
		        		$(resourceTypes).each(function(i,resourceType){
		        			html += "<option value='" + resourceType.typeId + "'>" + resourceType.typeName + "</option>";
		        		});
		        		$("#resourceInfo_resourceTypeObj_typeId_edit").html(html);
		        		$("#resourceInfo_resourceTypeObj_typeId_edit").val(resourceInfo.resourceTypeObjPri);
					}
				});
				$("#resourceInfo_name_edit").val(resourceInfo.name);
				$("#resourceInfo_resourcePhoto").val(resourceInfo.resourcePhoto);
				$("#resourceInfo_resourcePhotoImg").attr("src", basePath +　resourceInfo.resourcePhoto);
				$("#resourceInfo_numberLimit_edit").val(resourceInfo.numberLimit);
				$("#resourceInfo_resourceNum_edit").val(resourceInfo.resourceNum);
				$("#resourceInfo_addDate_edit").val(resourceInfo.addDate);
				$("#resourceInfo_resourceDesc_edit").val(resourceInfo.resourceDesc);
				$('#resourceInfoEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除资源信息*/
function resourceInfoDelete(resourceNo) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "ResourceInfo/deletes",
			data : {
				resourceNos : resourceNo,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#resourceInfoQueryForm").submit();
					//location.href= basePath + "ResourceInfo/frontlist";
				}
				else 
					alert(data.message);
			},
		});
	}
}

/*ajax方式提交资源信息表单给服务器端修改*/
function ajaxResourceInfoModify() {
	$.ajax({
		url :  basePath + "ResourceInfo/" + $("#resourceInfo_resourceNo_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#resourceInfoEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#resourceInfoQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
	/*小屏幕导航点击关闭菜单*/
    $('.navbar-collapse a').click(function(){
        $('.navbar-collapse').collapse('hide');
    });
    new WOW().init();

    /*加入日期组件*/
    $('.resourceInfo_addDate_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd',
    	minView: 2,
    	weekStart: 1,
    	todayBtn:  1,
    	autoclose: 1,
    	minuteStep: 1,
    	todayHighlight: 1,
    	startView: 2,
    	forceParse: 0
    });
})
</script>
</body>
</html>

