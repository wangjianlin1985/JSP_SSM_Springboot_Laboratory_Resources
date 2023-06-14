package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Borrow {
    /*借阅id*/
    private Integer borrowId;
    public Integer getBorrowId(){
        return borrowId;
    }
    public void setBorrowId(Integer borrowId){
        this.borrowId = borrowId;
    }

    /*借阅的资源*/
    private ResourceInfo resourceObj;
    public ResourceInfo getResourceObj() {
        return resourceObj;
    }
    public void setResourceObj(ResourceInfo resourceObj) {
        this.resourceObj = resourceObj;
    }

    /*借阅用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*借阅数量*/
    @NotNull(message="必须输入借阅数量")
    private Integer borrowNum;
    public Integer getBorrowNum() {
        return borrowNum;
    }
    public void setBorrowNum(Integer borrowNum) {
        this.borrowNum = borrowNum;
    }

    /*借阅时间*/
    @NotEmpty(message="借阅时间不能为空")
    private String borrowTime;
    public String getBorrowTime() {
        return borrowTime;
    }
    public void setBorrowTime(String borrowTime) {
        this.borrowTime = borrowTime;
    }

    /*归还时间*/  
    private String returnTime;
    public String getReturnTime() {
        return returnTime;
    }
    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    /*附加信息*/
    private String memo;
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonBorrow=new JSONObject(); 
		jsonBorrow.accumulate("borrowId", this.getBorrowId());
		jsonBorrow.accumulate("resourceObj", this.getResourceObj().getName());
		jsonBorrow.accumulate("resourceObjPri", this.getResourceObj().getResourceNo());
		jsonBorrow.accumulate("userObj", this.getUserObj().getName());
		jsonBorrow.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonBorrow.accumulate("borrowNum", this.getBorrowNum());
		jsonBorrow.accumulate("borrowTime", this.getBorrowTime().length()>19?this.getBorrowTime().substring(0,19):this.getBorrowTime());
		jsonBorrow.accumulate("returnTime", this.getReturnTime().length()>19?this.getReturnTime().substring(0,19):this.getReturnTime());
		jsonBorrow.accumulate("memo", this.getMemo());
		return jsonBorrow;
    }}