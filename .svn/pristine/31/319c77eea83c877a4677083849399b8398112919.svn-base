package com.nxt.wly;


import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nxt.nxtapp.common.Util;
import com.nxt.wly.x5view.X5WebView;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebChromeClient.FileChooserParams;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/*此处使用的是腾讯X5内核*/
public class X5WebviewActivity extends Activity implements android.view.View.OnClickListener{
	private String main_url;
	private ViewGroup mViewParent;
	private NetworkInfo isNetWork;
	private TextView tv_title;
	private ImageView iv_back;
	private TextView tv_close;
	private RelativeLayout categoryTitle;
	private WebView mWebView;
	private ProgressBar mPageLoadingProgressBar;
	private boolean mNeedTestPage = false;
	private String ci_session;
	private Util util;
	private ValueCallback<Uri[]> mFilePathCallbacks;
	public static final int REQUEST_FILE_PICKER = 1;
	public static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
	// 选择图片操作的返回值
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int SELECT_PIC_KITKAT=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SoftApplication appState = (SoftApplication) this.getApplication();
		appState.addActivity(this);
		util=new Util(this);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		main_url=getIntent().getStringExtra("webviewpath");
		ci_session=util.getFromSp("session_id", "");
		try {
			if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
				getWindow()
				.setFlags(
						android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
						android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		setContentView(R.layout.x5activity_main);
		mViewParent = (ViewGroup) findViewById(R.id.webView1);
		initview();
		/*//网络是否可用
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		isNetWork = cwjManager.getActiveNetworkInfo();
		if (!main_url.contains("?")) {

		}else{

		}*/
		QbSdk.preInit(this);

		this.webViewTransportTest();

		mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);// �ӳ�1.5s����webview

	}
	private void initview() {
		// TODO Auto-generated method stub
		tv_title=(TextView)findViewById(R.id.category_title);
		iv_back=(ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		tv_close=(TextView)findViewById(R.id.tv_close);
		categoryTitle=(RelativeLayout)findViewById(R.id.categoryTitle);
		tv_close.setOnClickListener(this);
	}
	private void webViewTransportTest(){
		X5WebView.setSmallWebViewEnabled(true);
	}

	private void initProgressBar() {
		mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);// new
		mPageLoadingProgressBar.setMax(100);
		mPageLoadingProgressBar.setProgressDrawable(this.getResources()
				.getDrawable(R.drawable.color_progressbar));
	}

	private void init() {

		mWebView=new WebView(this);
		//		mWebView = new X5WebView(this);
		mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.FILL_PARENT));
		initProgressBar();
		mWebView.setWebViewClient(new MyWebViewClient());

		mWebView.setWebChromeClient(new MyWebChromeClient());

		WebSettings webSetting = mWebView.getSettings();
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(false);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setJavaScriptEnabled(true);
		//		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		String cacheDirPath = getFilesDir().getAbsolutePath()+"/webcache"; 
		webSetting.setAppCachePath(cacheDirPath);
		webSetting.setDatabasePath(cacheDirPath);
		//		webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
		//				.getPath());
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		// webSetting.setPreFectch(true);
		//更新cookie
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		cookieManager.setCookie(main_url, "ci_session="+ci_session);
		//		cookieManager.setCookie("http://wly.365960.com/park/index.php/Cloud_app/addnsjl", "ci_session="+ci_session);
		CookieSyncManager.getInstance().sync();
		mWebView.loadUrl(main_url);
		//		CookieSyncManager.createInstance(this);
		//		CookieSyncManager.getInstance().sync();
	}

	private class MyWebViewClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			//根据拦截到静态页面的URL作相应的操作
			//			WebViewWork1.executive(X5WebviewActivity.this,url,view,msgApi);
			mWebView.loadUrl(url);
			return true;
		}
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				WebResourceRequest request) {
			// TODO Auto-generated method stub

			return super.shouldInterceptRequest(view, request);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?

		}

	}
	private class MyWebChromeClient extends WebChromeClient{

		@Override
		public boolean onShowFileChooser(WebView webView,
				ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
			// TODO Auto-generated method stub
			System.out.println("@@@@@@@@@onshow");
			mFilePathCallbacks = filePathCallback;
			Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
			contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
			contentSelectionIntent.setType("image/*");

			Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
			chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
			chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

			startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
			return true;
		}
		@Override
		public void openFileChooser(ValueCallback<Uri> arg0, String arg1,
				String arg2) {
			System.out.println("@@@@@@openfile");
			// TODO Auto-generated method stub
			super.openFileChooser(arg0, arg1, arg2);
			//Android4.4以上版本选择图片是会获取不到图片的路径
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {/**4.4系统以上**/
				Intent intent;
				intent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, SELECT_PIC_KITKAT);
			} else {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/jpeg");
				startActivityForResult(intent, IMAGE_REQUEST_CODE);
			}
		}
		public void onReceivedTitle(WebView view, String title) {
			tv_title.setText(title);
		}
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			mPageLoadingProgressBar.setProgress(newProgress);
			if (mPageLoadingProgressBar != null && newProgress != 100) {
				mPageLoadingProgressBar.setVisibility(View.VISIBLE);
			} else if (mPageLoadingProgressBar != null) {
				mPageLoadingProgressBar.setVisibility(View.GONE);


			}
		}
	}

	/* private class TestHandler extends */private Handler mTestHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_OPEN_TEST_URL:
				if (!mNeedTestPage) {
					return;
				}

				String testUrl = "file:///sdcard/outputHtml/html/"
						+ Integer.toString(mCurrentUrl) + ".html";
				if (mWebView != null) {
					mWebView.loadUrl(testUrl);
				}

				mCurrentUrl++;
				break;
			case MSG_INIT_UI:
				init();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView != null && mWebView.canGoBack()) {
				mWebView.goBack();
				return true;
			} else
				return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent == null || mWebView == null || intent.getData() == null)
			return;
		mWebView.loadUrl(intent.getData().toString());
	}

	@Override
	protected void onDestroy() {
		if (mWebView != null)
			mWebView.destroy();
		super.onDestroy();
	}
	public static final int MSG_OPEN_TEST_URL = 0;
	public static final int MSG_INIT_UI = 1;
	private final int mUrlStartNum = 0;
	private final int mUrlEndNum = 108;
	private int mCurrentUrl = mUrlStartNum;
	public static String titleImg;
	public static String atitle;
	public static String description;
	public static String shareUrl;
	public static String shareid;
	private View view;


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id==R.id.iv_back){
			if(mWebView.canGoBack()){
				mWebView.goBack();
			}else{
				finish();
			}
		}else if(id==R.id.tv_close){
			finish();
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if (null == mFilePathCallbacks)
			return;
		Uri result = (intent == null ||requestCode != FILECHOOSER_RESULTCODE_FOR_ANDROID_5) ? null: intent.getData();
		if (result != null) {
			mFilePathCallbacks.onReceiveValue(new Uri[]{result});
		} else {
			mFilePathCallbacks.onReceiveValue(new Uri[]{});
		}
		mFilePathCallbacks = null;
	}
	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation== this.getResources().getConfiguration().ORIENTATION_PORTRAIT){
			categoryTitle.setVisibility(View.VISIBLE);
		}
		if(newConfig.orientation== this.getResources().getConfiguration().ORIENTATION_LANDSCAPE){
			categoryTitle.setVisibility(View.GONE);
		}
	};

}
