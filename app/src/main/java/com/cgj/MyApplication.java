package com.cgj;

import android.app.Application;
import android.util.Log;

import com.cgj.accountbook.bean.AccountDatabase;
import com.cgj.accountbook.bean.GroupsDatabase;
import com.cgj.accountbook.bean.SrzcsDatabase;
import com.cgj.accountbook.dao.DatabaseUtil;
import com.cgj.accountbook.util.LogUtil;

import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
		if (limitsDatas.size()==0){
			databaseUtil.initLimitsDatabase();
			LogUtil.logi(TAG,"初始化limits表成功");
		}
		List<AccountDatabase> all = databaseUtil.findAll(AccountDatabase.class);
		if (all==null){
			databaseUtil.initAccountDatabase();
			LogUtil.logi(TAG,"初始化Account表成功");
		}
		List<GroupsDatabase> group = databaseUtil.findAll(GroupsDatabase.class);
		if (group==null){
			databaseUtil.initGroupDatabase();
			LogUtil.logi(TAG,"初始化Group表成功");
		}
		List srzcs = databaseUtil.findAll(SrzcsDatabase.class);
		if (srzcs==null){
			databaseUtil.initSrzcsDatabase();
			LogUtil.logi(TAG,"初始化Srzcs表成功");
		}
	}
}
