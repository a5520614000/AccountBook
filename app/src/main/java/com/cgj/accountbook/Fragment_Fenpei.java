package com.cgj.accountbook;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.cgj.accountbook.bean.LimitData;
import com.cgj.accountbook.bean.LimitsDatabase;
import com.cgj.accountbook.dao.DatabaseUtil;
import com.cgj.accountbook.dao.MyDataBase;
import com.cgj.accountbook.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Fenpei extends Fragment {

	private static final String TAG = "Fragment_Fenpei_exception";
	private View view;
	private GridView gv;
	private List<LimitsDatabase> mDatas = new ArrayList<>();
	private Handler handler = new Handler();
	private DatabaseUtil databaseUtil;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.content_fenpei, container, false);
			gv = (GridView) view.findViewById(R.id.fenpei_gv);
			new Thread(new Runnable() {
				@Override
				public void run() {
					//初始化数据
					initData();
					handler.post(new Runnable() {

						@Override
						public void run() {
							GvAdapter adapter = new GvAdapter();
							gv.setAdapter(adapter);
						}
					});
				}
			}).start();
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	private void initData() {

		databaseUtil=DatabaseUtil.getInstance();
		mDatas.clear();
		mDatas = databaseUtil.findAll(LimitsDatabase.class);
		LogUtil.logi(TAG,"mDatas："+mDatas.toString());
	}

	private class GvAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mDatas.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			final ViewHolder holder;
			if (arg1 == null) {
				arg1 = LayoutInflater.from(getContext()).inflate(R.layout.content_fenpei_gv_item, (ViewGroup) arg1, false);
				holder = new ViewHolder();
				holder.view = arg1.findViewById(R.id.fenpei_gv_view);
				holder.tv = (TextView) arg1.findViewById(R.id.fenpei_gv_tv);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			//获得预算
			LimitsDatabase data = mDatas.get(arg0);
			holder.view.setBackgroundColor(Color.parseColor(data.getColor()));
			holder.tv.setText(data.getType());
			return arg1;
		}

		class ViewHolder {
			View view;
			TextView tv;
		}
	}
}
