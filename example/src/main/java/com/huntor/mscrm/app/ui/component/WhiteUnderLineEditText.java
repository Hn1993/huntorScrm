package com.huntor.mscrm.app.ui.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by SL
 * on 2015/4/25 0025 16:54.
 * By IDEA 14.0.2
 */

/**
 * 带有白色下划线的EditText
 */
public class WhiteUnderLineEditText extends EditText {

    private Paint mPaint;

    private boolean isTouch;

    public WhiteUnderLineEditText(Context context) {
        super(context);
        init();
    }

    public WhiteUnderLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);

//        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (hasFocus()){
            mPaint.setStrokeWidth(10.0f);
        }else {
            mPaint.setStrokeWidth(1.0f);
        }

        canvas.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1, mPaint);
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        boolean ret = false;
//        int action = event.getAction();
//        if (action == MotionEvent.ACTION_OUTSIDE){
//            setFocusable(false);
//            ret = false;
//        }else {
//            setFocusable(true);
//            ret = true;
//        }
//        isTouch = ret;
//        return ret;
//    }
}
