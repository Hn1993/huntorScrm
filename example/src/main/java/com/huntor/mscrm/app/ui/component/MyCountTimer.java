package com.huntor.mscrm.app.ui.component;

import android.os.CountDownTimer;
import android.widget.TextView;
import com.huntor.mscrm.app.R;

/**
 * Created by Admin on 2015/7/28.
 */
public class MyCountTimer extends CountDownTimer {
//	public static final int TIME_COUNT = 121000;//ʱ���ֹ��119s��ʼ��ʾ���Ե���ʱ120sΪ���ӣ�
	public static final int TIME_COUNT = 60000;
	private TextView btn;
	private int endStrRid;
	private int normalColor, timingColor;//δ��ʱ��������ɫ����ʱ�ڼ��������ɫ

	/**
	 * ���� millisInFuture         ����ʱ��ʱ�䣨��60S��120s�ȣ�
	 * ���� countDownInterval    ����ʱ�䣨ÿ�ε���1s��

	 * ���� btn               ����İ�ť(��ΪButton��TextView���࣬Ϊ��ͨ���ҵĲ�������ΪTextView��

	 * ���� endStrRid   ����ʱ�����󣬰�ť��Ӧ��ʾ������
	 */
	public MyCountTimer (long millisInFuture, long countDownInterval, TextView btn, int endStrRid) {
		super(millisInFuture, countDownInterval);
		this.btn = btn;
		this.endStrRid = endStrRid;
	}


	/**

	 *����������ע��
	 */
	public  MyCountTimer (TextView btn, int endStrRid) {
		super(TIME_COUNT, 1000);
		this.btn = btn;
		this.endStrRid = endStrRid;
	}

	public MyCountTimer (TextView btn) {
		super(TIME_COUNT, 1000);
		this.btn = btn;
		this.endStrRid = R.string.txt_getMsgCode_validate;
	}


	public MyCountTimer (TextView tv_varify, int normalColor, int timingColor) {
		this(tv_varify);
		this.normalColor = normalColor;
		this.timingColor = timingColor;
	}

	// ��ʱ���ʱ����
	@Override
	public void onFinish() {
		if(normalColor > 0){
			btn.setTextColor(normalColor);
		}
		btn.setText(endStrRid);
		btn.setEnabled(true);
	}

	// ��ʱ������ʾ
	@Override
	public void onTick(long millisUntilFinished) {
		if(timingColor > 0){
			btn.setTextColor(timingColor);
		}
		btn.setEnabled(false);
		btn.setText(millisUntilFinished / 1000 + "s");
	}
}
