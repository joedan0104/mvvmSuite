package com.jindan.tools.log;

import android.util.Log;

import com.jindan.tools.Config;


/**
 * Log统一管理类
 * 
 * @author way
 * 
 */
public class LogUtils {

	private LogUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static final String TAG = "way";

	// 下面四个是默认tag的函数
	public static void i(String msg) {
		if (Config.DEBUG.get())
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (Config.DEBUG.get())
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (Config.DEBUG.get())
			Log.v(TAG, msg);
	}

	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg) {
		if (Config.DEBUG.get())
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (Config.DEBUG.get())
			Log.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (Config.DEBUG.get())
			Log.i(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (Config.DEBUG.get())
			Log.i(tag, msg);
	}

	public static String getLogger (String name) {
		return name;
	}
}