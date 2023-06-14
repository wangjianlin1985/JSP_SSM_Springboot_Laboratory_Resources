package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ResourceType;

import com.chengxusheji.mapper.ResourceTypeMapper;
@Service
public class ResourceTypeService {

	@Resource ResourceTypeMapper resourceTypeMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加资源类型记录*/
    public void addResourceType(ResourceType resourceType) throws Exception {
    	resourceTypeMapper.addResourceType(resourceType);
    }

    /*按照查询条件分页查询资源类型记录*/
    public ArrayList<ResourceType> queryResourceType(String typeName,String addDate,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!typeName.equals("")) where = where + " and t_resourceType.typeName like '%" + typeName + "%'";
    	if(!addDate.equals("")) where = where + " and t_resourceType.addDate like '%" + addDate + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return resourceTypeMapper.queryResourceType(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<ResourceType> queryResourceType(String typeName,String addDate) throws Exception  { 
     	String where = "where 1=1";
    	if(!typeName.equals("")) where = where + " and t_resourceType.typeName like '%" + typeName + "%'";
    	if(!addDate.equals("")) where = where + " and t_resourceType.addDate like '%" + addDate + "%'";
    	return resourceTypeMapper.queryResourceTypeList(where);
    }

    /*查询所有资源类型记录*/
    public ArrayList<ResourceType> queryAllResourceType()  throws Exception {
        return resourceTypeMapper.queryResourceTypeList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String typeName,String addDate) throws Exception {
     	String where = "where 1=1";
    	if(!typeName.equals("")) where = where + " and t_resourceType.typeName like '%" + typeName + "%'";
    	if(!addDate.equals("")) where = where + " and t_resourceType.addDate like '%" + addDate + "%'";
        recordNumber = resourceTypeMapper.queryResourceTypeCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取资源类型记录*/
    public ResourceType getResourceType(int typeId) throws Exception  {
        ResourceType resourceType = resourceTypeMapper.getResourceType(typeId);
        return resourceType;
    }

    /*更新资源类型记录*/
    public void updateResourceType(ResourceType resourceType) throws Exception {
        resourceTypeMapper.updateResourceType(resourceType);
    }

    /*删除一条资源类型记录*/
    public void deleteResourceType (int typeId) throws Exception {
        resourceTypeMapper.deleteResourceType(typeId);
    }

    /*删除多条资源类型信息*/
    public int deleteResourceTypes (String typeIds) throws Exception {
    	String _typeIds[] = typeIds.split(",");
    	for(String _typeId: _typeIds) {
    		resourceTypeMapper.deleteResourceType(Integer.parseInt(_typeId));
    	}
    	return _typeIds.length;
    }
}
