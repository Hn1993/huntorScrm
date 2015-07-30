package com.huntor.mscrm.app2.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;


/**
 * @author Minami
 */

public class MySlideListView extends ListView {
	public static final String TAG = MySlideListView.class.getCanonicalName();
	protected static final float THRESHOLD = 20;
	
	public MySlideListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MySlideListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	private SlideItem mCurrentItem;
	
	private int mInitTagTop;

	private boolean scrollble=true;

	//事件的拦截
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.i("tag","scrollble msl:"+scrollble);
		if (!scrollble) {
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}
	public boolean isScrollble() {
		return scrollble;
	}

	public void setScrollble(boolean scrollble) {
		this.scrollble = scrollble;
	}
	public boolean onTouchEvent(MotionEvent e) {
		if(e.getAction() == MotionEvent.ACTION_DOWN){
			int x = (int) e.getX();
			int y = (int) e.getY();
			int position = pointToPosition(x, y) - getFirstVisiblePosition();
			if(position != INVALID_POSITION){
				SlideItem newItem = (SlideItem) getChildAt(position);
				if(mCurrentItem != null && newItem != mCurrentItem){
					mCurrentItem.close();
				}
				if(newItem != null){
					mCurrentItem = newItem;
					mInitTagTop = mCurrentItem.getTop();
				}
			}
		}
		if(mCurrentItem != null){
			int tagTop = mCurrentItem.getTop();
			if(Math.abs(tagTop - mInitTagTop) > THRESHOLD){
				mCurrentItem.close();
				mCurrentItem = null;
			}else{
				mCurrentItem.onPassTouchEvent(e);
			}
		}
		boolean superConsume =  super.onTouchEvent(e);
		return superConsume;
	}
}
