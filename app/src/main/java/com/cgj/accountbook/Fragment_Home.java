package com.cgj.accountbook;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgj.accountbook.adapter.HomeAccountAdapter;
import com.cgj.accountbook.bean.AccountData;
import com.cgj.accountbook.bean.AccountDatabase;
import com.cgj.accountbook.bean.MyStringUtils;
import com.cgj.accountbook.dao.DatabaseUtil;
import com.cgj.accountbook.dao.MyDataBase;
import com.cgj.accountbook.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//主页碎片
public class Fragment_Home extends Fragment {

	private static final int MSG_GETDATAS_DONE = 0x1;
	private View view, frame_home_head;
	private TextView tv_count, tv_info, tv_month;
	private ListView lisView;
	private HomeAccountAdapter adapter;
	private String month, count, info;
	private MyHandler myHandler;
	private ArrayList<Map<String, Object>> datas = new ArrayList<>();
	private List<Integer> l = new ArrayList<>();
	private DatabaseUtil databaseUtil;
	private String TAG = "Fragment_Home_Exception";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			databaseUtil = DatabaseUtil.getInstance();
			view = inflater.inflate(R.layout.content_home, container, false);
			frame_home_head = view.findViewById(R.id.frame_home_head);
			tv_count = (TextView) view.findViewById(R.id.frame_home_tv_count);
			tv_info = (TextView) view.findViewById(R.id.frame_home_tv_info);
			tv_month = (TextView) view.findViewById(R.id.frame_home_tv_month);
			Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Bold.ttf");
			tv_count.setTypeface(typeFace);
			lisView = (ListView) view.findViewById(R.id.frame_home_lv);
			TextView empty = (TextView) view.findViewById(R.id.frame_home_lv_empty);
			lisView.setEmptyView(empty);
			lisView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					LogUtil.logi(TAG, "位置:"+position);
					Toast.makeText(getContext(), "homelist" + position, Toast.LENGTH_SHORT).show();
				}
			});

		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				datas = databaseUtil.getHomeData();
				getHeadInfo();
				handler.sendEmptyMessage(MSG_GETDATAS_DONE);
			}
		}).start();
		return view;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_GETDATAS_DONE:
				adapter = new HomeAccountAdapter(datas, getContext());
				lisView.setAdapter(adapter);
				tv_month.setText(month);
				tv_count.setText(count);
				tv_info.setText(info);
				for (int i = 0; i < datas.size(); i++) {
					l.add(i, datas.get(i).get("pro").hashCode());
				}
				myHandler = new MyHandler();
				for (int i = 0; i < datas.size(); i++) {
					new Thread(new UpdateRunnable(i, 40, myHandler, l.get(i)))
							.start();
				}
//				dataBase.close();
				break;
			}
		}
	};

	class UpdateRunnable implements Runnable {
		int id;
		int currentPos = 0;
		int delay;
		MyHandler handler;
		int pos;

		public UpdateRunnable(int id, int delay, MyHandler handler, int pos) {
			this.id = id;
			this.handler = handler;
			this.delay = delay;
			this.pos = pos;
		}

		@Override
		public void run() {
			while (currentPos <= pos) {
				Message msg = handler.obtainMessage();
				msg.what = 1;
				msg.arg1 = id;
				msg.arg2 = currentPos;
				currentPos = currentPos + 1;
				msg.sendToTarget();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int id = msg.arg1;
				int current = msg.arg2;
				updateProgress(id, current);
				break;
			}
			super.handleMessage(msg);
		}

		private void updateProgress(int id, int press) {
			Map<String, Object> dataPress = datas.get(id);
			dataPress.put("pro", press);
			adapter.changeProgress(id, dataPress);
		}
	}

	/**
	 * 获取顶部信息
	 */
	protected void getHeadInfo() {
		month = MyStringUtils.getSysNowTime(3);
		float c = 0;
		List<AccountDatabase> used = databaseUtil.getData(AccountDatabase.class, "_month", month);
		for (int i = 0; i < used.size(); i++) {
			String money = used.get(i).getMoney();
			c+=Float.parseFloat(money);
			LogUtil.logi(TAG,"总计消费了:"+c);
		}
		if (c == 0.0) {
			count = "0.00 " + getString(R.string.rmb);
		} else {
			count = MyStringUtils.get2dotFloat(c) + " " + getString(R.string.rmb);
		}
		if (!(count.equals("0") || count == null)) {
			float limit = Float.valueOf(MyStringUtils.readSharedpre(getContext(), 1));
			if (limit != 0) {
				float surplus = limit - c;
				String sur = MyStringUtils.get2dotFloat(surplus);
				info = sur + getString(R.string.rmb) + "剩余，直到您达到每月限额";
				if (surplus == 0)
					info = "0.00" + getString(R.string.rmb) + "剩余，刚好达到每月限额";
				if (surplus < 0) {
					frame_home_head.setBackgroundColor(Color.RED);
					info = "剩余" + sur + getString(R.string.rmb)
							+ "，已超出每月限额，请省着点";
				}
			} else {
				info = "暂时还没有设置每月限额";
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
