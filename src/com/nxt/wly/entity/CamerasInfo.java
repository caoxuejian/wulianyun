package com.nxt.wly.entity;
import java.io.Serializable;

public class CamerasInfo implements Serializable{
private String camera;
private String errorcode;
private String camera_size;
public String getCamera() {
	return camera;
}
public void setCamera(String camera) {
	this.camera = camera;
}
public String getErrorcode() {
	return errorcode;
}
public void setErrorcode(String errorcode) {
	this.errorcode = errorcode;
}
public String getCamera_size() {
	return camera_size;
}
public void setCamera_size(String camera_size) {
	this.camera_size = camera_size;
}
@Override
public String toString() {
	return "CamerasInfo [camera=" + camera + ", errorcode=" + errorcode
			+ ", camera_size=" + camera_size + "]";
}


}
