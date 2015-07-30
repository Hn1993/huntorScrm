package com.huntor.mscrm.app2.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;


public class SlideItem extends LinearLayout {

	private String TAG = "SlideItem";

	private static final int TAN = 3;

	private Context mCtx;
	
	private Scroller mScroller;
	
	private View mContent; // main content
	
	private View mHolder; // operation area

	private int mHolderWidth = sHolderDip;

	private OnSlideListener mSlideListener;

	private int mLastX;

	private int mLastY;

	private static int sHolderDip = 70; // fix the holder width here
	
	@SuppressLint("NewApi")
	public SlideItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SlideItem(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}
	
	private void init() {
		mCtx = getContext();
		mScroller = new Scroller(mCtx);
		mHolderWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 
				sHolderDip, 
				getResources().getDisplayMetrics());
		// init your view component here
	}
	
	public void setOnSlideListener(OnSlideListener listener){
		mSlideListener = listener;
	}
	
	public void close(){
		if(getScrollX() != 0){
			smoothScrollTo(0);
		}
	}
	
	private void smoothScrollTo(int to) {
		int from = getScrollX();
		mScroller.startScroll(from, 0, to - from, 0);
		invalidate();
	}
	
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(), 0);
			postInvalidate();
		}
	}

	public void onPassTouchEvent(MotionEvent e){
		int x = (int) e.getX();
		int y = (int) e.getY();
		int scrollX = getScrollX();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:{
				if(mScroller.isFinished()){
					mScroller.abortAnimation();
				}
				if(mSlideListener != null){
					mSlideListener.onSlide(OnSlideListener.SlideState.BEGIN);
				}
				mLastX = x;
				mLastY = y;
				break;
			}
		case MotionEvent.ACTION_MOVE:{
				int deltaX = Math.abs(x - mLastX);
				int deltaY = Math.abs(y - mLastY);
				if(deltaX < deltaY * TAN){
					break;
				}
				int newScrollX = scrollX + mLastX - x;  // scroll should be the opposite direction 
				if(deltaX != 0){
					if(newScrollX < 0){
						newScrollX = 0;
					}else if(newScrollX > mHolderWidth){
						newScrollX = mHolderWidth;
					}
					scrollTo(newScrollX, 0);
				}
				mLastX = x;
				mLastY = y;
				break;
			}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:{
				int newScrollX = 0;
				if(scrollX > mHolderWidth * 0.6 ){
					newScrollX = mHolderWidth;
				}
				smoothScrollTo(newScrollX);
				if(mSlideListener != null){
					mSlideListener.onSlide(newScrollX == 0 ? OnSlideListener.SlideState.OFF : OnSlideListener.SlideState.ON);
				}
				break;
			}
		default:
			break;
		}
	}
}
