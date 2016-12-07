package com.nxt.wly.entity;

import java.io.Serializable;
import java.util.List;


//动态内部实体类
public class DynamicMsgListContent implements Serializable {
	private String commentNum;
	private String id;
	private String zanList;
	private String zanNum;
	private String title;
	private String address;
	private String img;
	private String iszan;
	private String createDate;
	private String msg;
	private String user;
	private String commentList;
	private String urlLink;
	private String zhuanfanum;
	private List<Picture> mlist;
	public String getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getZanList() {
		return zanList;
	}
	public void setZanList(String zanList) {
		this.zanList = zanList;
	}

	public String getIszan() {
		return iszan;
	}
	public void setIszan(String iszan) {
		this.iszan = iszan;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

	public String getZanNum() {
		return zanNum;
	}
	public void setZanNum(String zanNum) {
		this.zanNum = zanNum;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getCommentList() {
		return commentList;
	}
	public void setCommentList(String commentList) {
		this.commentList = commentList;
	}
	public String getUrlLink() {
		return urlLink;
	}
	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}
	public String getZhuanfanum() {
		return zhuanfanum;
	}
	public void setZhuanfanum(String zhuanfanum) {
		this.zhuanfanum = zhuanfanum;
	}
	public List<Picture> getMlist() {
		return mlist;
	}
	public void setMlist(List<Picture> mlist) {
		this.mlist = mlist;
	}
	
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "DynamicMsgListContent [commentNum=" + commentNum + ", id=" + id
				+ ", zanList=" + zanList + ", zanNum=" + zanNum + ", title="
				+ title + ", address=" + address + ", img=" + img + ", iszan="
				+ iszan + ", createDate=" + createDate + ", msg=" + msg
				+ ", user=" + user + ", commentList=" + commentList
				+ ", urlLink=" + urlLink + ", zhuanfanum=" + zhuanfanum
				+ ", mlist=" + mlist + "]";
	}

	

}
