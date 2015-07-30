package com.huntor.mscrm.app2.ui.component;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Admin on 2015/6/15.
 */
public class MyViewPager extends ViewPager{

    private boolean scrollble=true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (!scrollble) {
//            return true;
//        }
//        return super.onTouchEvent(ev);
//    }
//
//
//    public boolean isScrollble() {
//        return scrollble;
//    }
//
//    public void setScrollble(boolean scrollble) {
//        this.scrollble = scrollble;
//    }

    /**
     * 重写事件的分发和拦截
     *
     *
     *
     * 当TouchEvent发生时，首先Activity将TouchEvent传递给最顶层的View，
     * TouchEvent最先到达最顶层 view 的 dispatchTouchEvent ，
     * 然后由  dispatchTouchEvent 方法进行分发，如果dispatchTouchEvent返回true ，则交给这个view的onTouchEvent处理，
     * 如果dispatchTouchEvent返回 false ，则交给这个 view 的 interceptTouchEvent 方法来决定是否要拦截这个事件，
     * 如果 interceptTouchEvent 返回 true ，也就是拦截掉了，则交给它的 onTouchEvent 来处理，
     * 如果 interceptTouchEvent 返回 false ，那么就传递给子 view ，由子 view 的 dispatchTouchEvent 再来开始这个事件的分发。
     * 如果事件传递到某一层的子 view 的 onTouchEvent 上了，这个方法返回了 false ，那么这个事件会从这个 view 往上传递，都是 onTouchEvent 来接收。
     * 而如果传递到最上面的 onTouchEvent 也返回 false 的话，这个事件就会“消失”，而且接收不到下一次事件。
     */

    //这个方法用来分发TouchEvent
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    //这个方法用来拦截TouchEvent
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        //return true;//不拦截
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




    //这个方法用来处理TouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

////        if (!scrollble) {
////            return false;
////        }
//        Log.i("tag","scrollble mvp:"+scrollble);
        return super.onTouchEvent(ev);
        //return false;//返回true
    }

}
