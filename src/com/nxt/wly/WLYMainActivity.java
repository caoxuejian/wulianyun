package com.nxt.wly;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hichip.base.HiLog;
import com.hichip.sdk.HiChipSDK;
import com.hichip.sdk.HiChipSDK.HiChipInitCallback;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nxt.img.AlbumEditActivity;
import com.nxt.nxtapp.AbsMainActivity;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.http.NxtRestClient;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.wly.entity.LoginInfo;
import com.nxt.wly.entity.Operate;
import com.nxt.wly.fragment.X5MainFragment;
import com.nxt.wly.fragment.Morefragment;
import com.nxt.wly.util.Constans;
import com.nxt.wly.util.RosterTask;
import com.nxt.wly.x5view.FirstLoadingX5Service;
import com.tencent.smtt.sdk.QbSdk;
import com.thecamhi.base.DatabaseManager;
import com.thecamhi.bean.HiDataValue;
import com.thecamhi.bean.MyCamera;
import com.thecamhi.main.CameraFragment;

public class WLYMainActivity extends AbsMainActivity implements OnClickListener {
	private static String TAG = "XNBMainActivity";
	private TextView category_title;
	private int width;
	private LinearLayout tab1, tab2, tab3;
	private ImageView iv_1;
	private ImageView iv_2;
	private ImageView iv_3;
	private TextView title;
	private ImageView iv_addnsjl;
	private long initSdkTime;
	private Util util;
	private final static int HANDLE_MESSAGE_INIT_END = 0x90000001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wly_main);
		/**
		 * X5内核在使用preinit接口之后，对于首次安装首次加载没有效果
		 * 实际上，X5webview的preinit接口只是降低了webview的冷启动时间；
		 * 因此，现阶段要想做到首次安装首次加载X5内核，必须要让X5内核提前获取到内核的加载条件
		 *//*
		if (!QbSdk.isTbsCoreInited()) {// preinit只需要调用一次，如果已经完成了初始化，那么就直接构造view
			QbSdk.preInit(WLYMainActivity.this);// 设置X5初始化完成的回调接口
			// 第三个参数为true：如果首次加载失败则继续尝试加载；
		}
		  *//**
		  * 开启额外进程 服务 预加载X5内核， 此操作必须在主进程调起X5内核前进行，否则将不会实现预加载
		  *//*
		Intent intent = new Intent(this, FirstLoadingX5Service.class);
		startService(intent);*/
		util = new Util(this);
		//获取该园区摄像头列表
		RosterTask roster = new RosterTask(getApplicationContext(),
				new RosterTask.BackUI() {
			@Override
			public void back(String result) {
			}
		});
		roster.execute("n");
		initView();
		//加载视频sdk
		HiDataValue.CameraList=new ArrayList<MyCamera>();
		initSDK();
		new Thread(){
			public void run() {
				loadoperate();
			};
		}.start();
	}
	//初始化SDK
	private void initSDK() {
		initSdkTime=System.currentTimeMillis();
		HiChipSDK.init(new HiChipInitCallback() {

			@Override
			public void onSuccess() {
				Message msg=handler.obtainMessage();
				msg.what=HANDLE_MESSAGE_INIT_END;
				handler.sendMessage(msg);
				HiLog.v("success");
			}
			@Override
			public void onFali(int arg0, int arg1) {
				Message msg=handler.obtainMessage();
				msg.what=HANDLE_MESSAGE_INIT_END;
				handler.sendMessage(msg);
				HiLog.v("fail");
			}
		});

	}
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_MESSAGE_INIT_END:
				long spendingTime=System.currentTimeMillis()-initSdkTime;
				if(spendingTime<2000){
					this.postDelayed(new Runnable() {
						@Override
						public void run() {
							//requestEnd();
							initCamera();
						}
					}, 2000-spendingTime);

				}else{
					//requestEnd();
					initCamera();
				}
				break;

			}

		}
	};
	private LinearLayout tab4;
	private ImageView iv_4;

	//获取本地摄像头列表
	private void initCamera() {
		DatabaseManager manager=new DatabaseManager(this);
		SQLiteDatabase db=manager.getReadableDatabase();
		Cursor cursor=db.query(DatabaseManager.TABLE_DEVICE, 
				new String[] { "_id", "dev_nickname", "dev_uid", "dev_name", "dev_pwd",
				"dev_videoQuality", "dev_alarmState" ,"dev_pushState","snapshot" },
				null, null, null, null, null);

		while(cursor.moveToNext()){
			long id=cursor.getLong(cursor.getColumnIndex("_id"));
			String dev_nickname=cursor.getString(cursor.getColumnIndex("dev_nickname"));
			String dev_uid=cursor.getString(cursor.getColumnIndex("dev_uid"));
			String dev_name=cursor.getString(cursor.getColumnIndex("dev_name"));
			String  dev_pwd=cursor.getString(cursor.getColumnIndex("dev_pwd"));
			int dev_videoQuality=cursor.getInt(cursor.getColumnIndex("dev_videoQuality"));
			int dev_alarmState=cursor.getInt(cursor.getColumnIndex("dev_alarmState"));
			int  dev_pushState=cursor.getInt(cursor.getColumnIndex("dev_pushState"));
			byte[] byteSnapshot=cursor.getBlob(cursor.getColumnIndex("snapshot"));

			Bitmap snapshot=(byteSnapshot != null && byteSnapshot.length > 0) ?
					DatabaseManager.getBitmapFromByteArray(byteSnapshot) : null;
					HiLog.v( "dev_uid:"+dev_uid
							+ "  view_acc:" + dev_name
							+ "  view_pwd:" + dev_pwd
							+ "  dev_nickname:" + dev_nickname
							+ "  videoQuality:" +dev_videoQuality
							+ "  alarmState:" +dev_alarmState
							+" snapshot==null:"+snapshot==null?"true":""
							);

					MyCamera camera=new MyCamera(dev_nickname,
							dev_uid, dev_name, dev_pwd);
					camera.setDbID(id);
					camera.setVideoQuality(dev_videoQuality);
					camera.setAlarmState(dev_alarmState);
					camera.setPushState(dev_pushState);
					camera.snapshot = snapshot;
					camera.saveInCameraList();
					//					camera.connect();

		}
		cursor.close();
		db.close();

		requestEnd();

	}

	public void requestEnd(){
		//获取数据完毕，发送广播到CameraFragment界面去刷新adapter
		Intent intent = new Intent();
		intent.setAction(HiDataValue.ACTION_CAMERA_INIT_END);
		sendBroadcast(intent);
	}
	// 初始化界面
	protected void initView() {
		title=(TextView)findViewById(R.id.category_title);
		iv_addnsjl=(ImageView)findViewById(R.id.iv_addnsjl);
		iv_addnsjl.setVisibility(View.GONE);
		tab1 = (LinearLayout) findViewById(R.id.tab1);
		tab2 = (LinearLayout) findViewById(R.id.tab2);
		tab3 = (LinearLayout) findViewById(R.id.tab3);
		tab4 = (LinearLayout) findViewById(R.id.tab4);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab3.setOnClickListener(this);
		tab4.setOnClickListener(this);
		iv_1=(ImageView)findViewById(R.id.iv_main);
		iv_2=(ImageView)findViewById(R.id.iv_second);
		iv_3=(ImageView)findViewById(R.id.iv_more);
		iv_4=(ImageView)findViewById(R.id.iv_fourth);
		title.setText(getResources().getText(R.string.tab1));
		X5MainFragment fragment_home = new X5MainFragment();
		Bundle bundle = new Bundle();
		bundle.putString("URL",Constans.HUANJINGJIANCE);
		bundle.putString("title", "首页");
		//			bundle.putBoolean("refresh", true);
		fragment_home.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home)
		.addToBackStack(null).commit();
		setViewBackground(tab1, 1);
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		for(MyCamera camera: HiDataValue.CameraList) {
			//			camera.registerIOSessionListener(MainActivity.this);
			if(camera.isSetValueWithoutSave()) {
				camera.updateInDatabase(this);
			}
			camera.disconnect();
		}

		HiChipSDK.uninit();

		//		Log.v("HiChip", "-------------HIP2PDeInit:"+ ret);
		/*	NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(HiDataValue.NOTICE_RUNNING_ID);
		notificationManager.cancel(HiDataValue.NOTICE_ALARM_ID);
		 */
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
	}

	// 点击事件的处理
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.tab1) {// 客户端的tab1(首页)
			iv_addnsjl.setVisibility(View.GONE);
			title.setText(getResources().getText(R.string.tab1));
			X5MainFragment fragment_home = new X5MainFragment();
			Bundle bundle = new Bundle();
			bundle.putString("URL", Constans.HUANJINGJIANCE);
			//			bundle.putBoolean("refresh", true);
			fragment_home.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home)
			.addToBackStack(null).commit();
			setViewBackground(tab1, 1);
		} else if (id == R.id.tab2) {// 客户端的tab2
			iv_addnsjl.setVisibility(View.VISIBLE);
			title.setText(getResources().getText(R.string.tab2));
			X5MainFragment fragment_home = new X5MainFragment();
			Bundle bundle = new Bundle();
			bundle.putString("URL", Constans.NONGSHIJILU);
			//			bundle.putBoolean("refresh", true);
			fragment_home.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home)
			.addToBackStack(null).commit();
			setViewBackground(tab2, 2);

		} else if (id == R.id.tab3) {// 更多
			iv_addnsjl.setVisibility(View.GONE);
			title.setText(getResources().getText(R.string.tab3));
			Morefragment fragment_home = new Morefragment();
			//			Bundle bundle = new Bundle();
			//			bundle.putString("URL", "http://juapp.365960.com/wlwcloud/app/monitoring.html");
			//			//			bundle.putBoolean("refresh", true);
			//			fragment_home.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home)
			.addToBackStack(null).commit();
			setViewBackground(tab3, 3);
		}else if (id == R.id.tab4) {// 客户端的tab4
			iv_addnsjl.setVisibility(View.GONE);
			title.setText(getResources().getText(R.string.tab4));
			CameraFragment fragment_home=new CameraFragment();
			Bundle bundle = new Bundle();
			//			bundle.putString("URL", Constans.NONGSHIJILU);
			//			bundle.putBoolean("refresh", true);
			fragment_home.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment_home)
			.addToBackStack(null).commit();
			setViewBackground(tab4,4);

		}
		else if(id==R.id.iv_addnsjl){//添加农事记录
			/*Intent intent=new Intent();
			intent.setClass(WLYMainActivity.this, X5WebviewActivity.class);
			intent.putExtra("title", "添加农事记录");
			intent.putExtra("webviewpath",Constans.ADDNSJL);
			startActivity(intent);*/
			ArrayList<String> selectedDataList = new ArrayList<String>();
			Bundle bundle = new Bundle();
			bundle.putStringArrayList("dataList", selectedDataList);
			bundle.putString("editContent", "");
			Intent intent=new Intent();
			intent.setClass(WLYMainActivity.this, AlbumEditActivity.class);
			intent.putExtra("sort","13");
			intent.putExtras(bundle);
			startActivity(intent);
//			loadoperate();
			
		}
	}

	private void loadoperate() {
		// TODO Auto-generated method stub
		RequestParams rp = new RequestParams();
		rp.put("baid", util.getFromSp("baid", ""));
		rp.put("uname", util.getFromSp("uname", ""));
//		NxtRestClient.get(url, params, responseHandler);
		NxtRestClient.get(Constans.GETOPERATE, rp, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Operate operate = (Operate)JsonPaser.getObjectDatas(Operate.class, content);
				if("0".equals(operate.getErrorcode())){
 					util.saveToSp("operate",operate.getType());
 					util.saveToSp("operate_size", operate.getOperate_size());
 					String a = operate.getOperate_size();
//					ArrayList<String> selectedDataList = new ArrayList<String>();
//					Bundle bundle = new Bundle();
//					bundle.putStringArrayList("dataList", selectedDataList);
//					bundle.putString("editContent", "");
//					Intent intent=new Intent();
//					intent.setClass(WLYMainActivity.this, AlbumEditActivity.class);
//					intent.putExtra("sort","13");
//					intent.putExtras(bundle);
//					startActivity(intent);
				}
			}
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
			}
		});
	}
	// 点击效果设置
	private void setViewBackground(View tab, int id) {
		iv_1.setBackgroundResource(R.drawable.hjjc1);
		iv_2.setBackgroundResource(R.drawable.nsjl1);
		iv_3.setBackgroundResource(R.drawable.sz1);
		iv_4.setBackgroundResource(R.drawable.sssp1);
		switch (id) {
		case 1:
			iv_1.setBackgroundResource(R.drawable.hjjc);
			break;
		case 2:
			iv_2.setBackgroundResource(R.drawable.nsjl);
			break;
		case 3:
			iv_3.setBackgroundResource(R.drawable.sz);
			break;
		case 4:
			iv_4.setBackgroundResource(R.drawable.sssp);
			break;
		default:
			break;
		}
	}

}
