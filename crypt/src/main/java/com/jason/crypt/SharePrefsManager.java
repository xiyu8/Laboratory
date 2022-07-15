package com.jason.crypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;


import java.lang.reflect.Type;
import java.util.Map;

/**
 * 保存和用户uid关联的配置，不同用户配置文件不同
 */
public class SharePrefsManager {

	private static SharePrefsManager sInstance;
	private static CommonSharePrefsManager sCommonInstance;
	private SharedPreferences mManager;

	private static final String SP_NAME = "youban";

	private SharePrefsManager() {
	}

	public static SharePrefsManager getInstance() {
		if (sInstance == null) {
			sInstance = new SharePrefsManager();
		}
		return sInstance;
	}

	public static CommonSharePrefsManager getCommonInstance() {
		if (sCommonInstance == null) {
			sCommonInstance = CommonSharePrefsManager.getInstance();
		}
		return sCommonInstance;
	}




}
