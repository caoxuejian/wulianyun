package com.nxt.wly.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.common.MD5;
import com.nxt.nxtapp.common.ReadRaw;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.nxtapp.imageutils.ImageLoader;
import com.nxt.nxtapp.model.AppConfigData;
import com.nxt.wly.DengLuActivity;
import com.nxt.wly.FeedbackActivity;
import com.nxt.wly.PictureActivity;
import com.nxt.wly.R;
import com.nxt.wly.ShareActivity;
import com.nxt.wly.VideoActivity;
import com.nxt.wly.util.Constans;
import com.nxt.wly.util.DataCleanManager;
import com.shelwee.update.UpdateHelper;

public class Morefragment extends Fragment implements OnClickListener {

	public static Morefragment newInstance(String updateurl, String versionplist) {
		Morefragment fragment = new Morefragment();
		return fragment;
	}


	private FragmentActivity context;
	private ImageLoader loader;
	private RelativeLayout more_fankui;
	private RelativeLayout more_qinglihuancun;
	private RelativeLayout more_fenxiang;
	private RelativeLayout more_updateversion;
	private Button logout;
	private static TextView login_username;
	private ImageView faceImage;
	private TextView more_version;
	private Util util;
	private String uname;
	private RelativeLayout more_camera_photo;
	private RelativeLayout more_camera_video;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		util=new Util(context);
		uname=util.getFromSp("uname", "");
		loader = ImageLoader.getInstance(context);
		View view = inflater.inflate(R.layout.activity_more, container, false);
		more_fankui = (RelativeLayout) view.findViewById(R.id.more_fankui);
		more_fenxiang = (RelativeLayout) view.findViewById(R.id.more_fenxiang);
		more_qinglihuancun = (RelativeLayout) view.findViewById(R.id.more_qinglihuancun);
		more_updateversion = (RelativeLayout) view.findViewById(R.id.more_updateversion);
		more_camera_photo=(RelativeLayout)view.findViewById(R.id.more_camera_photo);
		more_camera_video=(RelativeLayout)view.findViewById(R.id.more_camera_video);

		logout = (Button) view.findViewById(R.id.logout);
		login_username = (TextView) view.findViewById(R.id.nickname);
		faceImage = (ImageView) view.findViewById(R.id.iv_myhead);
		more_version = (TextView) view.findViewById(R.id.more_version);
		if (uname != null && !"".equals(uname)) {
			login_username.setText(uname);
		} 
		faceImage.setImageResource(R.drawable.contractdefalut);
		try {

			String curVersion = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
			String version = "Version" + curVersion + " @"
					+ getResources().getString(R.string.app_name);
			util.saveToSp(com.nxt.nxtapp.common.Util.VERSION, curVersion);
			more_version.setText(version);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		more_fankui.setOnClickListener(this);
		more_fenxiang.setOnClickListener(this);
		more_qinglihuancun.setOnClickListener(this);
		more_updateversion.setOnClickListener(this);
		more_camera_photo.setOnClickListener(this);
		more_camera_video.setOnClickListener(this);
		logout.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.more_fankui) {//意见反馈
			Intent intent=new Intent();
			intent.setClass(context, FeedbackActivity.class);
			context.startActivity(intent);
		} else if (id == R.id.more_fenxiang) {//分享好友
			Intent intent=new Intent();
			intent.setClass(context, ShareActivity.class);
			context.startActivity(intent);
		} else if (id == R.id.more_qinglihuancun) {//缓存清理
			DataCleanManager.cleanInternalCache(context);
			DataCleanManager.cleanApplicationData(context, null);
			Toast.makeText(context, "清理完成", Toast.LENGTH_LONG).show();
		} else if (id == R.id.more_updateversion) {//软件更新
			UpdateHelper updateHelper = new UpdateHelper.Builder(context)
			.checkUrl(Constans.UPLOAD_VERSION)
			.isAutoInstall(true) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
			.build();
			updateHelper.check();
		} else if (id == R.id.logout) {
			if (uname != null && !"".equals(uname)) {
				getloginout().show();
			} else {
			}
		}else if(id==R.id.more_camera_photo){
			//本地拍照
			Intent intent=new Intent();
			intent.setClass(context, PictureActivity.class);
			context.startActivity(intent);
		}else if(id==R.id.more_camera_video){
			//本地录像
			Intent intent=new Intent();
			intent.setClass(context, VideoActivity.class);
			context.startActivity(intent);
			
		}
	}


	public synchronized AlertDialog.Builder getloginout() {
		return new AlertDialog.Builder(context)
		.setCancelable(false)
		.setTitle("用户注销")
		.setMessage("0".equals(util.getFromSp("status", ""))?"建议您注册，我们将提供更好的信息服务":"确定注销当前用户？")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				ProgressDialog progress = ProgressDialog.show(context,
						null, "请稍候...", true, true);
				progress.show();
				logout();
				progress.dismiss();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
	}

	/**
	 * 注销线程
	 */
	private void logout() {
		Intent intent = new Intent(context, DengLuActivity.class);
		CleanInf(util, context);
		Morefragment.login_username.setText(null);
		startActivity(intent);
	}


	public static void CleanInf(Util util, Context context) {
		util.saveToSp("uid", "");
		util.saveToSp("uname", "");
		util.saveToSp("baid", "");
		util.saveToSp("pwd","");
		util.saveToSp("status", "");
		util.saveToSp("camerasize", "");

	}
}
