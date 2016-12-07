package com.nxt.wly.util;

import java.io.File;

import android.os.Environment;


public class Constans {
	//环境监测
//	public static String HUANJINGJIANCE="http://juapp.365960.com/wlwcloud/app/monitoring.html";
	public static String HUANJINGJIANCE="http://wly.365960.com/park/index.php/Cloud_app/scene";
	//农事记录
//	public static String NONGSHIJILU="http://juapp.365960.com/wlwcloud/app/record.html";
	public static String NONGSHIJILU="http://wly.365960.com/park/index.php/Cloud_app/fards";
	//添加农事记录
//	public static String ADDNSJL="http://juapp.365960.com/wlwcloud/app/from.html";
	public static String ADDNSJL="http://wly.365960.com/park/index.php/Cloud_app/addnsjl";
	//版本更新
	public static String UPLOAD_VERSION="http://219.232.243.58:81/andriod/wly_version.txt";

	//登录接口
	public static String LOGIN="http://wly.365960.com/park/index.php/Cloud_app/login?";
	//该园区摄像头信息
	public static String GETCAMERAS="http://wly.365960.com/park/index.php/Cloud_app/videolist";

	//摄像头同步
	public static String ADDCAMERA="http://wly.365960.com/park/index.php/Cloud_app/addvideo";
	public static String NX_RECV_SAVE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "WLY" + File.separator + "JNBfile_recv";
	
	//获取操作类型
	public static String GETOPERATE="http://wly.365960.com/park/index.php/Cloud_app/nstype?";
	//发布农事记录
	public static String POSTNSJL="http://wly.365960.com/park/index.php/Cloud_app/nsadd";
	//删除摄像头
	public static String DELETECAME="http://wly.365960.com/park/index.php/Cloud_app/delvideo";
}
