package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Receive {
    /*领用id*/
    private Integer receiveId;
    public Integer getReceiveId(){
        return receiveId;
    }
    public void setReceiveId(Integer receiveId){
        this.receiveId = receiveId;
    }

    /*领用的资源*/
    private ResourceInfo resourceObj;
    public ResourceInfo getResourceObj() {
        return resourceObj;
    }
    public void setResourceObj(ResourceInfo resourceObj) {
        this.resourceObj = resourceObj;
    }

    /*领用用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*领用数量*/
    @NotNull(message="必须输入领用数量")
    private Integer receiveNum;
    public Integer getReceiveNum() {
        return receiveNum;
    }
    public void setReceiveNum(Integer receiveNum) {
        this.receiveNum = receiveNum;
    }

    /*领用时间*/
    @NotEmpty(message="领用时间不能为空")
    private String receiveTime;
    public String getReceiveTime() {
        return receiveTime;
    }
    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    /*领用用途*/
    private String purpose;
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonReceive=new JSONObject(); 
		jsonReceive.accumulate("receiveId", this.getReceiveId());
		jsonReceive.accumulate("resourceObj", this.getResourceObj().getName());
		jsonReceive.accumulate("resourceObjPri", this.getResourceObj().getResourceNo());
		jsonReceive.accumulate("userObj", this.getUserObj().getName());
		jsonReceive.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonReceive.accumulate("receiveNum", this.getReceiveNum());
		jsonReceive.accumulate("receiveTime", this.getReceiveTime().length()>19?this.getReceiveTime().substring(0,19):this.getReceiveTime());
		jsonReceive.accumulate("purpose", this.getPurpose());
		return jsonReceive;
    }}