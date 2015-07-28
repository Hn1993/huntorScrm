package com.huntor.scrm.view.EditText;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.huntor.scrm.R;


/**
 * Created by SL
 * on 2015/4/25 0025 16:54.
 * By IDEA 14.0.2
 */

/**
 * 带有白色下划线的EditText
 */
public class ColorUnderLineEditText extends EditText {

    private Paint mPaint;

    private boolean isTouch;

    public ColorUnderLineEditText(Context context) {
        super(context);
        init();
    }

    public ColorUnderLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.STROKE);
        //mPaint.setColor(Color.WHITE);
        mPaint.setColor(getResources().getColor(R.color.login_edit_undercolor));//按下后的背景色  根据需求更改
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
