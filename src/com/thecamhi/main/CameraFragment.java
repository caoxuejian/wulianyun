package com.thecamhi.main;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hichip.base.HiLog;
import com.hichip.callback.ICameraIOSessionCallback;
import com.hichip.content.HiChipDefines;
import com.hichip.control.HiCamera;
import com.hichip.data.HiDeviceInfo;
import com.hichip.tools.Packet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.nxt.nxtapp.common.MD5;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.http.NxtRestClient;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.wly.DengLuActivity;
import com.nxt.wly.R;
import com.nxt.wly.WLYMainActivity;
import com.nxt.wly.entity.CameraInfo;
import com.nxt.wly.entity.CamerasInfo;
import com.nxt.wly.entity.LoginInfo;
import com.nxt.wly.util.Constans;
import com.thecamhi.activity.AddCameraActivity;
import com.thecamhi.activity.AliveSettingActivity;
import com.thecamhi.activity.EditCameraActivity;
import com.thecamhi.activity.LiveViewActivity;
import com.thecamhi.base.DatabaseManager;
import com.thecamhi.base.HiToast;
import com.thecamhi.base.TitleView;
import com.thecamhi.bean.HiDataValue;
import com.thecamhi.bean.MyCamera;
import com.thecamhi.widget.swipe.SwipeMenu;
import com.thecamhi.widget.swipe.SwipeMenuCreator;
import com.thecamhi.widget.swipe.SwipeMenuItem;
import com.thecamhi.widget.swipe.SwipeMenuListView;
import com.thecamhi.widget.swipe.SwipeMenuListView.OnMenuItemClickListener;

//摄像机fragment
public class CameraFragment extends ListFragment implements ICameraIOSessionCallback{
	private View layoutView;
	private static final int REQUEST_CODE_CAMERA_ADD = 0;
	private static final int REQUEST_CODE_CAMERA_EDIT = 1;
	private static final int REQUEST_CODE_CAMERA_LIVE_VIEW = 2;


	private static final int MOTION_ALARM=0;      //移动侦测
	private static final int IO_ALARM	=1; 	  //外置报警
	private static final int AUDIO_ALARM=2;       //声音报警
	private static final int UART_ALARM=3  ;      //外置报警

	private CameraListAdapter adapter;
	private CameraBroadcastReceiver receiver;
	private SwipeMenuListView listView;

	private long lastAlarmTime = 0;
	private String dev_uid;
	private Util util;
	private String uid;

	public interface OnButtonClickListener{
		void onButtonClick(int btnId,MyCamera camera);
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (receiver == null) {
			receiver = new CameraBroadcastReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(HiDataValue.ACTION_CAMERA_INIT_END);
			getActivity().registerReceiver(receiver, filter);
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {

		layoutView=inflater.inflate(R.layout.fragment_camera,null);
		initView();
		util=new Util(getActivity());
		return layoutView;
	}

	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		listView = (SwipeMenuListView)getListView();

		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(180);
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		listView.setMenuCreator(creator);

		// step 2. listener item click event
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {

				MyCamera camera = HiDataValue.CameraList.get(position);
				switch (index) {
				case 0:
					//从服务器删除
					deletefromserver(camera);
					
					break;
				}
			}

		
		});
	}
	private void deletefromserver(final MyCamera camera) {
		// TODO Auto-generated method stub
		dev_uid=camera.getUid();
		uid=util.getFromSp("uid", "");
		RequestParams rp = new RequestParams();
		rp.put("dev_uid", dev_uid);
		rp.put("uid",uid);
		rp.put("checkcode", MD5.makeMD5(dev_uid+"|"+uid+"|"+"deletecccodes"));
		NxtRestClient.post(Constans.DELETECAME, rp,
				new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				try {
					JSONObject backcon = new JSONObject(content);
					if("0".equals(backcon.getString("errorcode"))){
						Toast.makeText(getActivity(), backcon.getString("msg"), Toast.LENGTH_LONG).show();
						//这里左滑删除摄像头
						camera.disconnect();
						camera.deleteInCameraList();
						camera.deleteInDatabase(getActivity());
						adapter.notifyDataSetChanged();
					}else{
						Toast.makeText(getActivity(), backcon.getString("msg"), Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		});
	}
	private void initView() {

		TitleView title_top=(TitleView)
				layoutView.findViewById(R.id.title_top);
		title_top.setTitle(getString(R.string.title_camera_fragment));


		LinearLayout add_camera_ll=(LinearLayout)layoutView.findViewById(R.id.add_camera_ll);
		add_camera_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),AddCameraActivity.class);
				startActivityForResult(intent,REQUEST_CODE_CAMERA_ADD);
			}
		});


		adapter = new CameraListAdapter(getActivity());
		this.setListAdapter(adapter);
		adapter.notifyDataSetChanged();
		//设置右边设置按钮和编辑按钮的监听
		adapter.setOnButtonClickListener(new OnButtonClickListener() {

			@Override
			public void onButtonClick(int btnId, MyCamera camera) {
				switch (btnId) {
				case R.id.setting_camera_item://摄像机设置
				{

					if(camera.getConnectState()==HiCamera.CAMERA_CONNECTION_STATE_LOGIN){
						HiLog.v("BUTTON_SETTING.setOnClickListener");
						Intent intent = new Intent();
						intent.putExtra(HiDataValue.EXTRAS_KEY_UID, camera.getUid());
						intent.setClass(getActivity(), AliveSettingActivity.class);
						startActivity(intent);	
					}else{
						HiToast.showToast(getActivity(), getString(R.string.click_offline_setting));
					}
				}
				break;
				case R.id.img_edit:
					HiLog.v("BUTTON_EDIT.setOnClickListener");
					Intent intent = new Intent();
					intent.putExtra(HiDataValue.EXTRAS_KEY_UID, camera.getUid());
					intent.setClass(getActivity(), EditCameraActivity.class);
					startActivityForResult(intent,REQUEST_CODE_CAMERA_EDIT);

					break;


				default:
					break;
				}

			}
		});

	}

	//Item的点击监听
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		HiLog.v("onListItemClick:"+position);

		MyCamera selectedCamera =  HiDataValue.CameraList.get(position);

		//		if(isSetting) {
		//			editUserCamear(selectedCamera);
		//			return;
		//		}
		//		if(selectedCamera.isOnline()) {

		//如果不在线则连接，如果在线就进入监控画面
		if(selectedCamera.getConnectState()==HiCamera.CAMERA_CONNECTION_STATE_LOGIN){
			Bundle extras = new Bundle();
			extras.putString(HiDataValue.EXTRAS_KEY_UID, selectedCamera.getUid());

			Intent intent = new Intent();
			intent.putExtras(extras);
			intent.setClass(getActivity(), LiveViewActivity.class);
			startActivityForResult(intent, REQUEST_CODE_CAMERA_LIVE_VIEW);

			HiDataValue.isOnLiveView = true;
			selectedCamera.setAlarmState(0);

			adapter.notifyDataSetChanged();
		}else{
			selectedCamera.connect();
		}

	}


	//刷新界面的广播
	private class CameraBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if(intent.getAction().equals(HiDataValue.ACTION_CAMERA_INIT_END)) {
				if (adapter!=null) {
					adapter.notifyDataSetChanged();
				}

				for(MyCamera camera: HiDataValue.CameraList) {

					camera.registerIOSessionListener(CameraFragment.this);
				}
			}
		}

	}


	//camera的adapter
	public class CameraListAdapter extends BaseAdapter{
		Context context;
		private LayoutInflater mInflater;
		OnButtonClickListener mListener;

		public void setOnButtonClickListener(OnButtonClickListener listener) {
			mListener = listener;
		}

		public CameraListAdapter(Context context){

			mInflater=LayoutInflater.from(context);
			this.context=context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return HiDataValue.CameraList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return HiDataValue.CameraList.get(position);
		}

		@Override
		public long getItemId(int arg0) {

			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final MyCamera camera=HiDataValue.CameraList.get(position);
			if(camera==null){
				return null;
			}
			ViewHolder holder = null;
			if(convertView==null){
				convertView=mInflater.inflate(R.layout.camera_main_item, null);
				holder=new ViewHolder();
				holder.setting=(ImageView)convertView.findViewById(R.id.setting_camera_item);
				holder.img_snapshot=(ImageView)convertView.findViewById(R.id.snapshot_camera_item);
				holder.txt_nikename=(TextView)convertView.findViewById(R.id.nickname_camera_item);
				holder.txt_uid=(TextView)convertView.findViewById(R.id.uid_camera_item);
				holder.txt_state=(TextView)convertView.findViewById(R.id.state_camera_item);
				holder.img_alarm = (ImageView) convertView.findViewById(R.id.img_alarm);
				holder.img_edit=(ImageView)convertView.findViewById(R.id.img_edit);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			if(holder!=null){

				holder.img_snapshot.setImageBitmap(camera.snapshot);
				HiLog.e( "uid="+camera.getUid()+" snapshot==null:"+camera.snapshot==null?"true":"");
				holder.txt_nikename.setText(camera.getNikeName());
				holder.txt_uid.setText(camera.getUid());
				int state=camera.getConnectState();
				if(state>=0 && state<=4) {
					String str_state[] = context.getResources().getStringArray(R.array.connect_state);
					holder.txt_state.setText(str_state[state]);
				}

				holder.setting.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(mListener!=null){
							mListener.onButtonClick(R.id.setting_camera_item, camera);
						}

					}
				});

				holder.img_edit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(mListener!=null){
							mListener.onButtonClick(R.id.img_edit, camera);
						}

					}
				});

				if(camera.getAlarmState() == 0) {
					holder.img_alarm.setVisibility(View.GONE);
				}
				else {
					holder.img_alarm.setVisibility(View.VISIBLE);
				}
			}

			return convertView;
		}

		public class ViewHolder{
			public ImageView img_snapshot;
			public TextView txt_nikename;
			public TextView txt_uid;
			public TextView txt_state;
			public ImageView img_alarm;
			public	ImageView img_edit;
			public ImageView setting;

		}

	}



	//所有发送命令的回调接口receiveIOCtrlData
	@Override
	public void receiveIOCtrlData(HiCamera arg0, int arg1, byte[] arg2, int arg3) {
		if(arg1 == HiChipDefines.HI_P2P_GET_SNAP && arg3 == 0) {
			MyCamera camera = (MyCamera)arg0;
			if(!camera.reciveBmpBuffer(arg2)) {
				return;
			}
		}

		Bundle bundle = new Bundle();
		bundle.putByteArray(HiDataValue.EXTRAS_KEY_DATA, arg2);
		Message msg = handler.obtainMessage();
		msg.what = HiDataValue.HANDLE_MESSAGE_RECEIVE_IOCTRL;
		msg.obj = arg0;
		msg.arg1 = arg1;
		msg.arg2 = arg3;
		msg.setData(bundle);
		handler.sendMessage(msg);

	}

	@Override
	public void receiveSessionState(HiCamera arg0, int arg1) {
		Message msg = handler.obtainMessage();
		msg.what = HiDataValue.HANDLE_MESSAGE_SESSION_STATE;
		msg.arg1 = arg1;
		msg.obj = arg0;
		handler.sendMessage(msg);

	}
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			//摄像机登录的回调
			case HiDataValue.HANDLE_MESSAGE_SESSION_STATE:
				if(adapter!=null)
					adapter.notifyDataSetChanged();
				switch(msg.arg1) {
				case HiCamera.CAMERA_CONNECTION_STATE_DISCONNECTED:
					break;
				case HiCamera.CAMERA_CONNECTION_STATE_LOGIN:
					MyCamera camera=(MyCamera)msg.obj;
					setTime(camera);
					cameraLogin((MyCamera)msg.obj);
					HiLog.e("uid:"+camera.getUid());
					break;
				case HiCamera.CAMERA_CONNECTION_STATE_WRONG_PASSWORD:
					break;
				case HiCamera.CAMERA_CONNECTION_STATE_CONNECTING:
					break;
				}
				break;

			case HiDataValue.HANDLE_MESSAGE_RECEIVE_IOCTRL:
			{
				if(msg.arg2==0) {

					MyCamera camera = (MyCamera)msg.obj;

					switch (msg.arg1) {
					case HiChipDefines.HI_P2P_GET_SNAP:
						//						Bundle bundle = msg.getData();
						//						byte[] data = bundle.getByteArray("data");
						adapter.notifyDataSetChanged();


						DatabaseManager manager = new DatabaseManager(getActivity());

						//						byte[] buff = camera.getBmpBuffer();
						if (camera.snapshot != null) {
							manager.updateDeviceSnapshotByUID(camera.getUid(), camera.snapshot);
							//							Bitmap snapshot = null;
							//							snapshot = manager.getDeviceSnapshotByUid(uid);
							//							camera.Snapshot = snapshot;
						}else{
							HiLog.e("camera.snapshot =null");
						}


						break;

						//服务器直推的回调
					case HiChipDefines.HI_P2P_ALARM_EVENT:

						if(camera.getPushState()==0){
							return;
						}

						//相对摄像机时间的每30秒一次回调，
						if(System.currentTimeMillis() - camera.getLastAlarmTime() < 30000) {

							HiLog.e("Time lastAlarmTime:"+(System.currentTimeMillis() - lastAlarmTime));

							return;
						}

						camera.setLastAlarmTime(System.currentTimeMillis());

						Bundle bundle = msg.getData();
						byte[] data = bundle.getByteArray(HiDataValue.EXTRAS_KEY_DATA);
						HiChipDefines.HI_P2P_EVENT event = new HiChipDefines.HI_P2P_EVENT(data);

						//						showP2PPushMessage(camera.getUid(),event.u32Event);
						showAlarmNotification(camera,event.u32Event,event.u32Time);
						//						HiLog.v("alarm time:"+event.u32Time);
						saveAlarmData(camera,event.u32Event,event.u32Time);
						camera.setAlarmState(1);
						adapter.notifyDataSetChanged();
						break;
					}
				}
			}
			break;
			}
		}
	};

	//报警推送到通知栏
	@SuppressWarnings("deprecation")
	private void showAlarmNotification(MyCamera camera, int evtType, int evtTime) {

		try {

			NotificationManager manager = (NotificationManager)  getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

			Bundle extras = new Bundle();
			extras.putString(HiDataValue.EXTRAS_KEY_UID, camera.getUid());
			extras.putInt("type",1);


			Intent intent = new Intent(getActivity(), WLYMainActivity.class);
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.putExtras(extras);


			PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			//			Calendar cal = Calendar.getInstance();
			//			cal.setTimeZone(TimeZone.getDefault());
			//			cal.setTimeInMillis(evtTime);
			//			cal.add(Calendar.MONTH, 0);

			Notification notification = new Notification(R.drawable.ic_launcher, camera.getNikeName(),(long)evtTime*1000 );
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.flags |= Notification.FLAG_NO_CLEAR;

			notification.defaults = Notification.DEFAULT_ALL;

			String[] alarmList=getResources().getStringArray(R.array.tips_alarm_list_array);

			//			notification.setLatestEventInfo(this, camera.getNikeName(), "baby!", pendingIntent);
			notification.setLatestEventInfo(getActivity(), camera.getUid(),alarmList[evtType], pendingIntent);

			manager.notify(HiDataValue.NOTICE_ALARM_ID, notification);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void saveAlarmData(MyCamera camera, int evtType, int evtTime) {

		DatabaseManager manager = new DatabaseManager(getActivity());
		manager.addAlarmEvent(camera.getUid(),evtTime,evtType);


	}


	private void setTime(MyCamera camera){
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		cal.setTimeInMillis(System.currentTimeMillis());

		byte[] time = HiChipDefines.HI_P2P_S_TIME_PARAM.parseContent(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));


		camera.sendIOCtrl(HiChipDefines.HI_P2P_SET_TIME_PARAM, time);
	}

	private void cameraLogin(MyCamera camera) { 
		HiLog.v("mainactivity cameraLogin:"+camera.getUid());
		if(camera.isFirstLogin()) {
			camera.setFirstLogin(false);

			//	if(camera.getDeciveInfo()!=null){
			String info=Packet.getString(camera.getDeciveInfo().aszSystemName);
			//	}
			//海思 的快照在登录后获取，（国科的快照在进入监控界面退出时获取）
			if(camera.getChipVersion() == HiDeviceInfo.CHIP_VERSION_HISI)
				camera.sendIOCtrl(HiChipDefines.HI_P2P_GET_SNAP, HiChipDefines.HI_P2P_S_SNAP_REQ.parseContent(0,HiChipDefines.HI_P2P_STREAM_2));
		}

	}

}
