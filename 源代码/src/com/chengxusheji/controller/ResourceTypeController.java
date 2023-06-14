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
import com.chengxusheji.service.ResourceTypeService;
import com.chengxusheji.po.ResourceType;

//ResourceType管理控制层
@Controller
@RequestMapping("/ResourceType")
public class ResourceTypeController extends BaseController {

    /*业务层对象*/
    @Resource ResourceTypeService resourceTypeService;

	@InitBinder("resourceType")
	public void initBinderResourceType(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("resourceType.");
	}
	/*跳转到添加ResourceType视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new ResourceType());
		return "ResourceType_add";
	}

	/*客户端ajax方式提交添加资源类型信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated ResourceType resourceType, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        resourceTypeService.addResourceType(resourceType);
        message = "资源类型添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询资源类型信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String typeName,String addDate,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (typeName == null) typeName = "";
		if (addDate == null) addDate = "";
		if(rows != 0)resourceTypeService.setRows(rows);
		List<ResourceType> resourceTypeList = resourceTypeService.queryResourceType(typeName, addDate, page);
	    /*计算总的页数和总的记录数*/
	    resourceTypeService.queryTotalPageAndRecordNumber(typeName, addDate);
	    /*获取到总的页码数目*/
	    int totalPage = resourceTypeService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = resourceTypeService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(ResourceType resourceType:resourceTypeList) {
			JSONObject jsonResourceType = resourceType.getJsonObject();
			jsonArray.put(jsonResourceType);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询资源类型信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<ResourceType> resourceTypeList = resourceTypeService.queryAllResourceType();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(ResourceType resourceType:resourceTypeList) {
			JSONObject jsonResourceType = new JSONObject();
			jsonResourceType.accumulate("typeId", resourceType.getTypeId());
			jsonResourceType.accumulate("typeName", resourceType.getTypeName());
			jsonArray.put(jsonResourceType);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询资源类型信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String typeName,String addDate,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (typeName == null) typeName = "";
		if (addDate == null) addDate = "";
		List<ResourceType> resourceTypeList = resourceTypeService.queryResourceType(typeName, addDate, currentPage);
	    /*计算总的页数和总的记录数*/
	    resourceTypeService.queryTotalPageAndRecordNumber(typeName, addDate);
	    /*获取到总的页码数目*/
	    int totalPage = resourceTypeService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = resourceTypeService.getRecordNumber();
	    request.setAttribute("resourceTypeList",  resourceTypeList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("typeName", typeName);
	    request.setAttribute("addDate", addDate);
		return "ResourceType/resourceType_frontquery_result"; 
	}

     /*前台查询ResourceType信息*/
	@RequestMapping(value="/{typeId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer typeId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键typeId获取ResourceType对象*/
        ResourceType resourceType = resourceTypeService.getResourceType(typeId);

        request.setAttribute("resourceType",  resourceType);
        return "ResourceType/resourceType_frontshow";
	}

	/*ajax方式显示资源类型修改jsp视图页*/
	@RequestMapping(value="/{typeId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer typeId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键typeId获取ResourceType对象*/
        ResourceType resourceType = resourceTypeService.getResourceType(typeId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonResourceType = resourceType.getJsonObject();
		out.println(jsonResourceType.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新资源类型信息*/
	@RequestMapping(value = "/{typeId}/update", method = RequestMethod.POST)
	public void update(@Validated ResourceType resourceType, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			resourceTypeService.updateResourceType(resourceType);
			message = "资源类型更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "资源类型更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除资源类型信息*/
	@RequestMapping(value="/{typeId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer typeId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  resourceTypeService.deleteResourceType(typeId);
	            request.setAttribute("message", "资源类型删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "资源类型删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条资源类型记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String typeIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = resourceTypeService.deleteResourceTypes(typeIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出资源类型信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String typeName,String addDate, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(typeName == null) typeName = "";
        if(addDate == null) addDate = "";
        List<ResourceType> resourceTypeList = resourceTypeService.queryResourceType(typeName,addDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "ResourceType信息记录"; 
        String[] headers = { "类型id","类型名称","加入日期"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<resourceTypeList.size();i++) {
        	ResourceType resourceType = resourceTypeList.get(i); 
        	dataset.add(new String[]{resourceType.getTypeId() + "",resourceType.getTypeName(),resourceType.getAddDate()});
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
			response.setHeader("Content-disposition","attachment; filename="+"ResourceType.xls");//filename是下载的xls的名，建议最好用英文 
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
