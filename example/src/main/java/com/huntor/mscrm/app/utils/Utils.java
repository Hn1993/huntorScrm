package com.huntor.mscrm.app.utils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.huntor.mscrm.app.ui.MainActivity;

public class Utils {
	private static final String TAG = "Utils";

	/**
	 * 获取整型的ip地址(0表示设备未联网)
	 *
	 * @param context 上下文
	 * @return 整型的ip地址
	 */
	public static int getIpAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return wifiInfo.getIpAddress();
	}

	/**
	 * 将整型的ip地址转换为点分十进制格式("192.168.21.30")
	 *
	 * @param ip int型的ip地址
	 * @return 点分十进制格式的ip
	 */
	public static String getDecimalIpAddress(int ip) {
		// 分别取出每个字节的值
		return ((ip) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
				+ (ip >> 24 & 0xFF);
	}

	/**
	 * 获取ip的网络号。如:"192.168.21.30",若子网掩码为"255.255.255.0",则前三位"192.168.21"为ip的网络号,
	 * 最后一位"30"为ip的主机号
	 *
	 * @param ip int型的ip地址
	 * @return ip的网络号
	 */
	public static String getIpNetAddress(int ip) {
		return ((ip) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF);
	}

	/**
	 * 获取ip的主机号。如:"192.168.21.30",若子网掩码为"255.255.255.0",则前三位"192.168.21"为ip的网络号,
	 * 最后一位"30"为ip的主机号
	 *
	 * @param ip int型的ip地址
	 * @return ip的主机号
	 */
	public static int getIpHostAddress(int ip) {
		// 右移24位后获取到的一个字节
		return (ip >> 24) & 0xFF;
	}

	/**
	 * 收起输入法键盘
	 *
	 * @param context   Context
	 * @param tokenView 该输入法绑定的View
	 */
	public static void colseInputMethod(Context context, View tokenView) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(tokenView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 打开输入法键盘
	 *
	 * @param context   Context
	 * @param tokenView 该输入法绑定的View
	 */
	public static void openInputMethod(Context context, View tokenView) {
		tokenView.setFocusable(true);
		tokenView.requestFocus();
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInputFromWindow(tokenView.getWindowToken(), 0, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 验证IP是否合法
	 *
	 * @param ipAddress ip地址
	 * @return ip合法返回true，否则返回false
	 */
	public static boolean verifyIP(String ipAddress) {
		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		if (ipAddress.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件或目录
	 *
	 * @param file 文件或目录
	 * @return 是否删除成功
	 */
	public static boolean deleteFile(File file) {
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			for (File childFile : childFiles) {
				deleteFile(childFile);
			}
		}
		return file.delete();
	}

	/**
	 * 获取应用版本号
	 *
	 * @param context 上下文
	 * @return 应用版本号
	 */
	public static String getVersionCode(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
		}
		return "";
	}

	/**
	 * 获取设备的MAC地址
	 *
	 * @param context 上下文
	 * @return 设备的MAC地址
	 */
	public static String getMacAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo != null) {
			return wifiInfo.getMacAddress();
		}
		return null;
	}

	/*
	 * 设置对话框大小
	 */
	public static void setDialogSize(Activity activity, Dialog dialog) {
		WindowManager m = activity.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
		p.height = (int) d.getHeight(); // 高度设置为屏幕的高度
		p.width = (int) d.getWidth(); // 宽度设置为屏幕的宽度
		dialog.getWindow().setAttributes(p);
	}

	/**
	 * Toast
	 *
	 * @param msg
	 * 提示内容
	 */
	private static Toast mToast = null;

	public static void toast(Context context, String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}

		mToast.show();
	}

	/**
	 * Toast
	 *
	 * @param resId 字串资源id
	 */
	public static void toast(Context context, int resId) {
		if (mToast == null) {
			mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(resId);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}

		mToast.show();
	}

	/**
	 * 获取缓存目录
	 *
	 * @return 缓存目录
	 */
	public static String getCacheDir(Context context) {
		Log.i(TAG, "CacheDir = " + context.getExternalCacheDir());
		return context.getExternalCacheDir() + Constant.CACHE_DIR;
	}

	/**
	 * 获取屏幕的宽度
	 */
	public static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 *
	 * @param pxValue （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * @param url 指定的任意字符串
	 * @return 指定字符串通过md5运算并转化为字符串，如果url为null或长度为0，则返回null
	 */
	public static String md5(String url) {
		String result = null;
		if (url != null && url.length() > 0) {
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(url.getBytes());
				byte[] tar = md5.digest();
				StringBuilder sb = new StringBuilder("");
				for (byte b : tar) {
					int h = (b >> 4) & 15;
					int l = b & 15;
					// 因为4位二进制数最大为1111，即15
					sb.append(Integer.toHexString(h)).append(
							Integer.toHexString(l));
				}
				result = sb.toString();

			} catch (NoSuchAlgorithmException e) {
				result = String.valueOf(url.hashCode());
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 在卸载应用后，会自动删除该文件夹
	 * 优先使用内存卡
	 *
	 * @param context
	 * @param uniqueName 保存文件夹名称
	 * @return
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else { // 内存卡不存在,获取应用地址
			cachePath = context.getCacheDir().getPath();
		}
		File cacheFile = new File(cachePath + File.separator + uniqueName);
		// 如果文件夹不存在，则创建
		if (!cacheFile.exists()) {
			cacheFile.mkdirs();
		}
		return cacheFile;
	}

	/**
	 * 检验是否为手机号
	 *
	 * @param phoneNum
	 * @return
	 */
	public static boolean isMobile(String phoneNum) {
		Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher matcher = pattern.matcher(phoneNum);
		return matcher.matches();
	}

	/**
	 * 判断网络是否连接
	 *
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivity) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断网络是否有网络连接
	 *
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				mNetworkInfo.isAvailable();
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}


	/**
	 * 判断是否是wifi连接
	 */

	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity) {
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings",
				"com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}


	/**
	 * 日志写入sdcard
	 */
	//public static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

}
