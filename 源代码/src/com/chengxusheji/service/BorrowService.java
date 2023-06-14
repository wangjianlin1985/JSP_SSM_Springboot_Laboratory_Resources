package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ResourceInfo;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.Borrow;

import com.chengxusheji.mapper.BorrowMapper;
@Service
public class BorrowService {

	@Resource BorrowMapper borrowMapper;
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

    /*添加借阅记录*/
    public void addBorrow(Borrow borrow) throws Exception {
    	borrowMapper.addBorrow(borrow);
    }

    /*按照查询条件分页查询借阅记录*/
    public ArrayList<Borrow> queryBorrow(ResourceInfo resourceObj,UserInfo userObj,String borrowTime,String returnTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != resourceObj &&  resourceObj.getResourceNo() != null  && !resourceObj.getResourceNo().equals(""))  where += " and t_borrow.resourceObj='" + resourceObj.getResourceNo() + "'";
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_borrow.userObj='" + userObj.getUser_name() + "'";
    	if(!borrowTime.equals("")) where = where + " and t_borrow.borrowTime like '%" + borrowTime + "%'";
    	if(!returnTime.equals("")) where = where + " and t_borrow.returnTime like '%" + returnTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return borrowMapper.queryBorrow(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Borrow> queryBorrow(ResourceInfo resourceObj,UserInfo userObj,String borrowTime,String returnTime) throws Exception  { 
     	String where = "where 1=1";
    	if(null != resourceObj &&  resourceObj.getResourceNo() != null && !resourceObj.getResourceNo().equals(""))  where += " and t_borrow.resourceObj='" + resourceObj.getResourceNo() + "'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_borrow.userObj='" + userObj.getUser_name() + "'";
    	if(!borrowTime.equals("")) where = where + " and t_borrow.borrowTime like '%" + borrowTime + "%'";
    	if(!returnTime.equals("")) where = where + " and t_borrow.returnTime like '%" + returnTime + "%'";
    	return borrowMapper.queryBorrowList(where);
    }

    /*查询所有借阅记录*/
    public ArrayList<Borrow> queryAllBorrow()  throws Exception {
        return borrowMapper.queryBorrowList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(ResourceInfo resourceObj,UserInfo userObj,String borrowTime,String returnTime) throws Exception {
     	String where = "where 1=1";
    	if(null != resourceObj &&  resourceObj.getResourceNo() != null && !resourceObj.getResourceNo().equals(""))  where += " and t_borrow.resourceObj='" + resourceObj.getResourceNo() + "'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_borrow.userObj='" + userObj.getUser_name() + "'";
    	if(!borrowTime.equals("")) where = where + " and t_borrow.borrowTime like '%" + borrowTime + "%'";
    	if(!returnTime.equals("")) where = where + " and t_borrow.returnTime like '%" + returnTime + "%'";
        recordNumber = borrowMapper.queryBorrowCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取借阅记录*/
    public Borrow getBorrow(int borrowId) throws Exception  {
        Borrow borrow = borrowMapper.getBorrow(borrowId);
        return borrow;
    }

    /*更新借阅记录*/
    public void updateBorrow(Borrow borrow) throws Exception {
        borrowMapper.updateBorrow(borrow);
    }

    /*删除一条借阅记录*/
    public void deleteBorrow (int borrowId) throws Exception {
        borrowMapper.deleteBorrow(borrowId);
    }

    /*删除多条借阅信息*/
    public int deleteBorrows (String borrowIds) throws Exception {
    	String _borrowIds[] = borrowIds.split(",");
    	for(String _borrowId: _borrowIds) {
    		borrowMapper.deleteBorrow(Integer.parseInt(_borrowId));
    	}
    	return _borrowIds.length;
    }
}
