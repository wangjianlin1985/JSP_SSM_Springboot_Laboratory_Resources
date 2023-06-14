package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.ResourceType;

public interface ResourceTypeMapper {
	/*添加资源类型信息*/
	public void addResourceType(ResourceType resourceType) throws Exception;

	/*按照查询条件分页查询资源类型记录*/
	public ArrayList<ResourceType> queryResourceType(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有资源类型记录*/
	public ArrayList<ResourceType> queryResourceTypeList(@Param("where") String where) throws Exception;

	/*按照查询条件的资源类型记录数*/
	public int queryResourceTypeCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条资源类型记录*/
	public ResourceType getResourceType(int typeId) throws Exception;

	/*更新资源类型记录*/
	public void updateResourceType(ResourceType resourceType) throws Exception;

	/*删除资源类型记录*/
	public void deleteResourceType(int typeId) throws Exception;

}
