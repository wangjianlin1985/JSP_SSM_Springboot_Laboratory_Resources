package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class ResourceInfo {
    /*资源编号*/
    @NotEmpty(message="资源编号不能为空")
    private String resourceNo;
    public String getResourceNo(){
        return resourceNo;
    }
    public void setResourceNo(String resourceNo){
        this.resourceNo = resourceNo;
    }

    /*资源类型*/
    private ResourceType resourceTypeObj;
    public ResourceType getResourceTypeObj() {
        return resourceTypeObj;
    }
    public void setResourceTypeObj(ResourceType resourceTypeObj) {
        this.resourceTypeObj = resourceTypeObj;
    }

    /*资源名称*/
    @NotEmpty(message="资源名称不能为空")
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*资源图片*/
    private String resourcePhoto;
    public String getResourcePhoto() {
        return resourcePhoto;
    }
    public void setResourcePhoto(String resourcePhoto) {
        this.resourcePhoto = resourcePhoto;
    }

    /*数量限制*/
    @NotEmpty(message="数量限制不能为空")
    private String numberLimit;
    public String getNumberLimit() {
        return numberLimit;
    }
    public void setNumberLimit(String numberLimit) {
        this.numberLimit = numberLimit;
    }

    /*资源库存*/
    @NotNull(message="必须输入资源库存")
    private Integer resourceNum;
    public Integer getResourceNum() {
        return resourceNum;
    }
    public void setResourceNum(Integer resourceNum) {
        this.resourceNum = resourceNum;
    }

    /*加入日期*/
    @NotEmpty(message="加入日期不能为空")
    private String addDate;
    public String getAddDate() {
        return addDate;
    }
    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    /*资源描述*/
    private String resourceDesc;
    public String getResourceDesc() {
        return resourceDesc;
    }
    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonResourceInfo=new JSONObject(); 
		jsonResourceInfo.accumulate("resourceNo", this.getResourceNo());
		jsonResourceInfo.accumulate("resourceTypeObj", this.getResourceTypeObj().getTypeName());
		jsonResourceInfo.accumulate("resourceTypeObjPri", this.getResourceTypeObj().getTypeId());
		jsonResourceInfo.accumulate("name", this.getName());
		jsonResourceInfo.accumulate("resourcePhoto", this.getResourcePhoto());
		jsonResourceInfo.accumulate("numberLimit", this.getNumberLimit());
		jsonResourceInfo.accumulate("resourceNum", this.getResourceNum());
		jsonResourceInfo.accumulate("addDate", this.getAddDate().length()>19?this.getAddDate().substring(0,19):this.getAddDate());
		jsonResourceInfo.accumulate("resourceDesc", this.getResourceDesc());
		return jsonResourceInfo;
    }}