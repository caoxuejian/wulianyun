//package com.nxt.wly.fragment;
//
//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebResourceResponse;
//import android.webkit.WebSettings;
//import android.webkit.WebSettings.LayoutAlgorithm;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//
//import com.nxt.nxtapp.common.Util;
//import com.nxt.wly.R;
//
//
///**
// * 动态创建webview
// * 
// * @author 曹学建
// *
// */
//public class MainFragment2 extends Fragment implements OnClickListener {
//	private WebView wv;
//	private String title;
//	private String main_url;
//	private ProgressBar mPageLoadingProgressBar;
//	private ViewGroup mViewParent;
//	private RelativeLayout rl;
//	private Util util;
//	private static final int IMAGE_REQUEST_CODE = 0;
//	private static final int SELECT_PIC_KITKAT=1;
//	private ValueCallback<Uri> mFilePathCallback;
////	private REFRESHReceiver refresh;
//
//	@SuppressWarnings("deprecation")
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		title=getArguments().getString("title");
//		main_url=getArguments().getString("URL");
//		util=new Util(getActivity());
//
//		rl=new RelativeLayout(this.getActivity());
//		//进度条		
//		mPageLoadingProgressBar = new ProgressBar(this.getActivity(),null,android.R.attr.progressBarStyleHorizontal);
//		mPageLoadingProgressBar.setMax(100);
//		mPageLoadingProgressBar.setProgressDrawable(this.getResources()
//				.getDrawable(R.drawable.color_progressbar));
//		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,8); 
//		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE); 
//		mPageLoadingProgressBar.setLayoutParams(lp);
//
//		wv = new WebView(this.getActivity());
//		rl.addView(wv);
//		rl.addView(mPageLoadingProgressBar);
//
//
//
//		//		rl_progress=(RelativeLayout)view.findViewById(R.id.webview_pro);
//		//		wv=(WebView)view.findViewById(R.id.web_filechooser);
//		wv.setWebChromeClient(new MyWebChromeClient());
//		wv.setWebViewClient(new MyWebViewClient());
//		webviewsetting();
//		//更新cookie
//		CookieSyncManager.createInstance(getActivity());
//		CookieManager cookieManager = CookieManager.getInstance();
//		cookieManager.removeAllCookie();
//		cookieManager.setCookie(main_url, "ci_session="+util.getFromSp("session_id", ""));
//		CookieSyncManager.getInstance().sync();
//		/*refresh=new REFRESHReceiver();
//		getActivity().registerReceiver(refresh, new IntentFilter("refresh"));*/
//		if(null!=savedInstanceState){
//			wv.restoreState(savedInstanceState);
//		}else{
//			wv.loadUrl(main_url);
//		}
//
//		return rl;
//	}
//	private class MyWebChromeClient extends WebChromeClient{
//
//
//		public void onReceivedTitle(WebView view, String title) {
//		}
//		@Override
//		public void onProgressChanged(WebView view, int newProgress) {
//			// TODO Auto-generated method stub
//			mPageLoadingProgressBar.setProgress(newProgress);
//			if (mPageLoadingProgressBar != null && newProgress != 100) {
//				mPageLoadingProgressBar.setVisibility(View.VISIBLE);
//			} else if (mPageLoadingProgressBar != null) {
//				mPageLoadingProgressBar.setVisibility(View.GONE);
//			}
//		}
//
//	}
//
//	private class MyWebViewClient extends WebViewClient{
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			// TODO Auto-generated method stub
//			//根据拦截到静态页面的URL作相应的操作
//			return false;
//		}
//		@SuppressLint("NewApi") @Override
//		public WebResourceResponse shouldInterceptRequest(WebView view,
//				WebResourceRequest request) {
//			// TODO Auto-generated method stub
//
//			return super.shouldInterceptRequest(view, request);
//		}
//
//		@Override
//		public void onPageStarted(WebView arg0, String arg1, Bitmap arg2) {
//			// TODO Auto-generated method stub
//			super.onPageStarted(arg0, arg1, arg2);
//			//			rl_progress.setVisibility(View.VISIBLE);
//		}
//		@Override
//		public void onPageFinished(WebView view, String url) {
//			super.onPageFinished(view, url);
//			//			rl_progress.setVisibility(View.GONE);
//
//		}
//
//	}
//	private void webviewsetting() {
//		// TODO Auto-generated method stub
//		WebSettings webSetting = wv.getSettings();
//		webSetting.setAllowFileAccess(true);
//		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
//		webSetting.setSupportZoom(true);
//		webSetting.setBuiltInZoomControls(true);
//		webSetting.setUseWideViewPort(true);
//		webSetting.setSupportMultipleWindows(false);
//		webSetting.setLoadWithOverviewMode(true);
//		webSetting.setAppCacheEnabled(true);
//		webSetting.setDatabaseEnabled(true);
//		webSetting.setDomStorageEnabled(true);
//		webSetting.setJavaScriptEnabled(true);
//		webSetting.setGeolocationEnabled(true);
//		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
//		webSetting.setAppCachePath(getActivity().getDir("appcache", 0).getPath());
//		webSetting.setDatabasePath(getActivity().getDir("databases", 0).getPath());
//		webSetting.setGeolocationDatabasePath(getActivity().getDir("geolocation", 0)
//				.getPath());
//		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
//		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
//		webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
//	}
//
//	public void onRefresh() {  
//		//下拉重新加载  
//		//		wv.loadUrl(main_url); 
//		wv.reload();
//	} 
//
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//
//		super.onSaveInstanceState(outState);
//
//		wv.saveState(outState);
//	}
//
//	public void onBackPressed() {
//
//		if(wv.canGoBack())
//
//			wv.goBack();
//		else{
//			// Process.killProcess(Process.myPid());
//
//		}
//
//	}
//	public void onDestroy() {
//		wv.stopLoading();
//		wv.removeAllViews();
//		wv.destroy();
//		wv = null;
//		super.onDestroy();
//	}
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//
//	}
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, intent);
//		if(requestCode==SELECT_PIC_KITKAT){
//			if(mFilePathCallback!=null){
//				Uri result =intent.getData();
//				if (result != null) {
//					mFilePathCallback.onReceiveValue(result);
//				}else{
//					mFilePathCallback.onReceiveValue(result);
//				}
//
//			}else{
//				/* if (null == mFilePathCallbacks)
//					 return;
//				 Uri result = (intent == null ||requestCode != IMAGE_REQUEST_CODE) ? null: intent.getData();
//				 if (result != null) {
//					 mFilePathCallbacks.onReceiveValue(new Uri[]{result});
//				 } else {
//					 mFilePathCallbacks.onReceiveValue(new Uri[]{});
//				 }*/
//			}
//
//
//		}
//		mFilePathCallback=null;
//		//		mFilePathCallbacks = null;
//	}
//	/*//刷新
//	public class REFRESHReceiver extends BroadcastReceiver {
//
//		@SuppressWarnings("deprecation")
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			//更新cookie
//			System.out.println("@@@@@@@@@refres");
//			CookieSyncManager.createInstance(getActivity());
//			CookieManager cookieManager = CookieManager.getInstance();
//			cookieManager.removeAllCookie();
//			cookieManager.setCookie(intent.getStringExtra("url"), "ci_session="+util.getFromSp("session_id", ""));
//			CookieSyncManager.getInstance().sync();
//			wv.loadUrl(intent.getStringExtra("url"));
//		}
//
//	}*/
//}
