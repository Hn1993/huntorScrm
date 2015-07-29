package com.huntor.mscrm.app.ui.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by cary.xi on 2015/5/3.
 */
public class GrayUnderLineEditText extends EditText {

    private Paint mPaint;

    private boolean isTouch;

    public GrayUnderLineEditText(Context context) {
        super(context);
        init();
    }

    public GrayUnderLineEditText(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GRAY);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (hasFocus()){
            mPaint.setStrokeWidth(10.0f);
        }else {
            mPaint.setStrokeWidth(1.0f);
        }

        canvas.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1, mPaint);
    }
}
