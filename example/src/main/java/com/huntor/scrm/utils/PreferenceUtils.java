package com.huntor.scrm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * Preference的工具类
 */
public class PreferenceUtils {

	/**
	 * 向Preference中存入一个String值
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            键值
	 * @param value
	 *            值
	 */
	public static void putString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.PREFERENCE_FILE, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 获取Preference中键值key对应的String值
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            键值
	 * @param defValue
	 *            默认值
	 * @return Preference中key对应的字串
	 */
	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.PREFERENCE_FILE, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	/**
	 * 向Preference中存入一个int值
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            键值
	 * @param value
	 *            值
	 */
	public static void putInt(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.PREFERENCE_FILE, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 获取Preference中键值key对应的整型值
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            键值
	 * @param defValue
	 *            默认值
	 * @return Preference中key对应的整型值
	 */
	public static int getInt(Context context, String key, int defValue) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.PREFERENCE_FILE, Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}

	/**
	 * 向Preference中存入一个boolean值
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            键值
	 * @param value
	 *            值
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.PREFERENCE_FILE, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 获取Preference中键值key对应的boolean值
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            键值
	 * @param defValue
	 *            默认值
	 * @return Preference中key对应的boolean值
	 */
	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.PREFERENCE_FILE, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}

	/**
	 * 向Preference中存入一个boolean值
	 *
	 * @param context
	 *            上下文
	 * @param key
	 *            键值
	 * @param value
	 *            值
	 */
	public static void putLong(Context context, String key, long value) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.PREFERENCE_FILE, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * 获取Preference中键值key对应的boolean值
	 *
	 * @param context
	 *            上下文
	 * @param key
	 *            键值
	 * @param defValue
	 *            默认值
	 * @return Preference中key对应的boolean值
	 */
	public static long getLong(Context context, String key,
									 long defValue) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.PREFERENCE_FILE, Context.MODE_PRIVATE);
		return sp.getLong(key, defValue);
	}

	/**
	 * 清空Preference
	 * 
	 * @param context
	 *            上下文
	 */
	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.PREFERENCE_FILE, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	public static void clearString(Context context, String key){
		putString(context, key, null);
	}

	public static void clearInt(Context context, String key){
		putInt(context, key, -1);
	}

	public static void clearUser(Context context){
		PreferenceUtils.clearString(context, Constant.PREFERENCE_EMP_NAME);
		PreferenceUtils.clearString(context, Constant.PREFERENCE_NUMBER);
		PreferenceUtils.clearString(context, Constant.PREFERENCE_PSW);
		PreferenceUtils.clearInt(context, Constant.PREFERENCE_EMP_ID);
	}

}
