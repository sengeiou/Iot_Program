

package com.vtech.vhealth.function.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;


/**
 * 提供统一的log打印工具类
 */
public class LogUtil {

	/** 五种Log日志类型 */

	/** 调试日志类型 */
	public static final int DEBUG = 111;
	/** 错误日志类型 */
	public static final int ERROR = 112;
	/** 信息日志类型 */
	public static final int INFO = 113;
	/** 详细信息日志类型 */
	public static final int VERBOSE = 114;
	/** 警告日志类型 */
	public static final int WARN = 115;

	private static final  boolean LOGON=true;

	public static void show(Class cl, String message){
		if(TextUtils.isEmpty(message)){
			return;
		}
		if(LOGON){
			Log.i(cl.getSimpleName(), message);
		}
	}
	public static void show(String Tag, String Message) {
		if(TextUtils.isEmpty(Message)){
			return;
		}
		if (LOGON) {
			Log.i(Tag, Message);
		}
	}

	public static void show(Context context, String message){
		if(TextUtils.isEmpty(message)){
			return;
		}
		if(LOGON){
			Log.i(context.getClass().getSimpleName(), message);
		}
	}

	public static void show(Object obj, String message){
		if(TextUtils.isEmpty(message)){
			return;
		}
		if(LOGON){
			Log.i(obj.getClass().getSimpleName(), message);
		}
	}

	public static void json(String tag, String message){
		if(TextUtils.isEmpty(message)){
			message = "";
		}
		if (LOGON) {
			Logger.t(tag).json(message);
		}
	}

	public static void xml(String tag, String message){
		if(TextUtils.isEmpty(message)){
			message = "";
		}
		if (LOGON) {
			Logger.t(tag).xml(message);
		}
	}

	public static void logger(String tag, String message){
		if(TextUtils.isEmpty(message)){
			message = "";
		}
		if (LOGON) {
			Logger.t(tag).i(message);
		}
	}


	public static void show(String Tag, String Message, int Style) {
		if(TextUtils.isEmpty(Message)){
			return;
		}
		if (LOGON) {
			switch (Style) {
			case DEBUG: {
				Log.d(Tag, Message);
			}
				break;
			case ERROR: {
				Log.e(Tag, Message);
			}
				break;
			case INFO: {
				Log.i(Tag, Message);
			}
				break;
			case VERBOSE: {
				Log.v(Tag, Message);
			}
				break;
			case WARN: {
				Log.w(Tag, Message);
			}
				break;
			default:
				Log.i(Tag, Message);
				break;
			}
		}
	}
}
