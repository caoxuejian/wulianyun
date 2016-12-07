package com.nxt.wly;



import java.util.List;

import org.apache.http.cookie.Cookie;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.nxt.nxtapp.AbsMainActivity;
import com.nxt.nxtapp.common.MD5;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.wly.entity.CameraInfo;
import com.nxt.wly.entity.CamerasInfo;
import com.nxt.wly.entity.LoginInfo;
import com.nxt.wly.util.Constans;
import com.shelwee.update.UpdateHelper;
import com.thecamhi.bean.HiDataValue;
import com.thecamhi.bean.MyCamera;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author 曹学建
 * @category 启动页面
 */
public class SplashActivity extends AbsMainActivity {

	private String ver;
	private SplashActivity context;
	private ImageView img;
	private Util util;
	private String uname;
	private String pwd;
	private ProgressDialog pdlogin;


	@SuppressLint("NewApi") public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		if(Build.VERSION.SDK_INT>=14){
			//隐藏底部的虚拟按键顶部导航栏,此方法只能是在api 14以上才可用
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

		}else{
			requestWindowFeature(Window.FEATURE_NO_TITLE);// 去头条目 全屏
		}
		context = this;
		setContentView(R.layout.activity_splash);
		img = (ImageView) findViewById(R.id.img_welcome);
		util=new Util(this);
		uname=util.getFromSp("uname", "");
		pwd=util.getFromSp("pwd", "");
		// 开机动画
		AlphaAnimation alpAnimation = new AlphaAnimation(0.01f,1.0f);
		alpAnimation.setDuration(4000);//设置动画持续时间
		alpAnimation.setAnimationListener(new AnimationListener() {

			private String isfirstinstall;

			@Override
			public void onAnimationStart(Animation animation) {
				UpdateHelper updateHelper = new UpdateHelper.Builder(SplashActivity.this,"0")
				.checkUrl(Constans.UPLOAD_VERSION)
				.isAutoInstall(true) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
				.build();
				updateHelper.check();
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				startactivity();
				/*isfirstinstall=util.getFromSp("isFirstInstall", "");
				if("false".equals(isfirstinstall)){
					startactivity();
				}else{
					//显示加载VR引擎提示
					showdialog();
				}*/
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}
		});
		img.setAnimation(alpAnimation);
		alpAnimation.startNow();
		// }
	}


	private void getcameras() {
		// TODO Auto-generated method stub
		AsyncHttpClient  client=new AsyncHttpClient ();
		//保存cookie，自动保存到了shareprefercece  
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);   
		client.setCookieStore(myCookieStore);
		client.post(Constans.GETCAMERAS,null, new AsyncHttpResponseHandler() {
			private MyCamera camera;
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				CamerasInfo camerasInfo=(CamerasInfo) JsonPaser.getObjectDatas(
						CamerasInfo.class, content);
				
				if(!camerasInfo.getCamera_size().equals(util.getFromSp("camerasize", ""))){
					util.saveToSp("camerasize", camerasInfo.getCamera_size());
					List<CameraInfo> cameras= JsonPaser.getArrayDatas(CameraInfo.class,camerasInfo.getCamera());
					for(int i=0;i<cameras.size();i++){
						camera = new MyCamera(cameras.get(i).getDev_nickname(), i+"", cameras.get(i).getDev_name(), cameras.get(i).getDev_pwd());
						camera.deleteInCameraList();
						camera.deleteInDatabase(SplashActivity.this);
						camera.saveInDatabase(SplashActivity.this);
//						camera.saveInCameraList();
					}
					Intent broadcast = new Intent();
					broadcast.setAction(HiDataValue.ACTION_CAMERA_INIT_END);
					sendBroadcast(broadcast);
					pdlogin.cancel();
					Intent intent=new Intent();
					intent.setClass(SplashActivity.this,WLYMainActivity.class);
					startActivity(intent);
					finish();
				}else{
					pdlogin.cancel();
					Intent intent=new Intent();
					intent.setClass(SplashActivity.this,WLYMainActivity.class);
					startActivity(intent);
					finish();
				}
			}
			@Override
			public void onFailure(Throwable error,
					String content) {
				super.onFailure(error, content);
				pdlogin.cancel();
			}
		});
	}
	protected void startactivity() {
		// TODO Auto-generated method stub
		if(uname==null||"".equals(uname)){
			Intent intent=new Intent();
			intent.setClass(SplashActivity.this, DengLuActivity.class);
			startActivity(intent);
		}else{
			//登录过之后再次进入自动登录
			autoLogin();
		}
	}



	private void autoLogin() {
		// TODO Auto-generated method stub

		// 显示进度条
		pdlogin = new ProgressDialog(this);
		pdlogin.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdlogin.setCancelable(false);
		pdlogin.setMessage("登录中,请稍后...");
		pdlogin.show();
		RequestParams params = new RequestParams();
		params.put("uname", uname);
		params.put("pword", pwd);
		AsyncHttpClient  client=new AsyncHttpClient ();
		//保存cookie，自动保存到了shareprefercece  
		PersistentCookieStore myCookieStore = new PersistentCookieStore(SplashActivity.this);   
		client.setCookieStore(myCookieStore);
		client.post(Constans.LOGIN,params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				LoginInfo rootdata = (LoginInfo) JsonPaser.getObjectDatas(
						LoginInfo.class, content);
				Message msg = handler.obtainMessage();
				msg.obj = rootdata;
				handler.sendMessage(msg);
//				//获取cookie
//				PersistentCookieStore myCookieStore = new PersistentCookieStore(SplashActivity.this);  
//				List<Cookie> cookies = myCookieStore.getCookies(); 
//				util.saveToSp("session_id", cookies.get(0).getValue());
				
			}
			@Override
			public void onFailure(Throwable error,
					String content) {
				super.onFailure(error, content);
				pdlogin.dismiss();
				System.out.println("@@@@@@@@@@@@loginerror"+content);
				Toast.makeText(context, "登录失败请重试！", Toast.LENGTH_LONG).show();
				
			}
		});

	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			LoginInfo login = (LoginInfo) msg.obj;
			if (login != null) {
				if (login.getErrorcode() != null
						&& login.getErrorcode().equals("0")) {

					util.saveToSp("uid", login.getUid());
					util.saveToSp("uname", login.getUname());
					util.saveToSp("baid", login.getBaid());
					util.saveToSp("pwd", pwd);
					util.saveToSp("scene", login.getScene());
					util.saveToSp("session_id", login.getSession_id());
					Toast.makeText(SplashActivity.this, "登录成功", Toast.LENGTH_LONG).show();
//					getcameras();
					Intent intent=new Intent();
					intent.setClass(SplashActivity.this,WLYMainActivity.class);
					startActivity(intent);
					finish();
				}else{
					Intent intent=new Intent();
					intent.setClass(SplashActivity.this,DengLuActivity.class);
					startActivity(intent);
					finish();
				}
			}
		};
	};
	//提示加载VR引擎
	private  void showdialog() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(context, R.style.dialog);
		View inflate = View.inflate(context, R.layout.x5dialog, null);
		TextView dialogCancel = (TextView) inflate.findViewById(R.id.del_cancel);

		dialogCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Intent intent=new Intent();
				intent.setClass(SplashActivity.this, DengLuActivity.class);
				startActivity(intent);
				finish();
			}


		});
		TextView dialogConfirm = (TextView) inflate.findViewById(R.id.confirm_del);
		dialogConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				util.saveToSp("isFirstInstall", "false");
				android.os.Process.killProcess(Process.myPid());
			}
		});
		dialog.setContentView(inflate);
		dialog.show();
	}

}
