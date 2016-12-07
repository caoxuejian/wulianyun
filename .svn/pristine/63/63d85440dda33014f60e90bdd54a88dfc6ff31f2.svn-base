package com.nxt.wly.fragment;

import com.nxt.nxtapp.common.Util;
import com.nxt.wly.R;
import com.nxt.wly.util.WebViewWork;
import com.nxt.wly.x5view.X5WebView;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


/**
 * 动态创建webview
 * 
 * @author 曹学建
 *
 */
public class MainFragments extends Fragment implements OnClickListener {
	private X5WebView wv;
	private String title;
	private String main_url;
	private ProgressBar mPageLoadingProgressBar;
	private ViewGroup mViewParent;
	private RelativeLayout rl;
	private Util util;
	private ValueCallback<Uri[]> mFilePathCallbacks;
	private REFRESHReceiver refresh;
	public static final int REQUEST_FILE_PICKER = 1;
	public static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		title=getArguments().getString("title");
		main_url=getArguments().getString("URL");
		util=new Util(getActivity());

		rl=new RelativeLayout(this.getActivity());
		//进度条		
		mPageLoadingProgressBar = new ProgressBar(this.getActivity(),null,android.R.attr.progressBarStyleHorizontal);
		mPageLoadingProgressBar.setMax(100);
		mPageLoadingProgressBar.setProgressDrawable(this.getResources()
				.getDrawable(R.drawable.color_progressbar));
		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,8); 
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE); 
		mPageLoadingProgressBar.setLayoutParams(lp);

		wv = new X5WebView(getActivity());
		rl.addView(wv);
		rl.addView(mPageLoadingProgressBar);



		//		rl_progress=(RelativeLayout)view.findViewById(R.id.webview_pro);
		//		wv=(WebView)view.findViewById(R.id.web_filechooser);
		wv.setWebChromeClient(new MyWebChromeClient());
		wv.setWebViewClient(new MyWebViewClient());
		webviewsetting();
		//更新cookie
		CookieSyncManager.createInstance(getActivity());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		cookieManager.setCookie(main_url, "ci_session="+util.getFromSp("session_id", ""));
		CookieSyncManager.getInstance().sync();
		refresh=new REFRESHReceiver();
		getActivity().registerReceiver(refresh, new IntentFilter("refresh"));
		if(null!=savedInstanceState){
			wv.restoreState(savedInstanceState);
		}else{
			wv.loadUrl(main_url);
		}

		return rl;
	}
	private class MyWebChromeClient extends WebChromeClient{


		public boolean onShowFileChooser(X5WebView webView,
				ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
			// TODO Auto-generated method stub
			mFilePathCallbacks = filePathCallback;
			//			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			//			intent.addCategory(Intent.CATEGORY_OPENABLE);
			//			intent.setType("*/*");
			//			getActivity().startActivityForResult(Intent.createChooser(intent, "File Chooser"),
			//					REQUEST_FILE_PICKER);
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
			// TODO Auto-generated method stub
			super.openFileChooser(arg0, arg1, arg2);
			System.out.println("@@@@@@@@@@@openfile");
		}
		public void onReceivedTitle(X5WebView view, String title) {
		}
		public void onProgressChanged(X5WebView view, int newProgress) {
			// TODO Auto-generated method stub
			mPageLoadingProgressBar.setProgress(newProgress);
			if (mPageLoadingProgressBar != null && newProgress != 100) {
				mPageLoadingProgressBar.setVisibility(View.VISIBLE);
			} else if (mPageLoadingProgressBar != null) {
				mPageLoadingProgressBar.setVisibility(View.GONE);
			}
		}
	}

	private class MyWebViewClient extends WebViewClient{
		public boolean shouldOverrideUrlLoading(X5WebView view, String url) {
			// TODO Auto-generated method stub
			//根据拦截到静态页面的URL作相应的操作
			WebViewWork.executive(getActivity(), url, view);
			return true;
		}
		public WebResourceResponse shouldInterceptRequest(X5WebView view,
				WebResourceRequest request) {
			// TODO Auto-generated method stub

			return super.shouldInterceptRequest(view, request);
		}

		public void onPageStarted(X5WebView arg0, String arg1, Bitmap arg2) {
			// TODO Auto-generated method stub
			super.onPageStarted(arg0, arg1, arg2);
			//			rl_progress.setVisibility(View.VISIBLE);
		}
		public void onPageFinished(X5WebView view, String url) {
			super.onPageFinished(view, url);
			//			rl_progress.setVisibility(View.GONE);

		}

	}
	private void webviewsetting() {
		// TODO Auto-generated method stub
		WebSettings webSetting = wv.getSettings();
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
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setAppCachePath(getActivity().getDir("appcache", 0).getPath());
		webSetting.setDatabasePath(getActivity().getDir("databases", 0).getPath());
		webSetting.setGeolocationDatabasePath(getActivity().getDir("geolocation", 0)
				.getPath());
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
	}

	public void onRefresh() {  
		//下拉重新加载  
		//		wv.loadUrl(main_url); 
		wv.reload();
	} 

	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

//		wv.saveState(outState);
	}

	public void onBackPressed() {

		if(wv.canGoBack())

			wv.goBack();
		else{
			// Process.killProcess(Process.myPid());

		}

	}
	public void onDestroy() {
		wv.stopLoading();
		wv.removeAllViews();
		wv.destroy();
		wv = null;
		super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		Uri result = (intent == null || requestCode != FILECHOOSER_RESULTCODE_FOR_ANDROID_5) ? null: intent.getData();
		mFilePathCallbacks.onReceiveValue(new Uri[]{result});
	}
	//刷新
	public class REFRESHReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//更新cookie
//			CookieSyncManager.createInstance(getActivity());
//			CookieManager cookieManager = CookieManager.getInstance();
//			cookieManager.removeAllCookie();
//			cookieManager.setCookie(intent.getStringExtra("url"), "ci_session="+util.getFromSp("session_id", ""));
//			CookieSyncManager.getInstance().sync();
//			wv.loadUrl(intent.getStringExtra("url"));
			wv.reload();
		}

	}
}
