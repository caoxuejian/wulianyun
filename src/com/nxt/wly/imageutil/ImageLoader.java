package com.nxt.wly.imageutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.nxt.nxtapp.imageutils.FileCache;
import com.nxt.nxtapp.imageutils.MemoryCache;

public class ImageLoader {

	MemoryCache memoryCache = new MemoryCache();
	// 线程池
	ExecutorService executorService;
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	private int stub_id;
	private static Context mContext;
	private static ImageLoader loader;

	public interface ImageCallBack {
		public void loadImage(Bitmap bitmap);
	}

	public static ImageLoader getInstance(Context context) {
		mContext = context;
		if (loader == null) {
			loader = new ImageLoader(context);
		}
		return loader;
	}

	private ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	// 最主要的方法
	public void displayImage(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
//			PersonalDetailsActivity.photo = bitmap;
			imageView.setImageBitmap(bitmap);
		} else {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView);
			imageView.setImageResource(getStub_id());
		}
	}

	public void displayImage(String url, ImageView imageView, int stud_id) {
		setStub_id(stud_id);
		displayImage(url, imageView);
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);
		// 先从文件缓存中查找是否有
		Bitmap b = decodeFile(f);
		if (b != null) {
			return b;
		}

		// 最后从指定的url中下载图片
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	private Bitmap decodeFile(File f) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, options);

			// int heightRatio =
			// (int)Math.ceil(options.outHeight/(float)(MyApplication.windowHeight/2));
			// int widthRatio =
			// (int)Math.ceil(options.outWidth/(float)(MyApplication.windowWidth/2));
			// if (heightRatio > 1 && widthRatio > 1){
			// scale = heightRatio > widthRatio ? heightRatio:widthRatio;
			// }
			//
			options.inSampleSize = 1;
			int outWidth = options.outWidth; // 获得图片的实际高和宽
			int outHeight = options.outHeight;
			// com.nxt.nxtapp.common.LogUtil.syso("================="+options.inSampleSize);
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			return BitmapFactory.decodeStream(new FileInputStream(f), null,
					options);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			// 判断imageView是否被重用
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
//			PersonalDetailsActivity.photo = bmp;
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// 更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	@SuppressLint("HandlerLeak")
	public void getBitmapAsync(final String url, final ImageCallBack callback) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					Bitmap bitmap = (Bitmap) msg.obj;
					callback.loadImage(bitmap);
				}
			};
		};

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				msg.obj = getBitmap(url);
				handler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * 防止图片错位
	 * 
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// 用于在UI线程中更新界面
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageResource(getStub_id());
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public void clearMemoryCache() {
		memoryCache.clear();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public void setStub_id(int stub_id) {
		this.stub_id = stub_id;
	}

	public int getStub_id() {
		return stub_id;
	}
}
