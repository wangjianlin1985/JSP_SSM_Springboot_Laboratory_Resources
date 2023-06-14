package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.ReceiveService;
import com.chengxusheji.po.Receive;
import com.chengxusheji.service.ResourceInfoService;
import com.chengxusheji.po.ResourceInfo;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//Receive管理控制层
@Controller
@RequestMapping("/Receive")
public class ReceiveController extends BaseController {

    /*业务层对象*/
    @Resource ReceiveService receiveService;

    @Resource ResourceInfoService resourceInfoService;
    @Resource UserInfoService userInfoService;
	@InitBinder("resourceObj")
	public void initBinderresourceObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("resourceObj.");
	}
	@InitBinder("userObj")
	public void initBinderuserObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userObj.");
	}
	@InitBinder("receive")
	public void initBinderReceive(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("receive.");
	}
	/*跳转到添加Receive视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Receive());
		/*查询所有的ResourceInfo信息*/
		List<ResourceInfo> resourceInfoList = resourceInfoService.queryAllResourceInfo();
		request.setAttribute("resourceInfoList", resourceInfoList);
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "Receive_add";
	}

	/*客户端ajax方式提交添加领用信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Receive receive, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		String resourceNo = receive.getResourceObj().getResourceNo();
		ResourceInfo resourceInfo = resourceInfoService.getResourceInfo(resourceNo);
		if(resourceInfo.getNumberLimit().equals("是") && resourceInfo.getResourceNum() < receive.getReceiveNum()) {
			message = "库存不足，失败！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		if(resourceInfo.getNumberLimit().equals("是")){
        	resourceInfo.setResourceNum(resourceInfo.getResourceNum() - receive.getReceiveNum());
        	resourceInfoService.updateResourceInfo(resourceInfo);
		}
		
        receiveService.addReceive(receive);
        message = "领用添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询领用信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("resourceObj") ResourceInfo resourceObj,@ModelAttribute("userObj") UserInfo userObj,String receiveTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (receiveTime == null) receiveTime = "";
		if(rows != 0)receiveService.setRows(rows);
		List<Receive> receiveList = receiveService.queryReceive(resourceObj, userObj, receiveTime, page);
	    /*计算总的页数和总的记录数*/
	    receiveService.queryTotalPageAndRecordNumber(resourceObj, userObj, receiveTime);
	    /*获取到总的页码数目*/
	    int totalPage = receiveService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = receiveService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Receive receive:receiveList) {
			JSONObject jsonReceive = receive.getJsonObject();
			jsonArray.put(jsonReceive);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询领用信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Receive> receiveList = receiveService.queryAllReceive();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Receive receive:receiveList) {
			JSONObject jsonReceive = new JSONObject();
			jsonReceive.accumulate("receiveId", receive.getReceiveId());
			jsonReceive.accumulate("receiveId", receive.getReceiveId());
			jsonArray.put(jsonReceive);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询领用信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("resourceObj") ResourceInfo resourceObj,@ModelAttribute("userObj") UserInfo userObj,String receiveTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (receiveTime == null) receiveTime = "";
		List<Receive> receiveList = receiveService.queryReceive(resourceObj, userObj, receiveTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    receiveService.queryTotalPageAndRecordNumber(resourceObj, userObj, receiveTime);
	    /*获取到总的页码数目*/
	    int totalPage = receiveService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = receiveService.getRecordNumber();
	    request.setAttribute("receiveList",  receiveList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("resourceObj", resourceObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("receiveTime", receiveTime);
	    List<ResourceInfo> resourceInfoList = resourceInfoService.queryAllResourceInfo();
	    request.setAttribute("resourceInfoList", resourceInfoList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "Receive/receive_frontquery_result"; 
	}
	
	
	/*用户前台按照查询条件分页查询自己的领用信息*/
	@RequestMapping(value = { "/userFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String userFrontlist(@ModelAttribute("resourceObj") ResourceInfo resourceObj,@ModelAttribute("userObj") UserInfo userObj,String receiveTime,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (receiveTime == null) receiveTime = "";
		userObj = new UserInfo();
		userObj.setUser_name((String)session.getAttribute("user_name"));
		List<Receive> receiveList = receiveService.queryReceive(resourceObj, userObj, receiveTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    receiveService.queryTotalPageAndRecordNumber(resourceObj, userObj, receiveTime);
	    /*获取到总的页码数目*/
	    int totalPage = receiveService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = receiveService.getRecordNumber();
	    request.setAttribute("receiveList",  receiveList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("resourceObj", resourceObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("receiveTime", receiveTime);
	    List<ResourceInfo> resourceInfoList = resourceInfoService.queryAllResourceInfo();
	    request.setAttribute("resourceInfoList", resourceInfoList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "Receive/receive_userFrontquery_result"; 
	}
	

     /*前台查询Receive信息*/
	@RequestMapping(value="/{receiveId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer receiveId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键receiveId获取Receive对象*/
        Receive receive = receiveService.getReceive(receiveId);

        List<ResourceInfo> resourceInfoList = resourceInfoService.queryAllResourceInfo();
        request.setAttribute("resourceInfoList", resourceInfoList);
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("receive",  receive);
        return "Receive/receive_frontshow";
	}

	/*ajax方式显示领用修改jsp视图页*/
	@RequestMapping(value="/{receiveId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer receiveId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键receiveId获取Receive对象*/
        Receive receive = receiveService.getReceive(receiveId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonReceive = receive.getJsonObject();
		out.println(jsonReceive.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新领用信息*/
	@RequestMapping(value = "/{receiveId}/update", method = RequestMethod.POST)
	public void update(@Validated Receive receive, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			receiveService.updateReceive(receive);
			message = "领用更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "领用更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除领用信息*/
	@RequestMapping(value="/{receiveId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer receiveId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  receiveService.deleteReceive(receiveId);
	            request.setAttribute("message", "领用删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "领用删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条领用记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String receiveIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = receiveService.deleteReceives(receiveIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出领用信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("resourceObj") ResourceInfo resourceObj,@ModelAttribute("userObj") UserInfo userObj,String receiveTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(receiveTime == null) receiveTime = "";
        List<Receive> receiveList = receiveService.queryReceive(resourceObj,userObj,receiveTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Receive信息记录"; 
        String[] headers = { "领用的资源","领用用户","领用数量","领用时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<receiveList.size();i++) {
        	Receive receive = receiveList.get(i); 
        	dataset.add(new String[]{receive.getResourceObj().getName(),receive.getUserObj().getName(),receive.getReceiveNum() + "",receive.getReceiveTime()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Receive.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
