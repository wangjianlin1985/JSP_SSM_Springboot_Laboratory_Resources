package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ResourceInfo;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.Receive;

import com.chengxusheji.mapper.ReceiveMapper;
@Service
public class ReceiveService {

	@Resource ReceiveMapper receiveMapper;
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

    /*添加领用记录*/
    public void addReceive(Receive receive) throws Exception {
    	receiveMapper.addReceive(receive);
    }

    /*按照查询条件分页查询领用记录*/
    public ArrayList<Receive> queryReceive(ResourceInfo resourceObj,UserInfo userObj,String receiveTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != resourceObj &&  resourceObj.getResourceNo() != null  && !resourceObj.getResourceNo().equals(""))  where += " and t_receive.resourceObj='" + resourceObj.getResourceNo() + "'";
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_receive.userObj='" + userObj.getUser_name() + "'";
    	if(!receiveTime.equals("")) where = where + " and t_receive.receiveTime like '%" + receiveTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return receiveMapper.queryReceive(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Receive> queryReceive(ResourceInfo resourceObj,UserInfo userObj,String receiveTime) throws Exception  { 
     	String where = "where 1=1";
    	if(null != resourceObj &&  resourceObj.getResourceNo() != null && !resourceObj.getResourceNo().equals(""))  where += " and t_receive.resourceObj='" + resourceObj.getResourceNo() + "'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_receive.userObj='" + userObj.getUser_name() + "'";
    	if(!receiveTime.equals("")) where = where + " and t_receive.receiveTime like '%" + receiveTime + "%'";
    	return receiveMapper.queryReceiveList(where);
    }

    /*查询所有领用记录*/
    public ArrayList<Receive> queryAllReceive()  throws Exception {
        return receiveMapper.queryReceiveList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(ResourceInfo resourceObj,UserInfo userObj,String receiveTime) throws Exception {
     	String where = "where 1=1";
    	if(null != resourceObj &&  resourceObj.getResourceNo() != null && !resourceObj.getResourceNo().equals(""))  where += " and t_receive.resourceObj='" + resourceObj.getResourceNo() + "'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_receive.userObj='" + userObj.getUser_name() + "'";
    	if(!receiveTime.equals("")) where = where + " and t_receive.receiveTime like '%" + receiveTime + "%'";
        recordNumber = receiveMapper.queryReceiveCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取领用记录*/
    public Receive getReceive(int receiveId) throws Exception  {
        Receive receive = receiveMapper.getReceive(receiveId);
        return receive;
    }

    /*更新领用记录*/
    public void updateReceive(Receive receive) throws Exception {
        receiveMapper.updateReceive(receive);
    }

    /*删除一条领用记录*/
    public void deleteReceive (int receiveId) throws Exception {
        receiveMapper.deleteReceive(receiveId);
    }

    /*删除多条领用信息*/
    public int deleteReceives (String receiveIds) throws Exception {
    	String _receiveIds[] = receiveIds.split(",");
    	for(String _receiveId: _receiveIds) {
    		receiveMapper.deleteReceive(Integer.parseInt(_receiveId));
    	}
    	return _receiveIds.length;
    }
}
