package com.huntor.scrm.utils;

import android.util.Log;

public final class MyLogger {
	//项目上线后改为false
	private final static boolean flag = true;

	public static void v(String tag, String msg) {
		if (flag)
			Log.v(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (flag)
			Log.d(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (flag)
			Log.i(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (flag)
			Log.w(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (flag)
			Log.e(tag, msg);
	}

}
