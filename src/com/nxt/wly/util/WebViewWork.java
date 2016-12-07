package com.nxt.wly.util;

import com.nxt.wly.WLYMainActivity;
import com.nxt.wly.WebViewActivity;
import com.nxt.wly.X5WebviewActivity;
import com.tencent.smtt.sdk.WebView;

import android.content.Context;
import android.content.Intent;

public class WebViewWork
{


	public static void executive(final Context context, String url,WebView view){
		System.out.println("@@@@@@@@@@@@@url"+url);
		if(url.contains("target=_blank")){
			if(url.contains("&target=_blank")){
				url=url.replaceAll("&target=_blank","");
			}else{
				url=url.replaceAll("target=_blank","");
			}
			if(url.contains("editnsjl")){
				Intent intent=new Intent();
				intent.setClass(context, WebViewActivity.class);
				intent.putExtra("webviewpath",Constans.ADDNSJL);
				context.startActivity(intent);
			}else{
				Intent x5webview = new Intent();
				x5webview.setClass(context, X5WebviewActivity.class);
				x5webview.putExtra("webviewpath", url);
				context.startActivity(x5webview);
			}
		}else if(url.contains("fards")){
			Intent ref = new Intent();
			ref.setAction("refresh");
			ref.putExtra("url", url);
			context.sendBroadcast(ref);
		}else{
			view.loadUrl(url);
			
		}
	}

}
