package com.nxt.wly;


import java.io.File;

import com.nxt.nxtapp.common.Util;
import com.nxt.wly.util.WebViewSwipeRefresh;
import com.nxt.wly.util.WebViewWork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 动态创建webview
 * webview下拉刷新
 * @author CaoXueJian
 *
 */
public class WebViewActivity extends Activity implements OnClickListener,OnRefreshListener {
	private WebView wv;
	private Util util;
	private String main_url;
	private SwipeRefreshLayout sw;
	private ProgressBar mPageLoadingProgressBar;
	private RelativeLayout rl;
	private String cacheDirPath;
	private TextView tv_title;
	private PopupWindow pw;
	private NetworkInfo isNetWork;
	private RelativeLayout categoryTitle;
	private WindowManager mWm;

	private String ci_session;
	private WebViewSwipeRefresh swref;
	private ValueCallback<Uri> mFilePathCallback;
	private ValueCallback<Uri[]> mFilePathCallbacks;
	private int curVersion;
	private LayoutInflater mLayoutInflater;
	public static final int REQUEST_FILE_PICKER = 1;
	public static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
	// 选择图片操作的返回值
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int SELECT_PIC_KITKAT=1;
	private String camepath=Environment.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator+ "jnb" + File.separator + "jnbpic_camera";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.webview_x5);
		util = new Util(this);
		main_url=getIntent().getStringExtra("webviewpath");
		ci_session=util.getFromSp("session_id", "");
		tv_title=(TextView)findViewById(R.id.category_title);
		categoryTitle=(RelativeLayout)findViewById(R.id.categoryTitle);

		mWm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		mLayoutInflater = LayoutInflater.from(this);

		rl=(RelativeLayout) findViewById(R.id.webView1);
		//下拉刷新
		swref= new WebViewSwipeRefresh(getApplicationContext());
		//进度条		
		mPageLoadingProgressBar = new ProgressBar(WebViewActivity.this,null,android.R.attr.progressBarStyleHorizontal);
		mPageLoadingProgressBar.setMax(100);
		mPageLoadingProgressBar.setProgressDrawable(this.getResources()
				.getDrawable(R.drawable.color_progressbar));
		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,8); 
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE); 
		mPageLoadingProgressBar.setLayoutParams(lp);
		//设置webview
		wvsetting();


		swref.addView(wv);
		rl.addView(swref);
		//		rl.addView(wv);
		rl.addView(mPageLoadingProgressBar);
		//更新cookie
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		cookieManager.setCookie(main_url, "ci_session="+util.getFromSp("session_id", ""));
		CookieSyncManager.getInstance().sync();
		if(null!=savedInstanceState){
			wv.restoreState(savedInstanceState);
		}else{
			wv.loadUrl(main_url);
		}
	}
	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" }) private void wvsetting() {
		// TODO Auto-generated method stub
		wv = new WebView(WebViewActivity.this);
		//		wv.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		swref.setViewGroup(wv);
		swref.setOnRefreshListener(this);

		//		wv.addJavascriptInterface(new playJavaScriptInterface(), "startplay");
		wv.setWebViewClient(new MyWebViewClient());
		//		wv.setWebChromeClient(new WebChromeClient());
		wv.setWebChromeClient(new MyWebChromeClient());
		WebSettings setting = wv.getSettings();

		setting.setUseWideViewPort(false);
		setting.setJavaScriptEnabled(true); 
		setting.setPluginState(WebSettings.PluginState.ON);
		setting.setJavaScriptCanOpenWindowsAutomatically(true);
		setting.setBlockNetworkImage(false);
		setting.setRenderPriority(RenderPriority.HIGH); 
		setting.setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式 
		// 开启 DOM storage API 功能 
		setting.setDomStorageEnabled(true); 
		//开启 database storage API 功能 
		setting.setDatabaseEnabled(true);  
		String cacheDirPath = getFilesDir().getAbsolutePath()+"/webcache"; 
		//	      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME; 
		//设置数据库缓存路径 
		setting.setDatabasePath(cacheDirPath); 
		//设置  Application Caches 缓存目录 
		setting.setAppCachePath(cacheDirPath); 
		//开启 Application Caches 功能 
		setting.setAppCacheEnabled(true); 
		setting.setBuiltInZoomControls(false);
		setting.setSupportZoom(false);
		setting.setDisplayZoomControls(false);
		setting.setAllowFileAccess(true);   // 可以读取文件缓存(manifest生效)
		//设置WebView是否通过手势触发播放媒体，默认是true，需要手势触发。
		//		setting.setMediaPlaybackRequiresUserGesture(true);

	}

	public void onRefresh() {  
		//下拉重新加载  
		wv.loadUrl(main_url);  
	} 

	private class MyWebChromeClient extends WebChromeClient{


		//4.1
		public void openFileChooser(ValueCallback<Uri> filePathCallback, String arg1,
				String arg2) {
			// TODO Auto-generated method stub
			System.out.println("@@@@@@@@@open");
			mFilePathCallback= filePathCallback;
			//Android4.4以上版本选择图片是会获取不到图片的路径
			showSelectImageDialog();
			/*
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {*//**4.4系统以上**//*
				Intent intent;
				intent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, IMAGE_REQUEST_CODE);
			} else {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/jpeg");
				startActivityForResult(intent, IMAGE_REQUEST_CODE);
			}*/
		}
		//3.0+
		public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
			mFilePathCallback=uploadMsg;
			System.out.println("@@@@@@@@@open2");
			showSelectImageDialog();
			//			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			//			i.addCategory(Intent.CATEGORY_OPENABLE);
			//			i.setType("*/*");
			//			startActivityForResult(
			//					Intent.createChooser(i, "File Browser"),
			//					IMAGE_REQUEST_CODE);
		}
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			mFilePathCallback = uploadMsg;
			System.out.println("@@@@@@@@@open3");
			showSelectImageDialog();
			//			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			//			i.addCategory(Intent.CATEGORY_OPENABLE);
			//			i.setType("*/*");
			//			startActivityForResult(Intent.createChooser(i, "File Chooser"), IMAGE_REQUEST_CODE);
		}
		//5.0+
		@SuppressLint("NewApi") @Override
		public boolean onShowFileChooser(WebView webView,
				ValueCallback<Uri[]> filePathCallback,
				FileChooserParams fileChooserParams) {
			// TODO Auto-generated method stub
			System.out.println("@@@@@@@@@onshow");
			mFilePathCallbacks = filePathCallback;
			showSelectImageDialog();
			/*Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
			contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
			contentSelectionIntent.setType("image/*");

			Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
			chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
			chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

			startActivityForResult(chooserIntent, IMAGE_REQUEST_CODE);*/
			return true;
		}

		//更改进度条状态
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
			mPageLoadingProgressBar.setProgress(newProgress);
			if (mPageLoadingProgressBar != null && newProgress != 100) {
				swref.setRefreshing(false);
				mPageLoadingProgressBar.setVisibility(View.VISIBLE);
			} else if (mPageLoadingProgressBar != null) {
				mPageLoadingProgressBar.setVisibility(View.GONE);
			}
		}
		@Override
		public void onReceivedTitle(WebView view, String title) {
			// TODO Auto-generated method stub
			super.onReceivedTitle(view, title);
			tv_title.setText(((title != null && !title.equals("")) ? title : "详细信息"));
		}
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			// TODO Auto-generated method stub
			//			new AlertDialog.Builder(WebViewActivity.this).setTitle("删除").setMessage(message)

			return super.onJsAlert(view, url, message, result);
		}
		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {
			// TODO Auto-generated method stub
			return super.onJsConfirm(view, url, message, result);
		}

	}
	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			//根据拦截到静态页面的URL作相应的操作
			//			WebViewWork.executive(WebViewActivity.this,url,view);
			Intent ref = new Intent();
			ref.setAction("refresh");
			ref.putExtra("url", url);
			sendBroadcast(ref);
			finish();
			return true;
		}
		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			// TODO Auto-generated method stub
			return super.shouldOverrideKeyEvent(view, event);
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			wv.setVisibility(View.GONE);
			Toast.makeText(WebViewActivity.this, "请检查网络是否可用！", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

		wv.saveState(outState);
	}

	public void onBackPressed() {

		if(wv.canGoBack())

			wv.goBack();
		else{
			// Process.killProcess(Process.myPid());

		}

	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation== this.getResources().getConfiguration().ORIENTATION_PORTRAIT){
			categoryTitle.setVisibility(View.VISIBLE);
		}
		if(newConfig.orientation== this.getResources().getConfiguration().ORIENTATION_LANDSCAPE){
			categoryTitle.setVisibility(View.GONE);
		}

	}
	public void onDestroy() {
		rl.removeAllViews();
		wv.stopLoading();
		wv.removeAllViews();
		wv.destroy();
		wv = null;
		rl = null;
		finish();
		super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id==R.id.iv_back){
			if(wv.canGoBack()){
				wv.goBack();
			}else{
				finish();
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		System.out.println("@@@@@@@@@@@@onactivityresult"+requestCode+"@"+resultCode);
		if(requestCode==IMAGE_REQUEST_CODE){
			if(mFilePathCallback!=null){
				Bundle u = intent.getExtras();
				Bitmap bitmap = (Bitmap)u.get("data");
				Uri result = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));      
				System.out.println("@@@@@@@@@@@@SELECT_PIC_KITKAT"+result);
				if (result != null) {
					mFilePathCallback.onReceiveValue(result);
				}else{
					mFilePathCallback.onReceiveValue(result);
				}

			}else{
				if (null == mFilePathCallbacks)
					return;
				Bundle u = intent.getExtras();
				Bitmap bitmap = (Bitmap)u.get("data");
				Uri result = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));      
				
				if (result != null) {
					mFilePathCallbacks.onReceiveValue(new Uri[]{result});
				} else {
					mFilePathCallbacks.onReceiveValue(new Uri[]{});
				}
			}


		}else if(requestCode==SELECT_PIC_KITKAT){
			if(mFilePathCallback!=null){
				Uri result=intent.getData();
				if (result != null) {
					mFilePathCallback.onReceiveValue(result);
				}else{
					mFilePathCallback.onReceiveValue(result);
				}

			}else{
				if (null == mFilePathCallbacks)
					return;
				Uri result=intent.getData();
				if (result != null) {
					mFilePathCallbacks.onReceiveValue(new Uri[]{result});
				} else {
					mFilePathCallbacks.onReceiveValue(new Uri[]{});
				}
			}
		}
		mFilePathCallback=null;
		mFilePathCallbacks = null;
	}
	// 选择相册，相机
	private void showSelectImageDialog() {
		final Dialog picAddDialog = new Dialog(this, R.style.dialog);
		View picAddInflate = View.inflate(this, R.layout.item_dialog_camera, null);
		TextView camera = (TextView) picAddInflate.findViewById(R.id.camera);
		camera.setOnClickListener(new OnClickListener() {

			private Uri uri;

			@Override
			public void onClick(View v) {// 选择相机
				Intent cameraIntent = new Intent();
				cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
				// 根据文件地址创建文件
				File file = new File(camepath);
				if (file.exists()) {
					file.delete();
				}
				uri = Uri.fromFile(file);

				// 开启系统拍照的Activity
				startActivityForResult(cameraIntent, IMAGE_REQUEST_CODE);
				picAddDialog.dismiss();
			}
		});
		TextView mapStroge = (TextView) picAddInflate.findViewById(R.id.mapstorage);
		mapStroge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// 选择图库
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/jpeg");
				startActivityForResult(intent, SELECT_PIC_KITKAT);

				picAddDialog.dismiss();
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
}
