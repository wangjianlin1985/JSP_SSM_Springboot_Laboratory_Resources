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
import com.chengxusheji.service.BorrowService;
import com.chengxusheji.po.Borrow;
import com.chengxusheji.service.ResourceInfoService;
import com.chengxusheji.po.ResourceInfo;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//Borrow管理控制层
@Controller
@RequestMapping("/Borrow")
public class BorrowController extends BaseController {

    /*业务层对象*/
    @Resource BorrowService borrowService;

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
	@InitBinder("borrow")
	public void initBinderBorrow(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("borrow.");
	}
	/*跳转到添加Borrow视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Borrow());
		/*查询所有的ResourceInfo信息*/
		List<ResourceInfo> resourceInfoList = resourceInfoService.queryAllResourceInfo();
		request.setAttribute("resourceInfoList", resourceInfoList);
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "Borrow_add";
	}

	/*客户端ajax方式提交添加借阅信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Borrow borrow, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		String resourceNo = borrow.getResourceObj().getResourceNo();
		ResourceInfo resourceInfo = resourceInfoService.getResourceInfo(resourceNo);
		if(resourceInfo.getNumberLimit().equals("是") && resourceInfo.getResourceNum() < borrow.getBorrowNum()) {
			message = "库存不足，失败！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		borrow.setReturnTime("--");
        borrowService.addBorrow(borrow);
        
        if(resourceInfo.getNumberLimit().equals("是")){
        	resourceInfo.setResourceNum(resourceInfo.getResourceNum() - borrow.getBorrowNum());
        	resourceInfoService.updateResourceInfo(resourceInfo);
        }
        
        message = "借阅添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询借阅信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("resourceObj") ResourceInfo resourceObj,@ModelAttribute("userObj") UserInfo userObj,String borrowTime,String returnTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (borrowTime == null) borrowTime = "";
		if (returnTime == null) returnTime = "";
		if(rows != 0)borrowService.setRows(rows);
		List<Borrow> borrowList = borrowService.queryBorrow(resourceObj, userObj, borrowTime, returnTime, page);
	    /*计算总的页数和总的记录数*/
	    borrowService.queryTotalPageAndRecordNumber(resourceObj, userObj, borrowTime, returnTime);
	    /*获取到总的页码数目*/
	    int totalPage = borrowService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = borrowService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Borrow borrow:borrowList) {
			JSONObject jsonBorrow = borrow.getJsonObject();
			jsonArray.put(jsonBorrow);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询借阅信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Borrow> borrowList = borrowService.queryAllBorrow();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Borrow borrow:borrowList) {
			JSONObject jsonBorrow = new JSONObject();
			jsonBorrow.accumulate("borrowId", borrow.getBorrowId());
			jsonBorrow.accumulate("borrowId", borrow.getBorrowId());
			jsonArray.put(jsonBorrow);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询借阅信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("resourceObj") ResourceInfo resourceObj,@ModelAttribute("userObj") UserInfo userObj,String borrowTime,String returnTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (borrowTime == null) borrowTime = "";
		if (returnTime == null) returnTime = "";
		List<Borrow> borrowList = borrowService.queryBorrow(resourceObj, userObj, borrowTime, returnTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    borrowService.queryTotalPageAndRecordNumber(resourceObj, userObj, borrowTime, returnTime);
	    /*获取到总的页码数目*/
	    int totalPage = borrowService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = borrowService.getRecordNumber();
	    request.setAttribute("borrowList",  borrowList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("resourceObj", resourceObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("borrowTime", borrowTime);
	    request.setAttribute("returnTime", returnTime);
	    List<ResourceInfo> resourceInfoList = resourceInfoService.queryAllResourceInfo();
	    request.setAttribute("resourceInfoList", resourceInfoList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "Borrow/borrow_frontquery_result"; 
	}
	
	
	
	/*用户前台按照查询条件分页查询自己的借阅信息*/
	@RequestMapping(value = { "/userFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String useFrontlist(@ModelAttribute("resourceObj") ResourceInfo resourceObj,@ModelAttribute("userObj") UserInfo userObj,String borrowTime,String returnTime,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (borrowTime == null) borrowTime = "";
		if (returnTime == null) returnTime = "";
		userObj = new UserInfo();
		userObj.setUser_name((String)session.getAttribute("user_name"));
		List<Borrow> borrowList = borrowService.queryBorrow(resourceObj, userObj, borrowTime, returnTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    borrowService.queryTotalPageAndRecordNumber(resourceObj, userObj, borrowTime, returnTime);
	    /*获取到总的页码数目*/
	    int totalPage = borrowService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = borrowService.getRecordNumber();
	    request.setAttribute("borrowList",  borrowList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("resourceObj", resourceObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("borrowTime", borrowTime);
	    request.setAttribute("returnTime", returnTime);
	    List<ResourceInfo> resourceInfoList = resourceInfoService.queryAllResourceInfo();
	    request.setAttribute("resourceInfoList", resourceInfoList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "Borrow/borrow_userFrontquery_result"; 
	}
	
	
	

     /*前台查询Borrow信息*/
	@RequestMapping(value="/{borrowId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer borrowId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键borrowId获取Borrow对象*/
        Borrow borrow = borrowService.getBorrow(borrowId);

        List<ResourceInfo> resourceInfoList = resourceInfoService.queryAllResourceInfo();
        request.setAttribute("resourceInfoList", resourceInfoList);
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("borrow",  borrow);
        return "Borrow/borrow_frontshow";
	}

	/*ajax方式显示借阅修改jsp视图页*/
	@RequestMapping(value="/{borrowId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer borrowId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键borrowId获取Borrow对象*/
        Borrow borrow = borrowService.getBorrow(borrowId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonBorrow = borrow.getJsonObject();
		out.println(jsonBorrow.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新借阅信息*/
	@RequestMapping(value = "/{borrowId}/update", method = RequestMethod.POST)
	public void update(@Validated Borrow borrow, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			String returnTime = borrow.getReturnTime();
			Borrow oldBorrow = borrowService.getBorrow(borrow.getBorrowId());
			ResourceInfo resourceObj = oldBorrow.getResourceObj();
			 
			if(oldBorrow.getReturnTime().equals("--") && resourceObj.getNumberLimit().equals("是")) {
				resourceObj.setResourceNum(resourceObj.getResourceNum() + borrow.getBorrowNum());
				resourceInfoService.updateResourceInfo(resourceObj);
			}
			 
			borrowService.updateBorrow(borrow);
			message = "归还成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "借阅更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除借阅信息*/
	@RequestMapping(value="/{borrowId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer borrowId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  borrowService.deleteBorrow(borrowId);
	            request.setAttribute("message", "借阅删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "借阅删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条借阅记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String borrowIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = borrowService.deleteBorrows(borrowIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出借阅信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("resourceObj") ResourceInfo resourceObj,@ModelAttribute("userObj") UserInfo userObj,String borrowTime,String returnTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(borrowTime == null) borrowTime = "";
        if(returnTime == null) returnTime = "";
        List<Borrow> borrowList = borrowService.queryBorrow(resourceObj,userObj,borrowTime,returnTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Borrow信息记录"; 
        String[] headers = { "借阅的资源","借阅用户","借阅数量","借阅时间","归还时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<borrowList.size();i++) {
        	Borrow borrow = borrowList.get(i); 
        	dataset.add(new String[]{borrow.getResourceObj().getName(),borrow.getUserObj().getName(),borrow.getBorrowNum() + "",borrow.getBorrowTime(),borrow.getReturnTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Borrow.xls");//filename是下载的xls的名，建议最好用英文 
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
