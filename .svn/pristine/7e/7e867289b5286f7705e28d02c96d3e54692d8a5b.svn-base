package com.nxt.wly.util;

import java.util.List;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.wly.SplashActivity;
import com.nxt.wly.WLYMainActivity;
import com.nxt.wly.entity.CameraInfo;
import com.nxt.wly.entity.CamerasInfo;
import com.thecamhi.bean.HiDataValue;
import com.thecamhi.bean.MyCamera;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;


/**
 * @author 摄像机更新 2016-11-30
 */
public class RosterTask extends AsyncTask<String, Void, String> {

	private Context context;
	private Util util;

	public RosterTask(Context context, BackUI backUI) {
		super();
		this.context = context;
		util=new Util(context);
		
	}

	
	@Override
	protected String doInBackground(String... params) {
		getcameras();
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	public interface BackUI {
		public void back(String result);
	}
	private void getcameras() {
		// TODO Auto-generated method stub
		AsyncHttpClient  client=new AsyncHttpClient ();
		//保存cookie，自动保存到了shareprefercece  
		PersistentCookieStore myCookieStore = new PersistentCookieStore(context);   
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
						camera = new MyCamera(cameras.get(i).getDev_nickname(), cameras.get(i).getDev_uid(), cameras.get(i).getDev_name(), cameras.get(i).getDev_pwd());
						camera.deleteInCameraList();
						camera.deleteInDatabase(context);
						camera.saveInDatabase(context);
//						camera.saveInCameraList();
					}
					Intent broadcast = new Intent();
					broadcast.setAction(HiDataValue.ACTION_CAMERA_INIT_END);
					context.sendBroadcast(broadcast);
				}else{
				}
			}
			@Override
			public void onFailure(Throwable error,
					String content) {
				super.onFailure(error, content);
			}
		});
	}
}
