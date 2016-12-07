package com.thecamhi.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.hichip.content.HiChipDefines;
import com.hichip.control.HiGLMonitor;
import com.hichip.sdk.HiChipP2P;
import com.thecamhi.bean.MyCamera;

public class MyLiveViewGLMonitor extends HiGLMonitor implements OnTouchListener,GestureDetector.OnGestureListener{

	private long ptzTime = 0;
	private GestureDetector mGestureDetector;
	private static final int FLING_MIN_DISTANCE = 50;

	private MyCamera mCamera = null;

	private OnTouchListener mOnTouchListener;

//	private int state=0;//normal=0, larger=1, arrow=2;

	public MyLiveViewGLMonitor(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mGestureDetector = new GestureDetector(context, this);
		super.setOnTouchListener(this);
		setOnTouchListener(this);  
		setFocusable(true);     
		setClickable(true);     
		setLongClickable(true);   
	}



/*	public int getState(){
		return state;
	}

	public void setState(int state){
		this.state=state;
	}
*/
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//		HiLog.e("==========MyGLMonitor  onPause===========");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//		HiLog.e("==========MyGLMonitor  onResume===========");
	}



	//View当前的位置  
	private int rawX = 0;  
	private int rawY = 0;  
	//View之前的位置  
	private int lastX = 0;  
	private int lastY = 0;  

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		//		HiLog.v("onTouch:");
		
/*		if(mOnTouchListener != null) {
			mOnTouchListener.onTouch(v, event);
		}

		if(state!=0){
			switch(event.getAction()){  
			
			case MotionEvent.ACTION_DOWN:  
				//Log.e("ACTION","down");  
				//获取手指落下的坐标并保存  
				rawX = (int)(event.getRawX());  
				rawY = (int)(event.getRawY());  
				lastX = rawX;  
				lastY = rawY;  
				break;  
			case MotionEvent.ACTION_MOVE:  
				//Log.e("ACTION","move");  
				//手指拖动时，获得当前位置  
				rawX = (int)event.getRawX();  
				rawY = (int)event.getRawY();  
				//手指移动的x轴和y轴偏移量分别为当前坐标-上次坐标  
				int offsetX = rawX - lastX;  
				int offsetY = rawY - lastY;  
				//通过View.layout来设置左上右下坐标位置  
				//获得当前的left等坐标并加上相应偏移量  
				layout(getLeft() + offsetX,  
						getTop() + offsetY,  
						getRight() + offsetX,  
						getBottom() + offsetY);  
				//移动过后，更新lastX与lastY  
				lastX = rawX;  
				lastY = rawY;  
				break;  
			}
			return true;  
		}

		
		return mGestureDetector.onTouchEvent(event);*/

		
		if(mOnTouchListener != null) {
			mOnTouchListener.onTouch(v, event);
		}




		return mGestureDetector.onTouchEvent(event);

		//		HiLog.v("onTouch:"+event);

		//		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		//		HiLog.v("onDown:");
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		//		HiLog.v("onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		//		HiLog.v("onSingleTapUp");
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		//		HiLog.v("onScroll:");


		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		//		HiLog.v("onLongPress:");
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		//		HiLog.v("velocityX: " + Math.abs(velocityX) + ", velocityY: " + Math.abs(velocityY));
		if(mCamera == null)
			return false;

		long curTime = System.currentTimeMillis();
		if(curTime - ptzTime > 500)
		{
			ptzTime = curTime;
		}
		else {
			return false;
		}

		this.scrollTo((int)velocityX,(int)velocityY);

		invalidate();

			if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
				mCamera.sendIOCtrl(HiChipDefines.HI_P2P_SET_PTZ_CTRL,HiChipDefines.HI_P2P_S_PTZ_CTRL.parseContent(HiChipP2P.HI_P2P_SE_CMD_CHN, HiChipDefines.HI_P2P_PTZ_CTRL_LEFT, HiChipDefines.HI_P2P_PTZ_MODE_STEP,(short)50,(short)50));
			} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {
				mCamera.sendIOCtrl(HiChipDefines.HI_P2P_SET_PTZ_CTRL,HiChipDefines.HI_P2P_S_PTZ_CTRL.parseContent(HiChipP2P.HI_P2P_SE_CMD_CHN, HiChipDefines.HI_P2P_PTZ_CTRL_RIGHT, HiChipDefines.HI_P2P_PTZ_MODE_STEP,(short)50,(short)50));
			} else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > Math.abs(velocityX)) {
				mCamera.sendIOCtrl(HiChipDefines.HI_P2P_SET_PTZ_CTRL,HiChipDefines.HI_P2P_S_PTZ_CTRL.parseContent(HiChipP2P.HI_P2P_SE_CMD_CHN, HiChipDefines.HI_P2P_PTZ_CTRL_UP, HiChipDefines.HI_P2P_PTZ_MODE_STEP,(short)50,(short)50));
			} else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE && Math.abs(velocityY) > Math.abs(velocityX)) {
				mCamera.sendIOCtrl(HiChipDefines.HI_P2P_SET_PTZ_CTRL,HiChipDefines.HI_P2P_S_PTZ_CTRL.parseContent(HiChipP2P.HI_P2P_SE_CMD_CHN, HiChipDefines.HI_P2P_PTZ_CTRL_DOWN, HiChipDefines.HI_P2P_PTZ_MODE_STEP,(short)50,(short)50));
			}
		

		return false;
	}



	public void setCamera(MyCamera mCamera) {
		this.mCamera = mCamera;
	}


	public void setOnTouchListener(OnTouchListener mOnTouchListener) {
		this.mOnTouchListener = mOnTouchListener;
	}




}
