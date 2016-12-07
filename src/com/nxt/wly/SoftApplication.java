package com.nxt.wly;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.nxt.nxtapp.NXTAPPApplication;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

public class SoftApplication extends NXTAPPApplication {
	private static final String TAG = "SoftApplication";
	private static SoftApplication instance;
	private List<Activity> mainActivity;
	private boolean need2Exit;

	// 单例模式获取唯一的MyApplication实例
	public static SoftApplication getInstance() {
		if (null == instance) {
			instance = new SoftApplication();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		/**
		 * X5内核在使用preinit接口之后，对于首次安装首次加载没有效果
		 * 实际上，X5webview的preinit接口只是降低了webview的冷启动时间；
		 * 因此，现阶段要想做到首次安装首次加载X5内核，必须要让X5内核提前获取到内核的加载条件
		 */
		initImageLoader(getApplicationContext());
		preinitX5WebCore();
	}
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		//设置缓存路径
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache");  
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
				.Builder(context)  
		.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽  
		.threadPoolSize(3)//线程池内加载的数量  
		.threadPriority(Thread.NORM_PRIORITY - 2)  
		.denyCacheImageMultipleSizesInMemory()  
		.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
		.memoryCacheSize(2 * 1024 * 1024)    
		.discCacheSize(50 * 1024 * 1024)    
		.tasksProcessingOrder(QueueProcessingType.LIFO)  
		.discCacheFileCount(100) //缓存的文件数量  
		.discCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径  
		.defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
		.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
		.writeDebugLogs() // Remove for release app  
		.build();//开始构建  
		/*
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();*/
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	private void preinitX5WebCore() {
		//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
				//TbsDownloader.needDownload(getApplicationContext(), false);

				QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

					@Override
					public void onViewInitFinished(boolean arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onCoreInitFinished() {
						// TODO Auto-generated method stub

					}
				};
				QbSdk.setTbsListener(new TbsListener() {
					@Override
					public void onDownloadFinish(int i) {
					}

					@Override
					public void onInstallFinish(int i) {
					}

					@Override
					public void onDownloadProgress(int i) {
					}
				});

				QbSdk.initX5Environment(getApplicationContext(),  cb);
	}
	public void setNeed2Exit(boolean bool) {
		need2Exit = bool;
	}

	public boolean need2Exit() {
		return need2Exit;
	}

	public List<Activity> MainActivity() {
		return mainActivity;
	}

	public void addActivity(Activity act) {
		if (mainActivity == null)
			mainActivity = new ArrayList<Activity>();
		mainActivity.add(act);
	}

	public void finishAll() {
		for (Activity act : mainActivity) {
			if (!act.isFinishing()) {
				act.finish();
			}
		}
		mainActivity = null;
		System.exit(0);
	}

}
