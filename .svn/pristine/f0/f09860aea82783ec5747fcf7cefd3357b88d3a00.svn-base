package com.nxt.wly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ShareActivity extends Activity implements OnClickListener {
	private Button share;
	private EditText share_phone;
	private TextView share_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_share);
		findView();
		
	}

	private void findView() {
		// TODO Auto-generated method stub
		share=(Button)findViewById(R.id.share);
		share_phone=(EditText)findViewById(R.id.share_phone);
		share_text=(TextView)findViewById(R.id.share_text);
		share.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		if(id==R.id.share){
			    String  number = share_phone.getText().toString();  
			    String content=share_text.getText().toString();
			    com.nxt.nxtapp.common.LogUtil.syso("content:::"+content);
				Intent send = new Intent();
				send.setAction(Intent.ACTION_SENDTO);
				send.setData(Uri.parse("smsto:" + number));
				send.putExtra("sms_body", getResources().getString(R.string.share_msg));
				startActivity(send);
		}
	}
	
	
	public void onback(View v){
		finish();
	}
	
	
	
	

}
