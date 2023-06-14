package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Borrow;

public interface BorrowMapper {
	/*添加借阅信息*/
	public void addBorrow(Borrow borrow) throws Exception;

	/*按照查询条件分页查询借阅记录*/
	public ArrayList<Borrow> queryBorrow(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有借阅记录*/
	public ArrayList<Borrow> queryBorrowList(@Param("where") String where) throws Exception;

	/*按照查询条件的借阅记录数*/
	public int queryBorrowCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条借阅记录*/
	public Borrow getBorrow(int borrowId) throws Exception;

	/*更新借阅记录*/
	public void updateBorrow(Borrow borrow) throws Exception;

	/*删除借阅记录*/
	public void deleteBorrow(int borrowId) throws Exception;

}
