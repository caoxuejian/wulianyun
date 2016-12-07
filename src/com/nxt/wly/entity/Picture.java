package com.nxt.wly.entity;

import android.graphics.Bitmap;

public class Picture {
	private String imageurl;
	private Bitmap bitmap;

	public Picture() {
		super();
	}

	public Picture(String imageurl) {
		super();
		this.imageurl = imageurl;
	}

	public Picture(String imageurl, Bitmap bitmap) {
		super();
		this.imageurl = imageurl;
		this.bitmap = bitmap;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public String toString() {
		return "Picture [imageurl=" + imageurl + ", bitmap=" + bitmap + "]";
	}

}
