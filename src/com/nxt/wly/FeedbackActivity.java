package com.nxt.wly;
/*
 * 意见反馈界面
 */
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import com.nxt.nxtapp.common.LogUtil;
import com.nxt.nxtapp.http.HttpCallBack;
import com.nxt.nxtapp.http.NxtRestClientNew;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends Activity {
	private EditText feedback_edit;
	private TextView feedback_device;
	private TextView feedback_android;
	private TextView feedback_appName;
	private TextView feedback_appVersion;
	private String feedback_content;
	private ProgressDialog dialog;
	private Handler mHandler;
	private String curVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					dialog.dismiss();
					Toast.makeText(FeedbackActivity.this, "发送成功！", 0).show();
				}
				super.handleMessage(msg);
			}
		};
		// 去头条目 全屏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		SoftApplication appState = (SoftApplication)this.getApplication();
		appState.addActivity(this);
		setContentView(R.layout.feedback);

		feedback_android = (TextView) findViewById(R.id.feedback_android);
		feedback_appName = (TextView) findViewById(R.id.feedback_Appname);
		feedback_appVersion = (TextView) findViewById(R.id.feedback_appversion);
		feedback_device = (TextView) findViewById(R.id.feedback_device);
		feedback_edit = (EditText) findViewById(R.id.feedback_edit);

		ImageView backButton = (ImageView) findViewById(R.id.cancel);
		backButton.setOnClickListener(new onClickBackListener());
		ImageView sendButton = (ImageView) findViewById(R.id.post);
		sendButton.setOnClickListener(new onClickSendListener());
		initView();

		// MyApplication.getInstance().addActivity(this);
	}

	public class MyThread extends Thread {
		public void run() {

			Message msg = new Message();
			msg.what = 0;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			mHandler.sendMessage(msg);
		}
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}

	private void initView() {
		// 获取设备型号
		Build bd = new Build();
		String model = bd.MODEL;
		feedback_device.setText(model);
		// 获取当前版本
		try {
			curVersion = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName;
			feedback_appVersion.setText(curVersion);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// 获取系统版本(1.android.os.Build.MODEL 2.android.os.Build.VERSION.RELEASE
		// 3.android.os.Build.VERSION.SDK)
		feedback_android.setText(android.os.Build.VERSION.RELEASE);
	}

	class onClickBackListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			finish();
		}

	}
	class onClickSendListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (feedback_edit.getText().toString() != null) {
				String msg="content:"+feedback_edit.getText()+";os_version:"+ android.os.Build.VERSION.RELEASE;
				dialog = new ProgressDialog(FeedbackActivity.this);
				dialog.setMessage("请稍等");
				dialog.show();
				Map<String, String> rp = new HashMap<String, String>();
				rp.put("siteid", "3757");
				rp.put("msg",msg);//反馈内容
				rp.put("sort","6");//sort为6则是用户反馈
				NxtRestClientNew.post("postdongtai", rp, new HttpCallBack() {
					@Override
					public void onSuccess(String content) {
						// TODO Auto-generated method stub
						super.onSuccess(content);
						content = this.getContent(content);
						dialog.dismiss();
						Toast.makeText(FeedbackActivity.this, "提交成功！", 0).show();
						finish();
					}
					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						super.onFailure(error, content);
						dialog.dismiss();
						Toast.makeText(FeedbackActivity.this, "提交成功！", 0).show();
						finish();
					}
				});
			} else {
				Toast.makeText(FeedbackActivity.this, "内容不能为空", 0).show();
			}

		}

	}

	// you need config the mail app in your android moble first,and the mail
	// will send by the mail app. and there are one big bug:
	// you can't send the mail Silently and you need to click the send button
	public int sendMailByIntent(String mybody) {
		String[] reciver = new String[] { "630538331@qq.com" };
		String[] mySbuject = new String[] { "test" };
		String myCc = "cc";
		Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
		myIntent.setType("plain/text");
		myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
		myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
		myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySbuject);
		myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);
		startActivity(Intent.createChooser(myIntent, "mail test"));

		return 1;

	}

	/*
	 * this method use javamail for android ,it is a good jar, you can see the
	 * demo in
	 * http://www.jondev.net/articles/Sending_Emails_without_User_Intervention_
	 * (no_Intents)_in_Android and you also need three jars ,which I offered in
	 * attachement
	 */
	// public int sendMailByJavaMail(String body) {
	// Mail m = new Mail("wungtingting@gmail.com", "wtt123456");
	// String[] toArr = { "wungtingting@gmail.com" };
	// m.set_debuggable(true);
	// m.set_to(toArr);
	// m.set_from("wungtingting@gmail.com");
	// m.set_subject("魅力三农");
	// m.setBody("魅力三农意见反馈：" + body);
	// try {
	// // m.addAttachment("/sdcard/filelocation");
	// if (m.send()) {
	// LogUtil.LogI("IcetestActivity", "Email was sent successfully.");
	//
	// } else {
	// LogUtil.LogI("IcetestActivity", "Email was sent failed.");
	// }
	// } catch (Exception e) {
	// // Toast.makeText(MailApp.this,
	// // "There was a problem sending the email.",
	// // Toast.LENGTH_LONG).show();
	// LogUtil.LogI("MailApp", "Could not send email", e);
	// }
	//
	// return 1;
	// }

}
