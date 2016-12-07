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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.hichip.base.HiLog;
import com.nxt.nxtapp.AbsMainActivity;
import com.thecamhi.activity.LocalPictureActivity;
import com.thecamhi.base.HiToast;
import com.thecamhi.base.TitleView;
import com.thecamhi.bean.HiDataValue;
import com.thecamhi.bean.MyCamera;



/**
 * 照片
 * @author 曹学建
 *
 */

public class PictureActivity extends Activity implements OnClickListener {

	private String appname;
	private ImageView iv_back;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_picture);
		initView();

	}

	private void initView() {
		//		TitleView title=(TitleView)findViewById(R.id.category_title);
		//		title.setTitle(getResources().getString(R.string.title_picture_fragment));

		appname = getResources().getString(R.string.app_name);

		ListView picture_fragment_camera_list=(ListView)findViewById(R.id.picture_fragment_camera_list);
		picture_fragment_camera_list.setAdapter(new PictureListAdapter(this));
		picture_fragment_camera_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				MyCamera selectedCamera =  HiDataValue.CameraList.get(position);
				if(getFileCount(selectedCamera.getUid())==0){
					HiToast.showToast(PictureActivity.this, getString(R.string.tips_no_picture));
					return;
				}
				//if(radio_group_video.getCheckedRadioButtonId() == R.id.radio_client) {
				HiLog.v(" R.id.radio_client");

				Bundle extras = new Bundle();
				extras.putString(HiDataValue.EXTRAS_KEY_UID, selectedCamera.getUid());
				Intent intent = new Intent();
				intent.putExtras(extras);
				intent.setClass(PictureActivity.this, LocalPictureActivity.class);
				startActivity(intent);

			}
		});
		iv_back=(ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
	}

	//获取图片数量，
	public int  getFileCount(String uid) {
		String path=Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/"+ appname + "/Snapshot/"+uid+"/";
		File folder = new File(path);
		String[] imageFiles = folder.list();


		if(imageFiles==null){
			return 0;
		}
		int sum=0;
		sum=imageFiles.length;

		return sum;
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
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
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
		}

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
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
