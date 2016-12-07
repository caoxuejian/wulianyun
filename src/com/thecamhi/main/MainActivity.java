/*package com.thecamhi.main;

import java.util.Timer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.hichip.base.HiLog;
import com.hichip.sdk.HiChipSDK;
import com.hichip.sdk.HiChipSDK.HiChipInitCallback;
import com.nxt.wly.R;
import com.nxt.wly.fragment.MainFragment;
import com.thecamhi.base.DatabaseManager;
import com.thecamhi.bean.HiDataValue;
import com.thecamhi.bean.MyCamera;


public class MainActivity extends FragmentActivity {

	private final static int HANDLE_MESSAGE_INIT_END = 0x90000001;
	private Class<?> fraList[]={CameraFragment.class,MainFragment.class,VideoFragment.class,AboutFragment.class};
	private int drawable[]={R.drawable.tab_camera_icon,R.drawable.tab_pic_icon,R.drawable.tab_voideo_icon,R.drawable.tab_about_icon};
	private ImageView welcom_imv;
	private long initSdkTime;
	private int count=0;
	Timer timer;
	private long exitTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		initview();
		initTabHost();
		initSDK();
		
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

	//搭载4个fragment
	private void initTabHost() {
		String[] tabString=getResources().getStringArray(R.array.tab_name);
		FragmentTabHost tabHost=(FragmentTabHost)findViewById(R.id.main_fragment_tabhost);
		tabHost.setup(this, getSupportFragmentManager(), R.id.fragment_main_content);
		LayoutInflater inflater=LayoutInflater.from(this);
		for(int i=0;i<fraList.length;i++){
			View view=inflater.inflate(R.layout.fragment_tabhost_switch_image, null);
			ImageView iv=(ImageView)view.findViewById(R.id.main_tabhost_imv);
			TextView tv=(TextView)view.findViewById(R.id.main_tabhost_tv);
			iv.setImageResource(drawable[i]);
			tv.setText(tabString[i]);
			TabSpec tabItem=tabHost.newTabSpec(tabString[i]).setIndicator(view);
			Bundle bundle=new Bundle();
			bundle.putString("title", "title");
			bundle.putString("url", "url");
			tabHost.addTab(tabItem,fraList[i],bundle);
		}
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
							welcom_imv.setVisibility(View.GONE);
						}
					}, 2000-spendingTime);

				}else{
					//requestEnd();
					initCamera();
					welcom_imv.setVisibility(View.GONE);
				}
				break;

			}

		}
	};

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
					camera.connect();
					
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
	
	private void initview() {
		
		welcom_imv=(ImageView)findViewById(R.id.welcome_imv);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
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
			NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(HiDataValue.NOTICE_RUNNING_ID);
		notificationManager.cancel(HiDataValue.NOTICE_ALARM_ID);
		 
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

				if((System.currentTimeMillis()-exitTime) > 2000){  
				HiToast.showToast(getApplicationContext(), getString(R.string.press_again_to_exit));                               
	            exitTime = System.currentTimeMillis();   
	        } else {
	            finish();
	            System.exit(0);
	        }
			moveTaskToBack(true);

			break;
		case KeyEvent.KEYCODE_MENU:
			AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
			.setTitle(R.string.exit_the_project)

			.setMessage(getResources().getString(R.string.sure_to_exit))
			.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {   		    
				@Override   
				public void onClick(DialogInterface dialog, int which) {   
					MainActivity.this.finish();
				}   
			})
			.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {   		    
				@Override   
				public void onClick(DialogInterface dialog, int which) {   
					dialog.dismiss();
				}   
			})
			.create();        
			dialog.show();  

			break;
		}
		return true;
	}



}
*/