package com.nxt.wly;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nxt.nxtapp.AbsMainActivity;
import com.thecamhi.activity.VideoLocalActivity;
import com.thecamhi.activity.VideoOnlineActivity;
import com.thecamhi.bean.HiDataValue;
import com.thecamhi.bean.MyCamera;


/**
 * 视频
 * @author 曹学建
 *
 */

public class VideoActivity extends Activity implements OnClickListener {
	private static int LOCAL_VIDEO_MODEL=1;
	private static int ONLINE_VIDEO_MODEL=0;

	private PictureListAdapter pictureAdapter;
	private String[] state;
	private ImageView btn_local;
	private ImageView btn_online;
	private ImageView iv_back;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_video);
		initView();

	}

	private void initView() {
		ListView picture_fragment_camera_list=(ListView)findViewById(R.id.video_fragment_camera_list);
		pictureAdapter=new PictureListAdapter(VideoActivity.this);
		picture_fragment_camera_list.setAdapter(pictureAdapter);
		picture_fragment_camera_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				MyCamera selectedCamera =  HiDataValue.CameraList.get(position);
				Bundle extras = new Bundle();
				extras.putString(HiDataValue.EXTRAS_KEY_UID, selectedCamera.getUid());
				Intent intent = new Intent();
				intent.putExtras(extras);
				//如果本地则跳到本地录像界面，远程则跳到online录像
				if(HiDataValue.model==ONLINE_VIDEO_MODEL){

					intent.setClass(VideoActivity.this, VideoOnlineActivity.class);
				}else{
					intent.setClass(VideoActivity.this, VideoLocalActivity.class);
				}
				startActivity(intent);

			}
		});

		btn_local=(ImageView)findViewById(R.id.iv_local_video);
		btn_local.setOnClickListener(this);
		btn_online=(ImageView)findViewById(R.id.iv_online_video);
		btn_online.setOnClickListener(this);


		//		btn_online.setBackgroundResource(R.color.btn_bg_press);
		//		btn_local.setBackgroundResource(R.color.title_middle);

		selectModel(HiDataValue.model);

		state=getResources().getStringArray(R.array.connect_state);
		iv_back=(ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
	}


	public void selectModel(int model){

		if(model==LOCAL_VIDEO_MODEL){
			btn_online.setSelected(false);
			btn_local.setSelected(true);
			pictureAdapter.notifyDataSetChanged();

		}else if(model==ONLINE_VIDEO_MODEL){
			btn_online.setSelected(true);
			btn_local.setSelected(false);
			pictureAdapter.notifyDataSetChanged();

		}


	}







	protected class PictureListAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		//	public VideoListAdapter(LayoutInflater layoutInflater) {
		//		this.mInflater = layoutInflater;
		//	}

		public PictureListAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);

			//		this.mContext = context;
			//		this.mInflater = layoutInflater;
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
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final MyCamera cam = HiDataValue.CameraList.get(position);

			if (cam == null)
				return null;

			ViewHolder holder = null;

			if (convertView == null) {

				convertView = mInflater.inflate(R.layout.list_video_camera, null);

				holder = new ViewHolder();
				//			holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.txt_video_camera_nike = (TextView) convertView.findViewById(R.id.txt_video_camera_nike);
				holder.txt_video_camera_uid = (TextView) convertView.findViewById(R.id.txt_video_camera_uid);
				holder.txt_video_camera_state=(TextView)convertView.findViewById(R.id.txt_video_camera_state);
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			if(HiDataValue.model==ONLINE_VIDEO_MODEL){
				holder.txt_video_camera_state.setVisibility(View.VISIBLE);
				holder.txt_video_camera_state.setText(state[cam.getConnectState()]);
			}else{
				holder.txt_video_camera_state.setVisibility(View.GONE);
			}

			String uid=cam.getUid();

			if (holder != null) {

				holder.txt_video_camera_nike.setText(cam.getNikeName());
				holder.txt_video_camera_uid.setText(uid);
			}

			return convertView;

		}



		public final class ViewHolder {
			//		public ImageView img;
			public TextView txt_video_camera_nike;
			public TextView txt_video_camera_uid;
			public TextView txt_video_camera_state;
		}

	}




	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_local_video:
		{
			if(HiDataValue.model!=LOCAL_VIDEO_MODEL)
			{
				HiDataValue.model=LOCAL_VIDEO_MODEL;
				selectModel(HiDataValue.model);

			}
		}
		break;
		case R.id.iv_online_video:
		{
			if(HiDataValue.model!=ONLINE_VIDEO_MODEL)
			{

				HiDataValue.model=ONLINE_VIDEO_MODEL;
				selectModel(HiDataValue.model);
			}
		}
		break;
		case R.id.iv_back:
		{
			finish();
			
		}
		break;

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
