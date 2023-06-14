package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class ResourceType {
    /*类型id*/
    private Integer typeId;
    public Integer getTypeId(){
        return typeId;
    }
    public void setTypeId(Integer typeId){
        this.typeId = typeId;
    }

    /*类型名称*/
    @NotEmpty(message="类型名称不能为空")
    private String typeName;
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /*类别描述*/
    private String typeDesc;
    public String getTypeDesc() {
        return typeDesc;
    }
    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
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

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonResourceType=new JSONObject(); 
		jsonResourceType.accumulate("typeId", this.getTypeId());
		jsonResourceType.accumulate("typeName", this.getTypeName());
		jsonResourceType.accumulate("typeDesc", this.getTypeDesc());
		jsonResourceType.accumulate("addDate", this.getAddDate().length()>19?this.getAddDate().substring(0,19):this.getAddDate());
		return jsonResourceType;
    }}