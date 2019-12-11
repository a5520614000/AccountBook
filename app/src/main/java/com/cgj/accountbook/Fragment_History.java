package com.cgj.accountbook;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgj.accountbook.dao.MyDataBase;
import com.cgj.accountbook.util.LogUtil;


public class Fragment_History extends Fragment {

    private static final String TAG = "Fragment_History";
    private static final String TAGL = "Fragment_History_Low";
    private int[] group_checked = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private View view;
    private ExpandableListView e_list;
    private TextView tvEmpty;
    private MyDataBase dataBase;
    private MyCursrTreeAdapter myCursorTreeAdapter;
    //游标集结果，序列用常量表示
    private static final int GROUP_NAME_INDEX = 1;
    private static final int TIME_INDEX = 3;
    private static final int TYPE_INDEX = 1;
    private static final int MONEY_INDEX = 2;
    private static final int MARK_INDEX = 6;
    int groupNameIndex;
    String mygroupName;
    Cursor groupCursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.content_history, container, false);
            tvEmpty = (TextView) view.findViewById(R.id.history_list_empty);
            e_list = (ExpandableListView) view.findViewById(R.id.history_list);
            e_list.setGroupIndicator(null);
            e_list.setEmptyView(tvEmpty);
            dataBase = new MyDataBase(getContext());
            dataBase.open();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        System.out.println(parent != null);
        if (parent != null) {
            parent.removeView(view);
        }
        initAdapterView();
        return view;
    }

    public class MyCursrTreeAdapter extends CursorTreeAdapter {

        public MyCursrTreeAdapter(Cursor cursor, Context context,
                                  boolean autoRequery) {
            super(cursor, context, autoRequery);
        }

        /**
         * 绑定父视图
         *
         * @param view       打气筒视图
         * @param context    上下文
         * @param cursor     游标集
         * @param isExpanded 是否展开状态
         */
        @Override
        protected void bindGroupView(View view, Context context, Cursor cursor,
                                     boolean isExpanded) {
            //绑定组视图：也就是折叠菜单
            TextView group_title = (TextView) view.findViewById(R.id.group_title);
            //从游标集中获得_month
            String group = cursor.getString(GROUP_NAME_INDEX);
            group_title.setText(group);
            TextView groupCount = (TextView) view.findViewById(R.id.group_count);
            //去account表中查之前从游标集中获得的_month对应的总数
            int count = dataBase.getCount(3, "accounts", group);
            groupCount.setText("[" + count + "]");
            //设置最右边箭头，折叠向下，打开向上
            ImageView group_state = (ImageView) view.findViewById(R.id.group_state);
            if (isExpanded) {
                group_state.setBackgroundResource(R.drawable.group_up);
            } else {
                group_state.setBackgroundResource(R.drawable.group_down);
            }
        }

        /**
         * 打气view
         *
         * @param context    上下文
         * @param cursor     游标集
         * @param isExpanded 是否展开状态
         * @param parent
         * @return
         */
        @Override
        protected View newGroupView(Context context, Cursor cursor,
                                    boolean isExpanded, ViewGroup parent) {
            View view = LayoutInflater.from(getContext()).inflate(
                    R.layout.content_history_e_list_parent_item, parent, false);
            // TODO: 2019-12-08 意义不明
//            bindGroupView(view, context, cursor, isExpanded);
            return view;
        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            String groupName = groupCursor.getString(GROUP_NAME_INDEX);
            Cursor childCursor = dataBase.getAccountByGroups(1, groupName);
            return childCursor;
        }

        @Override
        protected View newChildView(Context context, Cursor cursor,
                                    boolean isLastChild, ViewGroup parent) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.content_history_e_list_child_item, parent, false);
            // TODO: 2019-12-08 意义不明
//            bindChildView(view, context, cursor, isLastChild);
            return view;
        }

        @Override
        protected void bindChildView(View view, Context context, Cursor cursor,
                                     boolean isLastChild) {
            //绑定子视图
            TextView time = (TextView) view.findViewById(R.id.child_text_time);
            time.setText(cursor.getString(TIME_INDEX));
            TextView type = (TextView) view.findViewById(R.id.child_text_type);
            type.setTextKeepState(cursor.getString(TYPE_INDEX));
            TextView money = (TextView) view
                    .findViewById(R.id.child_text_money);
            money.setTextKeepState(cursor.getString(MONEY_INDEX) + " "
                    + getString(R.string.rmb));
            TextView mark = (TextView) view.findViewById(R.id.child_text_mark);
            mark.setTextKeepState(cursor.getString(MARK_INDEX));
        }
    }

    private void initAdapterView() {
        // TODO initAdapterView
        //返回结果集的游标
        groupCursor = dataBase.getAccountByGroups(0, "");
        // getActivity().startManagingCursor(groupCursor);
        groupNameIndex = groupCursor.getColumnIndexOrThrow("_month");
        myCursorTreeAdapter = new MyCursrTreeAdapter(groupCursor, getContext(),
                true);
        e_list.setAdapter(myCursorTreeAdapter);
        e_list.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                //被点击的位置的点击数INT++
                // TODO: 2019-12-07 作用未知 的设计？混淆？
                group_checked[groupPosition] = group_checked[groupPosition] + 1;
                //通知线程数据改变
                //				((BaseExpandableListAdapter) myCursorTreeAdapter).notifyDataSetChanged();
                LogUtil.logi(TAGL, "点击了group,所以+1了，现在结果是：" + group_checked[groupPosition]);
                myCursorTreeAdapter.notifyDataSetChanged();
                return false;
            }
        });
        e_list.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                //通知线程数据改变
                //				((BaseExpandableListAdapter) myCursorTreeAdapter).notifyDataSetChanged();
                myCursorTreeAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        groupCursor.close();
        dataBase.close();
    }

}
