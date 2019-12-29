package com.cgj;

import android.app.Application;
import android.util.Log;

import com.cgj.accountbook.dao.DatabaseUtil;
import com.cgj.accountbook.util.LogUtil;

import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

public class MyApplication extends Application {

	private static final String TAG = "Application-exception";
	public String verflag;

	public String getVerflag() {
		return verflag;
	}

	public void setVerflag(String verflag) {
		this.verflag = verflag;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		x.Ext.init(this);
		x.Ext.setDebug(false);
		initDatabase();
	}

	private void initDatabase() {

		DatabaseUtil databaseUtil = DatabaseUtil.getInstance();
		ArrayList<HashMap<String, String>> limitsDatas = databaseUtil.getPartLimitsDatas();
		LogUtil.logi(TAG,"limits表是否存在："+ limitsDatas.size());

		if (limitsDatas.size()==0){
			databaseUtil.initLimitsDatabase();
			LogUtil.logi(TAG,"初始化limits表成功");
		}
	}
}
