package com.nxt.wly.entity;
import java.io.Serializable;

//操作类型
import android.graphics.Bitmap;

public class Operate implements Serializable{
	public String getErrorcode() {
		return errorcode;
	}

	public String getOperate_size() {
		return operate_size;
	}

	private String type;
	private String errorcode;
	private String operate_size;
	
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public void setOperate_size(String operate_size) {
		this.operate_size = operate_size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Operate [type=" + type + ", errorcode=" + errorcode
				+ ", operate_size=" + operate_size + "]";
	}

	

}
