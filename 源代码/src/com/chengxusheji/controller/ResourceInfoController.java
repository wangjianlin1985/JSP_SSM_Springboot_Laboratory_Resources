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
import com.chengxusheji.service.ResourceInfoService;
import com.chengxusheji.po.ResourceInfo;
import com.chengxusheji.service.ResourceTypeService;
import com.chengxusheji.po.ResourceType;

//ResourceInfo管理控制层
@Controller
@RequestMapping("/ResourceInfo")
public class ResourceInfoController extends BaseController {

    /*业务层对象*/
    @Resource ResourceInfoService resourceInfoService;

    @Resource ResourceTypeService resourceTypeService;
	@InitBinder("resourceTypeObj")
	public void initBinderresourceTypeObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("resourceTypeObj.");
	}
	@InitBinder("resourceInfo")
	public void initBinderResourceInfo(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("resourceInfo.");
	}
	/*跳转到添加ResourceInfo视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new ResourceInfo());
		/*查询所有的ResourceType信息*/
		List<ResourceType> resourceTypeList = resourceTypeService.queryAllResourceType();
		request.setAttribute("resourceTypeList", resourceTypeList);
		return "ResourceInfo_add";
	}

	/*客户端ajax方式提交添加资源信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated ResourceInfo resourceInfo, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		if(resourceInfoService.getResourceInfo(resourceInfo.getResourceNo()) != null) {
			message = "资源编号已经存在！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			resourceInfo.setResourcePhoto(this.handlePhotoUpload(request, "resourcePhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
        resourceInfoService.addResourceInfo(resourceInfo);
        message = "资源添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询资源信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String resourceNo,@ModelAttribute("resourceTypeObj") ResourceType resourceTypeObj,String name,String addDate,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (resourceNo == null) resourceNo = "";
		if (name == null) name = "";
		if (addDate == null) addDate = "";
		if(rows != 0)resourceInfoService.setRows(rows);
		List<ResourceInfo> resourceInfoList = resourceInfoService.queryResourceInfo(resourceNo, resourceTypeObj, name, addDate, page);
	    /*计算总的页数和总的记录数*/
	    resourceInfoService.queryTotalPageAndRecordNumber(resourceNo, resourceTypeObj, name, addDate);
	    /*获取到总的页码数目*/
	    int totalPage = resourceInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = resourceInfoService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(ResourceInfo resourceInfo:resourceInfoList) {
			JSONObject jsonResourceInfo = resourceInfo.getJsonObject();
			jsonArray.put(jsonResourceInfo);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询资源信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<ResourceInfo> resourceInfoList = resourceInfoService.queryAllResourceInfo();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(ResourceInfo resourceInfo:resourceInfoList) {
			JSONObject jsonResourceInfo = new JSONObject();
			jsonResourceInfo.accumulate("resourceNo", resourceInfo.getResourceNo());
			jsonResourceInfo.accumulate("name", resourceInfo.getName());
			jsonArray.put(jsonResourceInfo);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询资源信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String resourceNo,@ModelAttribute("resourceTypeObj") ResourceType resourceTypeObj,String name,String addDate,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (resourceNo == null) resourceNo = "";
		if (name == null) name = "";
		if (addDate == null) addDate = "";
		List<ResourceInfo> resourceInfoList = resourceInfoService.queryResourceInfo(resourceNo, resourceTypeObj, name, addDate, currentPage);
	    /*计算总的页数和总的记录数*/
	    resourceInfoService.queryTotalPageAndRecordNumber(resourceNo, resourceTypeObj, name, addDate);
	    /*获取到总的页码数目*/
	    int totalPage = resourceInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = resourceInfoService.getRecordNumber();
	    request.setAttribute("resourceInfoList",  resourceInfoList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("resourceNo", resourceNo);
	    request.setAttribute("resourceTypeObj", resourceTypeObj);
	    request.setAttribute("name", name);
	    request.setAttribute("addDate", addDate);
	    List<ResourceType> resourceTypeList = resourceTypeService.queryAllResourceType();
	    request.setAttribute("resourceTypeList", resourceTypeList);
		return "ResourceInfo/resourceInfo_frontquery_result"; 
	}

     /*前台查询ResourceInfo信息*/
	@RequestMapping(value="/{resourceNo}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable String resourceNo,Model model,HttpServletRequest request) throws Exception {
		/*根据主键resourceNo获取ResourceInfo对象*/
        ResourceInfo resourceInfo = resourceInfoService.getResourceInfo(resourceNo);

        List<ResourceType> resourceTypeList = resourceTypeService.queryAllResourceType();
        request.setAttribute("resourceTypeList", resourceTypeList);
        request.setAttribute("resourceInfo",  resourceInfo);
        return "ResourceInfo/resourceInfo_frontshow";
	}

	/*ajax方式显示资源修改jsp视图页*/
	@RequestMapping(value="/{resourceNo}/update",method=RequestMethod.GET)
	public void update(@PathVariable String resourceNo,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键resourceNo获取ResourceInfo对象*/
        ResourceInfo resourceInfo = resourceInfoService.getResourceInfo(resourceNo);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonResourceInfo = resourceInfo.getJsonObject();
		out.println(jsonResourceInfo.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新资源信息*/
	@RequestMapping(value = "/{resourceNo}/update", method = RequestMethod.POST)
	public void update(@Validated ResourceInfo resourceInfo, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String resourcePhotoFileName = this.handlePhotoUpload(request, "resourcePhotoFile");
		if(!resourcePhotoFileName.equals("upload/NoImage.jpg"))resourceInfo.setResourcePhoto(resourcePhotoFileName); 


		try {
			resourceInfoService.updateResourceInfo(resourceInfo);
			message = "资源更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "资源更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除资源信息*/
	@RequestMapping(value="/{resourceNo}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable String resourceNo,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  resourceInfoService.deleteResourceInfo(resourceNo);
	            request.setAttribute("message", "资源删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "资源删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条资源记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String resourceNos,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = resourceInfoService.deleteResourceInfos(resourceNos);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出资源信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String resourceNo,@ModelAttribute("resourceTypeObj") ResourceType resourceTypeObj,String name,String addDate, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(resourceNo == null) resourceNo = "";
        if(name == null) name = "";
        if(addDate == null) addDate = "";
        List<ResourceInfo> resourceInfoList = resourceInfoService.queryResourceInfo(resourceNo,resourceTypeObj,name,addDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "ResourceInfo信息记录"; 
        String[] headers = { "资源编号","资源类型","资源名称","资源图片","数量限制","资源库存","加入日期"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<resourceInfoList.size();i++) {
        	ResourceInfo resourceInfo = resourceInfoList.get(i); 
        	dataset.add(new String[]{resourceInfo.getResourceNo(),resourceInfo.getResourceTypeObj().getTypeName(),resourceInfo.getName(),resourceInfo.getResourcePhoto(),resourceInfo.getNumberLimit(),resourceInfo.getResourceNum() + "",resourceInfo.getAddDate()});
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
			response.setHeader("Content-disposition","attachment; filename="+"ResourceInfo.xls");//filename是下载的xls的名，建议最好用英文 
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
