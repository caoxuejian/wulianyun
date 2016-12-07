package com.nxt.wly.entity;
/*
 * 登录接口返回信息
 */
import java.io.Serializable;

public class LoginInfo implements Serializable{
private String errorcode;
private String uname;
private String uid;
private String session_id;//加载页面时这个session_id都要一直带着
private String baid;//园区id
private String scene;//场景

public String getScene() {
	return scene;
}
public void setScene(String scene) {
	this.scene = scene;
}

public String getErrorcode() {
	return errorcode;
}
public String getBaid() {
	return baid;
}
public void setBaid(String baid) {
	this.baid = baid;
}
public void setErrorcode(String errorcode) {
	this.errorcode = errorcode;
}
public String getUname() {
	return uname;
}
public void setUname(String uname) {
	this.uname = uname;
}
public String getUid() {
	return uid;
}
public void setUid(String uid) {
	this.uid = uid;
}
public String getSession_id() {
	return session_id;
}
public void setSession_id(String session_id) {
	this.session_id = session_id;
}
@Override
public String toString() {
	return "LoginInfo [errorcode=" + errorcode + ", uname=" + uname + ", uid="
			+ uid + ", session_id=" + session_id + ", baid=" + baid
			+ ", scene=" + scene + "]";
}

}
