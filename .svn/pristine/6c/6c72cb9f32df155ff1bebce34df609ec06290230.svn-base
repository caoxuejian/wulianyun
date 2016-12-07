package com.nxt.img;

/**
 * 相机，相册�?�择  发表
 */
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.nxt.img.adapter.GridImageAdapter;
import com.nxt.img.util.CommonDefine;
import com.nxt.img.util.FileUtils;
import com.nxt.img.util.ImageUtils;
import com.nxt.nxtapp.common.Util;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClient;
import com.nxt.nxtapp.http.NxtRestClientNew;
import com.nxt.nxtapp.json.JsonPaser;
import com.nxt.wly.AbsMainActivity;
import com.nxt.wly.R;
import com.nxt.wly.SoftApplication;
import com.nxt.wly.entity.DongTaiList;
import com.nxt.wly.entity.DynamicMsgListContent;
import com.nxt.wly.entity.LoginInfo;
import com.nxt.wly.imageutil.ImageZipUtil;
import com.nxt.wly.util.Constans;
import com.nxt.wly.util.PickDialog;
import com.nxt.wly.util.PickDialogListener;
import com.qiniu.auth.JSONObjectRet;
import com.qiniu.io.IO;
import com.qiniu.utils.QiniuException;
import com.thecamhi.activity.AddCameraActivity;

public class AlbumEditActivity extends AbsMainActivity implements OnClickListener{
	private EditText mETGroupPhotoContent;
	private String locationMsg;
	String objectKey = null;
	private GridView gridView;
	private ArrayList<String> dataList;
	private GridImageAdapter gridImageAdapter;
	private ArrayList<String> tDataList;
	private String photoContent;
	private String intranetID;
	private String cameraImagePath = "";
	private int finishCount = -1;
	private StringBuilder builder;
	private Uri uri;
	private String sort;
	private String title;
	private String shareimg;
	private String shareUrl;
	private ProgressDialog pdlogin;
	private int num=0;
	private Util utils;
	private String token;
	private List<Uri> url_mlist = new ArrayList<Uri>();
	private List listkey = new ArrayList();
	String key;
	String url = "";
	String urlkey = "";
	private Uri fileUri;
	private String upZipPath;
	private LinearLayout ll_cznr;
	private LinearLayout ll_qucj;
	ArrayList<String> bpname_list=new ArrayList<String>();
	ArrayList<String> bpid_list=new ArrayList<String>();
	ArrayList<String> operatename_list=new ArrayList<String>();
	ArrayList<String> operateid_list=new ArrayList<String>();
	private TextView tv_qucj;
	private TextView tv_cznr;
	private String operateid;
	private String bpid;

	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.activity_album_edit);
		RelativeLayout topRl = (RelativeLayout) findViewById(R.id.top_title);
		ImageView mTVCancel = (ImageView) topRl.findViewById(R.id.set_cancel);
		ImageView mTVOk = (ImageView) topRl.findViewById(R.id.set_ok);
		ll_cznr=(LinearLayout)findViewById(R.id.ll_cznr);//操作内容
		ll_qucj=(LinearLayout)findViewById(R.id.ll_ivqucj);//区域场景
		tv_qucj=(TextView)findViewById(R.id.tv_qucj);
		tv_cznr=(TextView)findViewById(R.id.tv_cznr);
		ll_cznr.setOnClickListener(this);
		ll_qucj.setOnClickListener(this);
		mTVOk.setVisibility(View.VISIBLE);
		mTVCancel.setVisibility(View.VISIBLE);
		mTVCancel.setOnClickListener(mCancelListener);
		mTVOk.setOnClickListener(mOkListener);
		sort = getIntent().getStringExtra("sort");
		title = getIntent().getStringExtra("title");
		shareimg = getIntent().getStringExtra("shareimg");
		shareUrl = getIntent().getStringExtra("shareUrl");

		pdlogin = new ProgressDialog(AlbumEditActivity.this);

		dataList = new ArrayList<String>();
		init();
		initdate();
		initListener();
		photoContent = mETGroupPhotoContent.getText().toString();

		Bundle extras = getIntent().getExtras();
		String path = extras.getString("path");

		tDataList = (ArrayList<String>)extras.getSerializable("dataList");
		String editContent = extras.getString("editContent");
		if(editContent != null){
			mETGroupPhotoContent.setText(editContent);
		}
		if(path != null) {
			dataList.add(path);
			if(dataList.size() < 9){
				dataList.add("camera_default");
			}
			gridImageAdapter.notifyDataSetChanged();
		}
		if (tDataList != null) {
			for (int i = 0; i < tDataList.size(); i++) {
				String string = tDataList.get(i);
				dataList.add(string);
			}
			if (dataList.size() < 9) {
				dataList.add("camera_default");
			}
			gridImageAdapter.notifyDataSetChanged();
		}
	}

	private void initdate() {
		// TODO Auto-generated method stub
		String camescene = utils.getFromSp("scene", "");
		String operate=utils.getFromSp("operate", "");
		try {
			JSONArray operates = new JSONArray(operate);
			JSONArray camerajs = new JSONArray(camescene);
			for(int i=0;i<camerajs.length();i++){
				JSONObject js = (JSONObject)camerajs.get(i);
				bpid_list.add(js.getString("bpid").trim());
				bpname_list.add(js.getString("bpname").trim());
			}
			operateid_list.add("-1");
			operatename_list.add("无操作");
			for(int i=0;i<operates.length();i++){
				JSONObject js = (JSONObject)operates.get(i);
				operateid_list.add(js.getString("id").trim());
				operatename_list.add(js.getString("name").trim());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		operateid=operateid_list.get(0);
		bpid=bpid_list.get(0);
		tv_qucj.setText(bpname_list.get(0));
		tv_cznr.setText(operatename_list.get(0));
	}

	private void init() {
		utils = new Util(this);
		mETGroupPhotoContent = (EditText) findViewById(R.id.group_camera_photo_content);
		gridView = (GridView) findViewById(R.id.gridview_image);
		//		dataList.add("camera_default");
		gridImageAdapter = new GridImageAdapter(this, dataList, loader, options);
		gridView.setAdapter(gridImageAdapter);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				num++;
				android.os.Message message = handler.obtainMessage();
				message.what = 3;
				handler.sendMessage(message);
				break;
			case 3:

				pdlogin.setMessage("正在上传第"+(num+1)+"张图片");
				pdlogin.show();
				// 上传文件
				IO.putFile(null, token, null, url_mlist.get(num), null,
						new JSONObjectRet() {

					private String key;

					public void onProcess(long current, long total) {

					};
					public void onSuccess(JSONObject resp) {
						key = resp.optString("key", "");
						if (key != null) {
							listkey.add(key);
						}
						if(num==(url_mlist.size()-1)){
							postPublicMessage();
						}else{
							android.os.Message message = handler
									.obtainMessage();
							message.what = 2;
							handler.sendMessage(message);

						}

					}

					public void onFailure(QiniuException ex) {
						ex.printStackTrace();
						android.os.Message message = handler
								.obtainMessage();
						message.what = 4;
						handler.sendMessage(message);
						/*util.showMsg(FaBiaoActivity.this,
								"网络不给力啊，检查下网络或�?�再试一次吧�??");
						pdlogin.cancel();*/
					}
				});
				break;
			case 4:
				// 获取upToken
				HttpCallBack callback = new HttpCallBack() {



					@Override
					public void onSuccess(String content) {
						// {"uploadToken":"vahlgVTVPR59i46tgrzZVlzybNF9S5CRMASH_RMS:pzmzowtU9T88B2NzqYBS4XsRHqs=:eyJzY29wZSI6Im5vbmd4bG9nIiwiZGVhZGxpbmUiOjE0MTE4NzExMTh9",
						// "errorcode":1,"expireSeconds":3600}
						// 在父类对content做了解密处理
						content = this.getContent(content);
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(content);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						token = jsonObject.optString("uploadToken",
								"");

						android.os.Message message = handler
								.obtainMessage();
						message.obj = token;
						message.what = 3;
						handler.sendMessage(message);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						int i = this.AutoLoading("uptoken", null, this);
						android.os.Message message = handler
								.obtainMessage();
						message.what = 4;
						handler.sendMessage(message);
					}
				};
				NxtRestClientNew.post("seven", null, callback);
				break;
			case 5:
				File file = new File(upZipPath );
				fileUri = Uri.fromFile(file);
				url_mlist.add(fileUri);
				break;
			}
		}
	};


	private void initListener() {

		gridView.setOnItemClickListener(new GridView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String path = dataList.get(position);
				if (path.contains("default") && position == dataList.size() -1 && dataList.size() -1 != 9) {
					showSelectImageDialog();
				} else {
					Intent intent = new Intent(mActThis, ImageDelActivity.class);
					intent.putExtra("position", position);
					intent.putExtra("path", dataList.get(position));
					startActivityForResult(intent, CommonDefine.DELETE_IMAGE);
				}
			}
		});
	}

	private OnClickListener mCancelListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final Dialog dialog = new Dialog(mActThis, R.style.dialog);
			View inflate = View.inflate(mActThis, R.layout.dialog_del, null);
			TextView dialogTitle = (TextView) inflate.findViewById(R.id.dialog_title);
			dialogTitle.setText("放弃此次编辑？");
			TextView dialogCancel = (TextView) inflate.findViewById(R.id.del_cancel);
			dialogCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			TextView dialogConfirm = (TextView) inflate.findViewById(R.id.confirm_del);
			dialogConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlbumEditActivity.this.finish();
				}
			});
			dialog.setContentView(inflate);
			dialog.show();
		}
	};

	private OnClickListener mOkListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			/*// TODO 发�?�，带图
			if((TextUtils.isEmpty(mETGroupPhotoContent.getText().toString()) && dataList.size() == 1)){

				Toast.makeText(AlbumEditActivity.this, "发布信息不能为空", Toast.LENGTH_SHORT).show();
			} else */
			if(dataList.size()==1){
				Toast.makeText(AlbumEditActivity.this, "图片不能为空", Toast.LENGTH_SHORT).show();
			}else{

				pdlogin.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pdlogin.setCancelable(false);
				pdlogin.setMessage("正在上传，请稍后...");
				pdlogin.show();
				if(dataList.size()==1){  //如果不�?�择图片的时�??
					postPublicMessage();
				}else{     //如果选择图片的时�??

					new Thread(){
						public void run() {
							for(String path:dataList){

								if(!path.contains("camera_default")){
									/*
									File file = new File(path);
									fileUri = Uri.fromFile(file);
									url_mlist.add(fileUri);*/
									upZipPath = ImageZipUtil.getzippath(path);
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									android.os.Message message = handler
											.obtainMessage();
									message.what = 5;
									handler.sendMessage(message);
								}

							}
							android.os.Message message = handler
									.obtainMessage();
							message.what = 4;
							handler.sendMessage(message);
						};
					}.start();

				}  


			}
		}
	};
	private PickDialog pickDialog;


	// 异步提交发布信息
	private void postPublicMessage() {
		RequestParams rp = new RequestParams();
		rp.put("bpid", bpid);
		rp.put("uid", utils.getFromSp("uid", ""));
		rp.put("controlid",operateid);
		rp.put("msg",  mETGroupPhotoContent.getText().toString());
		rp.put("baid", utils.getFromSp("baid", ""));
		if (listkey.size()!=0) {
			for (int i = 0; i < listkey.size(); i++) {
				if (i == 0) {
					url = (String) listkey.get(i);
				} else {
					url = "," + (String) listkey.get(i);
				}
				urlkey = urlkey + url;
			}
		}
		rp.put("img", urlkey);
		//		if(shareUrl!=null){
		//			rp.put("urlLink", shareUrl);
		//		}
		//util.showMsg(getApplicationContext(), "img:"+urlkey);
		AsyncHttpClient  client=new AsyncHttpClient ();
		//保存cookie，自动保存到了shareprefercece  
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);   
		client.setCookieStore(myCookieStore);
		client.post(Constans.POSTNSJL, rp, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				try {
					JSONObject backcon = new JSONObject(content);
					if("0".equals(backcon.getString("errorcode"))){
						utils.showMsg(AlbumEditActivity.this, "发表成功");
						Intent ref = new Intent();
						ref.setAction("refresh");
						sendBroadcast(ref);
						pdlogin.cancel();
					}else{
						utils.showMsg(AlbumEditActivity.this,backcon.getString("msg"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				finish();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				utils.showMsg(getApplicationContext(), "发表失败");
				pdlogin.cancel();
			}
		});
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			final Dialog dialog = new Dialog(this, R.style.dialog);
			View inflate = View.inflate(this, R.layout.dialog_del, null);
			TextView dialogTitle = (TextView) inflate.findViewById(R.id.dialog_title);
			dialogTitle.setText("放弃此次编辑？");
			TextView dialogCancel = (TextView) inflate.findViewById(R.id.del_cancel);
			dialogCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			TextView dialogConfirm = (TextView) inflate.findViewById(R.id.confirm_del);
			dialogConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlbumEditActivity.this.finish();
				}
			});
			dialog.setContentView(inflate);
			dialog.show();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CommonDefine.TAKE_PICTURE_FROM_CAMERA:
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
					return;
				}
				Bitmap bitmap = ImageUtils.getUriBitmap(this, uri, 400, 400);
				String cameraImagePath = FileUtils.saveBitToSD(bitmap, System.currentTimeMillis()+"");

				//				Bundle bundle = data.getExtras();
				//				Bitmap bitmap = (Bitmap) bundle.get("data");
				//				String cameraImagePath = ImageUtils.setCameraImage(bitmap);

				for (int i = 0; i < dataList.size(); i++) {
					String path = dataList.get(i);
					if(path.contains("default")) {
						dataList.remove(dataList.size()-1);
					}
				}
				dataList.add(cameraImagePath);
				if(dataList.size() < 9) {
					dataList.add("camera_default");
				}
				gridImageAdapter.notifyDataSetChanged();
				break;
			case CommonDefine.TAKE_PICTURE_FROM_GALLERY:
				Bundle bundle2 = data.getExtras();
				tDataList = (ArrayList<String>) bundle2.getSerializable("dataList");
				if (tDataList != null) {
					for (int i = 0; i < tDataList.size(); i++) {
						String string = tDataList.get(i);
						dataList.add(string);
					}
					if (dataList.size() < 9) {
						dataList.add("camera_default");
					}
					gridImageAdapter.notifyDataSetChanged();
				}

				break;
			case CommonDefine.DELETE_IMAGE:
				int position = data.getIntExtra("position", -1);
				dataList.remove(position);
				if(dataList.size() < 9 ) {
					dataList.add(dataList.size(), "camera_default");
					for (int i = 0; i < dataList.size(); i++) {
						String path = dataList.get(i);
						if(path.contains("default")) {
							dataList.remove(dataList.size() - 2);
						}
					}
				}
				gridImageAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	// 选择相册，相�??
	private void showSelectImageDialog() {
		final Dialog picAddDialog = new Dialog(mActThis, R.style.dialog);
		View picAddInflate = View.inflate(mActThis, R.layout.item_dialog_camera, null);
		TextView camera = (TextView) picAddInflate.findViewById(R.id.camera);
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// 选择相机
				Intent cameraIntent = new Intent();
				cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
				// 根据文件地址创建文件
				File file = new File(CommonDefine.FILE_PATH);
				if (file.exists()) {
					file.delete();
				}
				uri = Uri.fromFile(file);
				// 设置系统相机拍摄照片完成后图片文件的存放地址
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

				// �??启系统拍照的Activity
				startActivityForResult(cameraIntent, CommonDefine.TAKE_PICTURE_FROM_CAMERA);
				picAddDialog.dismiss();
			}
		});
		TextView mapStroge = (TextView) picAddInflate.findViewById(R.id.mapstorage);
		mapStroge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// 选择图库
				Intent intent = new Intent(mActThis, AlbumActivity.class);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("dataList", getIntentArrayList(dataList));
				bundle.putString("editContent", mETGroupPhotoContent.getText().toString());
				bundle.putString("sort", sort);
				intent.putExtras(bundle);
				startActivityForResult(intent, CommonDefine.TAKE_PICTURE_FROM_GALLERY);

				picAddDialog.dismiss();
				AlbumEditActivity.this.finish();
			}
		});
		TextView cancel = (TextView) picAddInflate.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				picAddDialog.dismiss();
			}
		});
		picAddDialog.setContentView(picAddInflate);
		picAddDialog.show();

	}

	private ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {

		ArrayList<String> tDataList = new ArrayList<String>();

		for (String s : dataList) {
			if (!s.contains("default")) {
				tDataList.add(s);
			}
		}
		return tDataList;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.ll_cznr://操作内容
			selectscene(operatename_list,getString(R.string.select_cznr),2);
			break;
		case R.id.ll_ivqucj://区域场景
			selectscene(bpname_list,getString(R.string.select_changjing),1);
			break;
		default:
			break;
		}
	}
	private void selectscene(ArrayList<String> arraylist, String title, final int i) {
		// TODO Auto-generated method stub


		pickDialog=new PickDialog(AlbumEditActivity.this, title, new PickDialogListener() {

			@Override
			public void onListItemClick(int position, String string) {
				// TODO Auto-generated method stub
				if(i==1){
					tv_qucj.setText(bpname_list.get(position));
					bpid=bpid_list.get(position);
				}else{
					tv_cznr.setText(operatename_list.get(position));
					operateid=operateid_list.get(position);
				}
			}
			@Override
			public void onLeftBtnClick() {
				// TODO Auto-generated method stub
			}
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
			}
			@Override
			public void onRightBtnClick() {
				// TODO Auto-generated method stub

			}
			@Override
			public void onListItemLongClick(int position, String string) {
				// TODO Auto-generated method stub

			}
		});
		pickDialog.show();
		pickDialog.initListViewData(arraylist);
	}
}
