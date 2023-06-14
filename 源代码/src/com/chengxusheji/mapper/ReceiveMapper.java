package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Receive;

public interface ReceiveMapper {
	/*添加领用信息*/
	public void addReceive(Receive receive) throws Exception;

	/*按照查询条件分页查询领用记录*/
	public ArrayList<Receive> queryReceive(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有领用记录*/
	public ArrayList<Receive> queryReceiveList(@Param("where") String where) throws Exception;

	/*按照查询条件的领用记录数*/
	public int queryReceiveCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条领用记录*/
	public Receive getReceive(int receiveId) throws Exception;

	/*更新领用记录*/
	public void updateReceive(Receive receive) throws Exception;

	/*删除领用记录*/
	public void deleteReceive(int receiveId) throws Exception;

}
