package com.thecamhi.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.hichip.base.HiLog;
import com.hichip.base.HiThread;
import com.hichip.callback.ICameraIOSessionCallback;
import com.hichip.callback.ICameraPlayStateCallback;
import com.hichip.content.HiChipDefines;
import com.hichip.control.HiCamera;
import com.hichip.tools.Packet;
import com.nxt.wly.R;
import com.thecamhi.base.HiToast;
import com.thecamhi.base.MyPlaybackGLMonitor;
import com.thecamhi.bean.HiDataValue;
import com.thecamhi.bean.MyCamera;
import com.thecamhi.main.HiActivity;

public class PlaybackOnlineActivity extends HiActivity implements ICameraIOSessionCallback,ICameraPlayStateCallback {

	//	private final static int HANDLE_MESSAGE_PLAY_START = 0x90000001;
	//	private final static int HANDLE_MESSAGE_PLAY_STATE = 0x90000001;

	private final static int HANDLE_MESSAGE_PROGRESSBAR_RUN = 0x90000002;
	private final static int HANDLE_MESSAGE_SEEKBAR_RUN=0x90000003;

	public static final short HI_P2P_PB_PLAY=1;
	public static final short HI_P2P_PB_STOP=2;
	public static final short HI_P2P_PB_PAUSE=3;
	public static final short HI_P2P_PB_SETPOS=4;
	public static final short HI_P2P_PB_GETPOS=5;

	private int video_width;
	private int video_height;

	private ProgressThread pthread = null;


	private ProgressBar prs_loading;
	private ImageView img_shade;

	private byte[] startTime;
	private MyPlaybackGLMonitor mMonitor;
	private MyCamera mCamera;
	private SeekBar prs_playing;


	private long playback_time;
	private long startTimeLong;
	private long endTimeLong;

	private int progressTime;

	private short model;//PLAY=1,STOP=2,PAUSE=3,SETPOS=4,GETPOS=5





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playback_online_landscape);

		Bundle bundle = this.getIntent().getExtras();
		String uid = bundle.getString(HiDataValue.EXTRAS_KEY_UID);
		byte[] b_startTime =  bundle.getByteArray("st");
		startTime = new byte[8]; 
		System.arraycopy(b_startTime, 0, startTime, 0, 8);
		playback_time =  bundle.getLong("pb_time");

		startTimeLong=bundle.getLong(VideoOnlineActivity.VIDEO_PLAYBACK_START_TIME);	
		endTimeLong=bundle.getLong(VideoOnlineActivity.VIDEO_PLAYBACK_END_TIME);	


		for(MyCamera camera: HiDataValue.CameraList) {
			if(camera.getUid().equals(uid)) {
				mCamera = camera;
				break;
			}
		}


		initView();
		showLoadingShade();


		mCamera.registerIOSessionListener(this);
		mCamera.registerPlayStateListener(this);
		mCamera.startPlayback(new HiChipDefines.STimeDay(startTime, 0),mMonitor);
	}



	private void initView() {
		/*	if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			setContentView(R.layout.activity_playback_online_portrait);


			TitleView nb = (TitleView)findViewById(R.id.title_top);

			nb.setButton(TitleView.NAVIGATION_BUTTON_LEFT);
//			nb.setButton(HiNavigationBar.NAVIGATION_BUTTON_RIGHT);
			nb.setTextTitle(getString(R.string.title_play_vidoe));
			nb.setNavigationBarButtonListener(new TitleView.NavigationBarButtonListener() {

				@Override
				public void OnNavigationButtonClick(int which) {
					// TODO Auto-generated method stub
					switch(which) {
					case TitleView.NAVIGATION_BUTTON_LEFT:
						finish();
						break;
					case TitleView.NAVIGATION_BUTTON_RIGHT:
						break;
					}
				}
			});
		}
		else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {*/
		//	setContentView(R.layout.activity_playback_online_landscape);
		//}

		mMonitor = (MyPlaybackGLMonitor)findViewById(R.id.monitor_playback_view);
		mCamera.setLiveShowMonitor(mMonitor);
		prs_loading = (ProgressBar)findViewById(R.id.prs_loading);
		img_shade = (ImageView)findViewById(R.id.img_shade);


		progressTime=(int) ((endTimeLong-startTimeLong)/1000);//default time between two frame is 500 Millisecond 
		prs_playing=(SeekBar)findViewById(R.id.prs_playing);
		prs_playing.setMax(progressTime); 
		prs_playing.setProgress(0);

		prs_playing.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int count=seekBar.getProgress();
				long time=count*1000;
				//	mCamera.stopPlayback();
				//	mCamera.startPlayback(new HiChipDefines.STimeDay(Packet.longToByteArray_Little(time), 0),mMonitor);
				
				mCamera.sendIOCtrl(HiChipDefines.HI_P2P_PB_POS_SET, 
						HiChipDefines.HI_P2P_PB_SETPOS_REQ.parseContent(0, (int)(count*100/progressTime),
								startTime));
				
				HiLog.e("channel="+0+"  time rate="+(int)(count*100/progressTime)+"  startTime=:"+
						Packet.getHex(startTime, startTime.length));
				
				/*mCamera.sendIOCtrl(HiChipDefines.HI_P2P_PB_PLAY_CONTROL,HiChipDefines.HI_P2P_S_PB_PLAY_REQ
						.parseContent(0, HI_P2P_PB_PAUSE, startTime));*/
				model=HI_P2P_PB_PAUSE;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {


			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {


			}
		});

	}

	private void showLoadingShade() {

		prs_loading.setMax(100);
		prs_loading.setProgress(10);
		pthread = new ProgressThread();
		pthread.startThread();
		//		mCamera.startLiveShow(1, mMonitor);
	}

	private void displayLoadingShade() {
		if(pthread != null)
			pthread.stopThread();
		pthread = null;
		prs_loading .setVisibility(View.GONE);
		img_shade.setVisibility(View.GONE);
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mCamera != null) {
			mCamera.startPlayback(new HiChipDefines.STimeDay(startTime, 0),mMonitor);
			mCamera.registerIOSessionListener(this);
			mCamera.registerPlayStateListener(this);
		}
	}


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mCamera != null) {
			mCamera.stopPlayback();
			mCamera.unregisterIOSessionListener(this);
			mCamera.unregisterPlayStateListener(this);
		}
	}



	private class ProgressThread extends HiThread {
		public void run() {
			while(isRunning) {
				sleep(100);
				Message msg = handler.obtainMessage();
				msg.what = HANDLE_MESSAGE_PROGRESSBAR_RUN;
				handler.sendMessage(msg);
			}
		}
	}


	/*	@Override
	public void callbackState(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		HiLog.e("flag:"+arg0+ "   width:" + arg1 + "   heigth:"+arg2);

		//		if(arg0 != ICameraPlayStateCallback.PLAY_STATE_START)
		//			return;
		Message msg = handler.obtainMessage();
		msg.what = arg0;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		handler.sendMessage(msg);
	}
	 */
	@Override
	public void receiveIOCtrlData(HiCamera camera, int arg1, byte[] arg2, int arg3) {
		// TODO Auto-generated method stub
		if(mCamera!=camera)return;

		Message msg=handler.obtainMessage();
		msg.what=arg1;
		handler.sendMessage(msg);

	}

	@Override
	public void receiveSessionState(HiCamera arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {

			case ICameraPlayStateCallback.PLAY_STATE_START:

				video_width = msg.arg1;
				video_height = msg.arg2;

				resetMonitorSize();
				prs_playing.setVisibility(View.VISIBLE);

				break;

			case ICameraPlayStateCallback.PLAY_STATE_EDN:
				HiToast.showToast(PlaybackOnlineActivity.this, "播放已停止");

				break;
			case ICameraPlayStateCallback.PLAY_STATE_POS:


				break;

			case HANDLE_MESSAGE_PROGRESSBAR_RUN:
			{	int cur = prs_loading.getProgress();
			//				HiLog.v("HANDLE_MESSAGE_PROGRESSBAR_RUN:"+cur);
			if(cur>=100) {
				prs_loading.setProgress(10);
			}
			else {
				prs_loading.setProgress(cur + 8);
			}
			}				
			break;

			case HANDLE_MESSAGE_SEEKBAR_RUN:
				//int count=prs_playing.getProgress();
				prs_playing.setProgress(msg.arg1); 


				break;
			case HiChipDefines.HI_P2P_PB_POS_SET:
				
				/*mCamera.sendIOCtrl(HiChipDefines.HI_P2P_PB_PLAY_CONTROL,
						HiChipDefines.HI_P2P_S_PB_PLAY_REQ.parseContent(0, HI_P2P_PB_PLAY, startTime));*/
			
				
				try {
					Thread.sleep(600); //每一帧的时间间隔是500毫秒
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				model=HI_P2P_PB_PLAY;
				
				break;


			}
		}
	};

	private void resetMonitorSize() {

		if(video_width==0 || video_height==0){
			return;
		}
		displayLoadingShade();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screen_width  = dm.widthPixels;
		int screen_height = dm.heightPixels;

		HiLog.e("screen_width" +screen_width + "   screen_height"+screen_height);

		/*if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

			WindowManager.LayoutParams wlp = getWindow().getAttributes();
            wlp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(wlp);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

			int width = screen_width;
			int height = (int)((float)width/((float)video_width/video_height));

			HiLog.e("width" +width + "   height"+height);

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					width, height);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
			lp.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);

			mMonitor.setLayoutParams(lp);

		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {*/

		WindowManager.LayoutParams wlp = getWindow().getAttributes();
		wlp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getWindow().setAttributes(wlp);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

		int width = screen_width;
		int height = screen_height;

		HiLog.e("width" +width + "   height"+height);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				width, height);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
		lp.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);

		mMonitor.setLayoutParams(lp);
		//	}

	}





	long oldTime;
	@Override
	public void callbackPlayUTC(HiCamera camera, int timeInteger) {
		if(mCamera!=camera||model==HI_P2P_PB_PAUSE)return;

	

		if(oldTime==0){
			oldTime=(long)timeInteger;
		}

		long sub=(long)timeInteger-oldTime;



		int step= (int) (sub/1000);
		Message msg = handler.obtainMessage();
		msg.what = HANDLE_MESSAGE_SEEKBAR_RUN;
		msg.arg1=step;
		handler.sendMessage(msg);

	}



	@Override
	public void callbackState(HiCamera camera, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		if(mCamera!=camera)return;
		HiLog.e("flag:"+camera+ "   width:" + arg1 + "   heigth:"+arg2);

		//		if(arg0 != ICameraPlayStateCallback.PLAY_STATE_START)
		//			return;
		Message msg = handler.obtainMessage();
		msg.what = arg1;
		msg.arg1 = arg2;
		msg.arg2 = arg3;
		handler.sendMessage(msg);

	}



}
