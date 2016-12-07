package com.thecamhi.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class DatabaseManager {
	public static final String TABLE_DEVICE="device";
	public static final String TABLE_ALARM_EVENT = "alarm_event";
	private DatabaseHelper dbHelper;

	public DatabaseManager(Context context) {
		super();
		dbHelper=new DatabaseHelper(context);
	}
	
	public SQLiteDatabase getReadableDatabase(){
		return dbHelper.getReadableDatabase();
	}
	
	public long addDevice(String dev_nickname,String dev_uid,String dev_name,String dev_pwd,int videoQuality,int allAlarmState,int pushState){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("dev_nickname", dev_nickname);
		values.put("dev_uid", dev_uid);
		values.put("dev_name", dev_name);
		values.put("dev_pwd", dev_pwd);
		values.put("dev_videoQuality", videoQuality);
		values.put("dev_alarmState", allAlarmState);
		values.put("dev_pushState", pushState);
		
		long ret=db.insertOrThrow(TABLE_DEVICE, null, values);
		return ret;
		
	}
	
	public void updateDeviceByDBID(long db_id,String dev_nickname,String dev_uid,String dev_name,
			String dev_pwd,int videoQuality,int allAlarmState,int pushState){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("dev_nickname", dev_nickname);
		values.put("dev_uid", dev_uid);
		values.put("dev_name", dev_name);
		values.put("dev_pwd", dev_pwd);
		values.put("dev_videoQuality", videoQuality);
		values.put("dev_alarmState", allAlarmState);
		values.put("dev_pushState", pushState);
		db.update(TABLE_DEVICE, values, "_id = '" + db_id + "'", null);
	}
	
	public void updateDeviceSnapshotByUID(String dev_uid,Bitmap snapshot){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("snapshot",getByteArrayFromBitmap(snapshot));
		db.update(TABLE_DEVICE, values, "dev_uid='"+dev_uid+"'", null);
		db.close();
	}
	
	public void updateDeviceSnapshotByUID(String dev_uid,byte[] snapshot){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("snapshot",snapshot);
		db.update(TABLE_DEVICE, values, "dev_uid='"+dev_uid+"'", null);
		db.close();
	}
	
	public void getDeviceSnapshotByUID(String dev_uid){
			Bitmap bmp=null;
			SQLiteDatabase db=dbHelper.getReadableDatabase();
			Cursor cursor=db.query(TABLE_DEVICE, new String[]{"snapshot"}, 
					"dev_uid=", null, null, null, null);
			while(cursor.moveToNext()){
				byte[] snapshot=cursor.getBlob(0);
				if(snapshot!=null){
					bmp = DatabaseManager.getBitmapFromByteArray(snapshot);
				}
			}
		
	}
	
	public void removeDeviceByUID(String dev_uid){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		db.delete(TABLE_DEVICE, "dev_uid='"+dev_uid+"'", null);
		db.close();
	}
	
	
	public void removeDeviceAlartEvent(String dev_uid) {
		
		SQLiteDatabase db =dbHelper.getWritableDatabase();
		db.delete(TABLE_ALARM_EVENT, "dev_uid = '" + dev_uid + "'", null);
		db.close();
	}
	
	public long addAlarmEvent(String dev_uid,int time,int type ){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("dev_uid", dev_uid);
		values.put("time", time);
		values.put("type", type);
		long ret=db.insertOrThrow(TABLE_ALARM_EVENT, null, values);
		db.close();
		return ret;
	}
	
	
	public static Bitmap getBitmapFromByteArray(byte[] byts) {

		InputStream is = new ByteArrayInputStream(byts);
		return BitmapFactory.decodeStream(is, null, getBitmapOptions(2));
	}
	
	@SuppressWarnings("deprecation")
	public static BitmapFactory.Options getBitmapOptions(int scale) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inSampleSize = scale;

		return options;
	}
	
	public static byte[] getByteArrayFromBitmap(Bitmap bitmap){
		if(bitmap!=null&&!bitmap.isRecycled()){
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 0, bos);
			return bos.toByteArray();
		}else{
			return null;
		}
	}
	
	
	
	
	
	
	
	
	private class DatabaseHelper extends SQLiteOpenHelper{
		
		private static final String DB_FILE="HiChipCamera.db";
		private static final int de_version=15;
		private static final String SQLCMD_CREATE_TABLE_DEVICE="CREATE TABLE "+TABLE_DEVICE+" ( _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
				"dev_nickname NVARCHAR(30) NULL , "+" dev_uid  NVARCHAR(20) NULL ,"+"dev_name VARCHAR(30) NULL, " +
						"dev_pwd VARCHAR(30) NULL ,"+" dev_videoQuality  INTEGER , "+"dev_alarmState INTEGER ,"+"  dev_pushState INTEGER ,"+
				" snapshot BLOB "+" );";
		private static final String SQLCMD_DROP_TABLE_DEVICE="drop table if exists "+TABLE_DEVICE+";";
		private static final String SQLCMD_CREATE_TABLE_ALARM_EVENT="CREATE TABLE "+TABLE_ALARM_EVENT +" (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , " +
				"dev_uid VARCHAR(20) NULL , "+"time INTEGER ,"+"type INTEGER "+" ) ";
		private static final String SQLCMD_DROP_TABLE_ALARM_EVENT ="drop table id exists  "+ TABLE_ALARM_EVENT+" ;";
		
		
		public DatabaseHelper(Context context) {
			super(context, DB_FILE, null, de_version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQLCMD_CREATE_TABLE_DEVICE);
			db.execSQL(SQLCMD_CREATE_TABLE_ALARM_EVENT);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQLCMD_DROP_TABLE_ALARM_EVENT);
			db.execSQL(SQLCMD_DROP_TABLE_DEVICE);
			onCreate(db);
		}
		
	}
}
