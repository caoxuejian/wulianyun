//package com.nxt.wly.fragment;
//
//
//
//import java.io.File;
//
//import com.nxt.nxtapp.common.LogUtil;
//import com.nxt.nxtapp.common.Util;
//import com.nxt.wly.R;
//import com.nxt.wly.util.WebViewSwipeRefresh;
//import com.nxt.wly.util.WebViewWork;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.graphics.Bitmap;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Environment;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//import android.webkit.JsResult;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebSettings.RenderPriority;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
///**
// * 动态创建webview
// * webview下拉刷新
// * @author CaoXueJian
// *
// */
//public class MainFragment extends Fragment implements OnClickListener,OnRefreshListener {
//	private WebView wv;
//	private String title;
//	private String savekey;
//	private Util util;
//	private String main_url;
//	//	private RelativeLayout rl;
//	//	private ProgressBar pb;
//	private SwipeRefreshLayout sw;
//	private View view;
//	//	private ViewGroup mViewParent;
//	private ProgressBar mPageLoadingProgressBar;
//	private WebViewSwipeRefresh swref;
//	private RelativeLayout rl;
//	private String cacheDirPath;
//	private REFRESHReceiver refresh;
//	private NetworkInfo isNetWork;
//	private String curVersion;
//	private String cookie;
//	private FragmentActivity context;
//
//	@SuppressWarnings("deprecation")
//	@SuppressLint("NewApi")
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		context = getActivity();
//		util = new Util(context);
//		title=getArguments().getString("title");
//		main_url=getArguments().getString("URL");
//		savekey=main_url;
//
//
//		view =inflater.inflate(R.layout.webview_x5, null);
//		//		mViewParent = (ViewGroup) view.findViewById(R.id.webView1);
//		rl=new RelativeLayout(this.getActivity());
//		//下拉刷新
//		swref= new WebViewSwipeRefresh(this.getActivity());
//		//		swref=(WebViewSwipeRefresh)view.findViewById(R.id.swipe_container);
//		wv = new WebView(this.getActivity());
//		swref.setViewGroup(wv);
//		swref.setOnRefreshListener(this);
//		//进度条		
//		mPageLoadingProgressBar = new ProgressBar(this.getActivity(),null,android.R.attr.progressBarStyleHorizontal);
//		mPageLoadingProgressBar.setMax(100);
//		mPageLoadingProgressBar.setProgressDrawable(this.getResources()
//				.getDrawable(R.drawable.color_progressbar));
//		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,8); 
//		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE); 
//		mPageLoadingProgressBar.setLayoutParams(lp);
//
//		//		
//		//更新cookie
//		CookieSyncManager.createInstance(getActivity());
//		CookieManager cookieManager = CookieManager.getInstance();
//		cookieManager.removeAllCookie();
//		cookieManager.setCookie(main_url, "ci_session="+util.getFromSp("session_id", ""));
//		CookieSyncManager.getInstance().sync();
//		swref.addView(wv);
//		rl.addView(swref);
//		rl.addView(mPageLoadingProgressBar);
//		if(null!=savedInstanceState){
//			wv.restoreState(savedInstanceState);
//		}else{
//			wv.loadUrl(main_url);
//		}
//		wvsetting();
//		//聚合刷新
//		refresh=new REFRESHReceiver();
//		context.registerReceiver(refresh, new IntentFilter("refresh"));
//
//		return rl;
//	}
//	private void wvsetting() {
//		// TODO Auto-generated method stub
//		try {
//
//			curVersion = context.getPackageManager().getPackageInfo(
//					context.getPackageName(), 0).versionName;
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		wv.setWebViewClient(new MyWebViewClient());
//		wv.setWebChromeClient(new MyWebChromeClient());
//		//		wv.addJavascriptInterface(new JavaScriptInterface(), "ncp");
//		WebSettings setting = wv.getSettings();
//
//		//设置user agent
//		setting.setUserAgentString(setting.getUserAgentString()+" wulianyun/"+curVersion);
//
//
//		setting.setJavaScriptEnabled(true); 
//		setting.setRenderPriority(RenderPriority.HIGH); 
//		setting.setCacheMode(WebSettings.LOAD_DEFAULT);
//		// 开启 DOM storage API 功能 
//		setting.setDomStorageEnabled(true); 
//		//开启 database storage API 功能 
//		setting.setDatabaseEnabled(true);  
//		//开启 Application Caches 功能 
//		setting.setAppCacheEnabled(true); 
//		setting.setAllowFileAccess(true);
//		//		String cacheDirPath = getActivity().getFilesDir().getAbsolutePath()+"/webcache"; 
//		String cacheDirPath=getActivity().getFilesDir().getAbsolutePath()+"/webcache";
//		//	      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME; 
//		//设置数据库缓存路径 
//		setting.setDatabasePath(cacheDirPath); 
//		//设置  Application Caches 缓存目录 
//		setting.setAppCachePath(cacheDirPath); 
//	}
//	public void onRefresh() {  
//		//下拉重新加载  
//		//		wv.loadUrl(main_url); 
//		wv.reload();
//	} 
//	private class MyWebChromeClient extends WebChromeClient{
//		//更改进度条状态
//		@Override
//		public void onProgressChanged(WebView view, int newProgress) {
//			// TODO Auto-generated method stub
//			mPageLoadingProgressBar.setProgress(newProgress);
//			if (mPageLoadingProgressBar != null && newProgress != 100) {
//				swref.setRefreshing(false);
//				mPageLoadingProgressBar.setVisibility(View.VISIBLE);
//			} else if (mPageLoadingProgressBar != null) {
//				mPageLoadingProgressBar.setVisibility(View.GONE);
//			}
//			super.onProgressChanged(view, newProgress);
//		}
//		@Override
//		public void onReceivedTitle(WebView view, String title) {
//			// TODO Auto-generated method stub
//			super.onReceivedTitle(view, title);
//		}
//		@Override
//		public boolean onJsAlert(WebView view, String url, String message,
//				JsResult result) {
//			// TODO Auto-generated method stub
//			return super.onJsAlert(view, url, message, result);
//		}
//		@Override
//		public boolean onJsConfirm(WebView view, String url, String message,
//				final JsResult result) {
//			// TODO Auto-generated method stub
//			/*new AlertDialog.Builder(getActivity())
//			.setTitle("警告")
//			.setMessage(message)
//			.setPositiveButton("确定",
//					new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog,
//						int arg1) {
//					result.confirm();
//				}
//			}).setNegativeButton("取消", new DialogInterface.OnClickListener(){
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					result.cancel();
//				}
//
//			}).show();*/
//			return super.onJsConfirm(view, url, message, result);
//		}
//	}
//	private class MyWebViewClient extends WebViewClient {
//
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			//根据拦截到静态页面的URL作相应的操作
//			WebViewWork.executive(getActivity(),url,view);
//			return true;
//		}
//		@Override
//		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
//			// TODO Auto-generated method stub
//			return super.shouldOverrideKeyEvent(view, event);
//		}
//		@Override
//		public void onPageStarted(WebView view, String url, Bitmap favicon) {
//			super.onPageStarted(view, url, favicon);
//		}
//		@Override
//		public void onPageFinished(WebView view, String url) {
//			super.onPageFinished(view, url);
//
//			//			rl.removeView(pb);
//			//			//缓存本地数据库
//			//			savecache();
//
//		}
//		@Override
//		public void onReceivedError(WebView view, int errorCode,
//				String description, String failingUrl) {
//			// TODO Auto-generated method stub
//			super.onReceivedError(view, errorCode, description, failingUrl);
//			wv.setVisibility(View.GONE);
//			Toast.makeText(getActivity(), "请检查网络是否可用！", Toast.LENGTH_LONG).show();
//		}
//
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
//		getActivity().unregisterReceiver(refresh);
//		rl.removeAllViews();
//		wv.stopLoading();
//		wv.removeAllViews();
//		wv.destroy();
//		wv = null;
//		rl = null;
//		super.onDestroy();
//	}
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//
//	}
//	//刷新
//	public class REFRESHReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			wv.reload();
//		}
//
//	}
//}
