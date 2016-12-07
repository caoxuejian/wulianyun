package com.nxt.wly;

import java.net.URL;
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
import com.thecamhi.bean.HiDataValue;
import com.thecamhi.bean.MyCamera;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登录
 * @author 曹学建
 *
 */

public class DengLuActivity extends AbsMainActivity implements OnClickListener {

	private URL url;
	private Util util;
	private LinearLayout historyuser;
	private LinearLayout ll_number;
	private Button button_changeuser;
	private Button button_back;
	private EditText editText_zh;
	private TextView textView_zh;
	private TextView forget_paw;
	private EditText editText_pw;
	private ImageView faceImage;
	private Button button_dl;
	private Button button_zc;
	private Button button_yk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		util=new Util(this);
		setContentView(R.layout.activity_denglu);
		initview();
		initData();
		//		//通过httpclient从URL中获得cookie
		//		new Thread(networkTask).start();

	}
	private void initData() {
		// TODO Auto-generated method stub
		str_zh=util.getFromSp("uname", "");
		str_pw=util.getFromSp("pwd", "");
		if(str_zh!=null||"".equals(str_zh)){
			editText_zh.setText(str_zh);
		}
		if(str_pw!=null||"".equals(str_pw)){
			editText_pw.setText(str_pw);
		}
	}
	private void initview() {
		// TODO Auto-generated method stub
		historyuser = (LinearLayout) findViewById(R.id.historyuser);
		ll_number = (LinearLayout) findViewById(R.id.ll_number);
		button_changeuser = (Button) findViewById(R.id.changeuser);
		button_back = (Button) findViewById(R.id.news_view_back);
		editText_zh = (EditText) findViewById(R.id.edit_zh);
		textView_zh = (TextView) findViewById(R.id.tv_zh);
		forget_paw = (TextView) findViewById(R.id.forget_paw);
		editText_pw = (EditText) findViewById(R.id.edit_password);
		faceImage = (ImageView) findViewById(R.id.login_head_img);
		button_dl = (Button) findViewById(R.id.denglu_btn);
		button_zc = (Button) findViewById(R.id.zhuce_btn);
		button_yk = (Button) findViewById(R.id.youke_btn);
		button_dl.setOnClickListener(this);
		button_zc.setOnClickListener(this);
		forget_paw.setOnClickListener(this);
		button_yk.setOnClickListener(this);
	}
	/** 
	 * 网络操作相关的子线程 
	 *//*  
	Runnable networkTask = new Runnable() {  

		@Override  
		public void run() {  
			// TODO  
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(Constans.LOGIN);
				CookieStore cookieStore = new BasicCookieStore();
				HttpContext context = new BasicHttpContext();
				context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
				HttpResponse response = client.execute(post, context);
				List<Cookie> cookies = cookieStore.getCookies();
				for(int i=0;i<cookies.size();i++){
					System.out.println("@@@@"+i+"@"+cookies.get(i).getValue());
					util.saveToSp("ci_session", cookies.get(i).getValue());
					//					Intent intent=new Intent();
					//					intent.setClass(DengLuActivity.this, X5WebviewActivity.class);
					//					intent.putExtra("webviewpath", "http://wly.365960.com/park/index.php/Cloud_app/testa2");
					//					intent.putExtra("ci_session", cookies.get(i).getValue());
					//					startActivity(intent);
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}  
	};*/
	private String str_zh;
	private String str_pw;
	private ProgressDialog pdlogin; 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		if(id==R.id.denglu_btn){
			ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cwjManager.getActiveNetworkInfo() != null) {
				if (ll_number.getVisibility() == View.VISIBLE) {
					str_zh = editText_zh.getText().toString();
				}
				str_pw = editText_pw.getText().toString();
				if (str_zh.equals("") || str_pw.equals("")) {
					Toast.makeText(this, "账号密码不能为空！", 0).show();
				} else {
					
					login(str_zh, str_pw);
				}
			} else
				Toast.makeText(this, "请检查您的网络是否连接", 0).show();

		}
	}
	private void login(String name, String psw) {
		// TODO Auto-generated method stub
		// 显示进度条
		pdlogin = new ProgressDialog(this);
		pdlogin.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdlogin.setCancelable(false);
		pdlogin.setMessage("登录中,请稍后...");
		pdlogin.show();
		RequestParams params = new RequestParams();
		params.put("uname", name);
		params.put("pword", MD5.makeMD5(psw));
		AsyncHttpClient  client=new AsyncHttpClient ();
		//保存cookie，自动保存到了shareprefercece  
		PersistentCookieStore myCookieStore = new PersistentCookieStore(DengLuActivity.this);   
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
				/*//获取cookie
				PersistentCookieStore myCookieStore = new PersistentCookieStore(DengLuActivity.this);  
				List<Cookie> cookies = myCookieStore.getCookies(); 
				util.saveToSp("session_id", cookies.get(0).getValue());*/
			}
			@Override
			public void onFailure(Throwable error,
					String content) {
				super.onFailure(error, content);
				pdlogin.dismiss();
				Toast.makeText(DengLuActivity.this, "登录失败，请检查网络是否连接！", Toast.LENGTH_LONG).show();
			}
		});
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
						camera.deleteInDatabase(DengLuActivity.this);
						camera.saveInDatabase(DengLuActivity.this);
//						camera.saveInCameraList();
					}
					Intent broadcast = new Intent();
					broadcast.setAction(HiDataValue.ACTION_CAMERA_INIT_END);
					sendBroadcast(broadcast);
					pdlogin.cancel();
					Intent intent=new Intent();
					intent.setClass(DengLuActivity.this,WLYMainActivity.class);
					startActivity(intent);
					finish();
				}else{
					pdlogin.cancel();
					Intent intent=new Intent();
					intent.setClass(DengLuActivity.this,WLYMainActivity.class);
					startActivity(intent);
					finish();
				}
			}
			@Override
			public void onFailure(Throwable error,
					String content) {
				super.onFailure(error, content);
				pdlogin.dismiss();
				Toast.makeText(DengLuActivity.this, "登录失败，请重新登录！", Toast.LENGTH_LONG).show();
			
			}
		});
	}
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			LoginInfo login = (LoginInfo) msg.obj;
			if (login != null) {
				if (login.getErrorcode() != null
						&& login.getErrorcode().equals("0")) {

					util.saveToSp("uid", login.getUid());
					util.saveToSp("uname", login.getUname());
					util.saveToSp("scene", login.getScene());//园区场景
					util.saveToSp("pwd", MD5.makeMD5(str_pw));
					util.saveToSp("baid", login.getBaid());//园区id
					util.saveToSp("status", "1");
					util.saveToSp("session_id", login.getSession_id());
					Toast.makeText(DengLuActivity.this, "登录成功", Toast.LENGTH_LONG).show();
					Intent intent=new Intent();
					intent.setClass(DengLuActivity.this,WLYMainActivity.class);
					startActivity(intent);
					finish();
//					getcameras();
				}else{
					pdlogin.cancel();
					Util.showMsg(DengLuActivity.this, "登录失败请重试！");
				}
			}
		};
	};
}
