package com.thecamhi.server;

import com.hichip.base.HiLog;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DownloadInBackgroundService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		HiLog.e("DownloadInBackgroundService.bind()");
		
		return new LocalBinder();
	}
	 public class LocalBinder extends Binder {  
		 DownloadInBackgroundService getService() {  
	            // Return this instance of LocalService so clients can call public methods  
	            return DownloadInBackgroundService.this;  
	        }     
	    }  
}
