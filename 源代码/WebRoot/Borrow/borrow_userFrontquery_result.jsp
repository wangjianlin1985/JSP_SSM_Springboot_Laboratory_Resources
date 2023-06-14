<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Borrow" %>
<%@ page import="com.chengxusheji.po.ResourceInfo" %>
<%@ page import="com.chengxusheji.po.UserInfo" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Borrow> borrowList = (List<Borrow>)request.getAttribute("borrowList");
    //获取所有的resourceObj信息
    List<ResourceInfo> resourceInfoList = (List<ResourceInfo>)request.getAttribute("resourceInfoList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    ResourceInfo resourceObj = (ResourceInfo)request.getAttribute("resourceObj");
    UserInfo userObj = (UserInfo)request.getAttribute("userObj");
    String borrowTime = (String)request.getAttribute("borrowTime"); //借阅时间查询关键字
    String returnTime = (String)request.getAttribute("returnTime"); //归还时间查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>借阅查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="row"> 
		<div class="col-md-9 wow fadeInDown" data-wow-duration="0.5s">
			<div>
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
			    	<li><a href="<%=basePath %>index.jsp">首页</a></li>
			    	<li role="presentation" class="active"><a href="#borrowListPanel" aria-controls="borrowListPanel" role="tab" data-toggle="tab">我的借阅列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>Borrow/borrow_frontAdd.jsp" style="display:none;">添加借阅</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="borrowListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>借阅的资源</td><td>借阅数量</td><td>借阅时间</td><td>归还时间</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<borrowList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		Borrow borrow = borrowList.get(i); //获取到借阅对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><a href="<%=basePath  %>ResourceInfo/<%=borrow.getResourceObj().getResourceNo() %>/frontshow"><%=borrow.getResourceObj().getName() %></a></td> 
 											<td><%=borrow.getBorrowNum() %></td>
 											<td><%=borrow.getBorrowTime() %></td>
 											<td><%=borrow.getReturnTime() %></td>
 											<td>
 												<a href="<%=basePath  %>Borrow/<%=borrow.getBorrowId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="borrowEdit('<%=borrow.getBorrowId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="borrowDelete('<%=borrow.getBorrowId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
 											</td> 
 										</tr>
 										<%}%>
				    				</table>
				    				</div>
				    			</div>
				    		</div>

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
			</div>
		</div>
	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>借阅查询</h1>
		</div>
		<form name="borrowQueryForm" id="borrowQueryForm" action="<%=basePath %>Borrow/userFrontlist" class="mar_t15">
            <div class="form-group">
            	<label for="resourceObj_resourceNo">借阅的资源：</label>
                <select id="resourceObj_resourceNo" name="resourceObj.resourceNo" class="form-control">
                	<option value="">不限制</option>
	 				<%
	 				for(ResourceInfo resourceInfoTemp:resourceInfoList) {
	 					String selected = "";
 					if(resourceObj!=null && resourceObj.getResourceNo()!=null && resourceObj.getResourceNo().equals(resourceInfoTemp.getResourceNo()))
 						selected = "selected";
	 				%>
 				 <option value="<%=resourceInfoTemp.getResourceNo() %>" <%=selected %>><%=resourceInfoTemp.getName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
            <div class="form-group" style="display:none;">
            	<label for="userObj_user_name">借阅用户：</label>
                <select id="userObj_user_name" name="userObj.user_name" class="form-control">
                	<option value="">不限制</option>
	 				<%
	 				for(UserInfo userInfoTemp:userInfoList) {
	 					String selected = "";
 					if(userObj!=null && userObj.getUser_name()!=null && userObj.getUser_name().equals(userInfoTemp.getUser_name()))
 						selected = "selected";
	 				%>
 				 <option value="<%=userInfoTemp.getUser_name() %>" <%=selected %>><%=userInfoTemp.getName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="borrowTime">借阅时间:</label>
				<input type="text" id="borrowTime" name="borrowTime" class="form-control"  placeholder="请选择借阅时间" value="<%=borrowTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
			<div class="form-group">
				<label for="returnTime">归还时间:</label>
				<input type="text" id="returnTime" name="returnTime" class="form-control"  placeholder="请选择归还时间" value="<%=returnTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="borrowEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;借阅信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="borrowEditForm" id="borrowEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="borrow_borrowId_edit" class="col-md-3 text-right">借阅id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="borrow_borrowId_edit" name="borrow.borrowId" class="form-control" placeholder="请输入借阅id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="borrow_resourceObj_resourceNo_edit" class="col-md-3 text-right">借阅的资源:</label>
		  	 <div class="col-md-9">
			    <select id="borrow_resourceObj_resourceNo_edit" name="borrow.resourceObj.resourceNo" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="borrow_userObj_user_name_edit" class="col-md-3 text-right">借阅用户:</label>
		  	 <div class="col-md-9">
			    <select id="borrow_userObj_user_name_edit" name="borrow.userObj.user_name" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="borrow_borrowNum_edit" class="col-md-3 text-right">借阅数量:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="borrow_borrowNum_edit" name="borrow.borrowNum" class="form-control" placeholder="请输入借阅数量">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="borrow_borrowTime_edit" class="col-md-3 text-right">借阅时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date borrow_borrowTime_edit col-md-12" data-link-field="borrow_borrowTime_edit">
                    <input class="form-control" id="borrow_borrowTime_edit" name="borrow.borrowTime" size="16" type="text" value="" placeholder="请选择借阅时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="borrow_returnTime_edit" class="col-md-3 text-right">归还时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date borrow_returnTime_edit col-md-12" data-link-field="borrow_returnTime_edit">
                    <input class="form-control" id="borrow_returnTime_edit" name="borrow.returnTime" size="16" type="text" value="" placeholder="请选择归还时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="borrow_memo_edit" class="col-md-3 text-right">附加信息:</label>
		  	 <div class="col-md-9">
			    <textarea id="borrow_memo_edit" name="borrow.memo" rows="8" class="form-control" placeholder="请输入附加信息"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#borrowEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxBorrowModify();">提交</button>
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
    document.borrowQueryForm.currentPage.value = currentPage;
    document.borrowQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.borrowQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.borrowQueryForm.currentPage.value = pageValue;
    documentborrowQueryForm.submit();
}

/*弹出修改借阅界面并初始化数据*/
function borrowEdit(borrowId) {
	$.ajax({
		url :  basePath + "Borrow/" + borrowId + "/update",
		type : "get",
		dataType: "json",
		success : function (borrow, response, status) {
			if (borrow) {
				$("#borrow_borrowId_edit").val(borrow.borrowId);
				$.ajax({
					url: basePath + "ResourceInfo/listAll",
					type: "get",
					success: function(resourceInfos,response,status) { 
						$("#borrow_resourceObj_resourceNo_edit").empty();
						var html="";
		        		$(resourceInfos).each(function(i,resourceInfo){
		        			html += "<option value='" + resourceInfo.resourceNo + "'>" + resourceInfo.name + "</option>";
		        		});
		        		$("#borrow_resourceObj_resourceNo_edit").html(html);
		        		$("#borrow_resourceObj_resourceNo_edit").val(borrow.resourceObjPri);
					}
				});
				$.ajax({
					url: basePath + "UserInfo/listAll",
					type: "get",
					success: function(userInfos,response,status) { 
						$("#borrow_userObj_user_name_edit").empty();
						var html="";
		        		$(userInfos).each(function(i,userInfo){
		        			html += "<option value='" + userInfo.user_name + "'>" + userInfo.name + "</option>";
		        		});
		        		$("#borrow_userObj_user_name_edit").html(html);
		        		$("#borrow_userObj_user_name_edit").val(borrow.userObjPri);
					}
				});
				$("#borrow_borrowNum_edit").val(borrow.borrowNum);
				$("#borrow_borrowTime_edit").val(borrow.borrowTime);
				$("#borrow_returnTime_edit").val(borrow.returnTime);
				$("#borrow_memo_edit").val(borrow.memo);
				$('#borrowEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除借阅信息*/
function borrowDelete(borrowId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Borrow/deletes",
			data : {
				borrowIds : borrowId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#borrowQueryForm").submit();
					//location.href= basePath + "Borrow/frontlist";
				}
				else 
					alert(data.message);
			},
		});
	}
}

/*ajax方式提交借阅信息表单给服务器端修改*/
function ajaxBorrowModify() {
	$.ajax({
		url :  basePath + "Borrow/" + $("#borrow_borrowId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#borrowEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#borrowQueryForm").submit();
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

    /*借阅时间组件*/
    $('.borrow_borrowTime_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd hh:ii:ss',
    	weekStart: 1,
    	todayBtn:  1,
    	autoclose: 1,
    	minuteStep: 1,
    	todayHighlight: 1,
    	startView: 2,
    	forceParse: 0
    });
    /*归还时间组件*/
    $('.borrow_returnTime_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd hh:ii:ss',
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

