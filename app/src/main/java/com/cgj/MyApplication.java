package com.cgj;

import android.app.Application;

import org.xutils.x;

public class MyApplication extends Application {

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
	}
}
