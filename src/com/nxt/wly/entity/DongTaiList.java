package com.nxt.wly.entity;

import java.io.Serializable;
//动态实体类
public class DongTaiList  implements Serializable{
	private String dynamicMsgList;
	private String recnums;

	public String getDynamicMsgList() {
		return dynamicMsgList;
	}

	public void setDynamicMsgList(String dynamicMsgList) {
		this.dynamicMsgList = dynamicMsgList;
	}

	public String getRecnums() {
		return recnums;
	}

	public void setRecnums(String recnums) {
		this.recnums = recnums;
	}
	
	

}
