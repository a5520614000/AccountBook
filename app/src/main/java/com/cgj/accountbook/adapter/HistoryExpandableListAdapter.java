package com.cgj.accountbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.PopupWindow;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.cgj.accountbook.R;
import com.cgj.accountbook.bean.AccountData;
import com.cgj.accountbook.bean.AccountDatabase;
import com.cgj.accountbook.bean.GroupsDatabase;
import com.cgj.accountbook.bean.MyStringUtils;
import com.cgj.accountbook.dao.DatabaseUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author onono
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
//todo 完成adapter
public class HistoryExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<GroupsDatabase> group;
    private List<AccountDatabase> child;
    private DatabaseUtil databaseUtil;

    public HistoryExpandableListAdapter(Context context, List group, DatabaseUtil databaseUtil) {
        mContext = context;
        this.group = group;
        this.databaseUtil = databaseUtil;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        getChildList(groupPosition);
        return child.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        getChildList(groupPosition);
        return child.get(childPosition);
    }

    private void getChildList(int groupPosition) {
        String month = group.get(groupPosition).getMonth();
        child = databaseUtil.getData(AccountDatabase.class, "_month", month);
        //排序
        Collections.sort(child, new Comparator<AccountDatabase>() {
            @Override
            public int compare(AccountDatabase o1, AccountDatabase o2) {
                long t1 = MyStringUtils.dataToMills(o1.getTime());
                long t2 = MyStringUtils.dataToMills(o2.getTime());
                return t1-t2>0?1:-1;
            }
        });
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupsDatabase groupsDatabase = group.get(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_history_e_list_parent_item, parent, false);
        }
        TextView tv_history_title = convertView.findViewById(R.id.group_title);
        TextView tv_history_count = convertView.findViewById(R.id.group_count);
        tv_history_title.setText(groupsDatabase.getMonth());
        tv_history_count.setText("[ "+getChildrenCount(groupPosition)+" ]");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        AccountDatabase child = (AccountDatabase) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_history_e_list_child_item, parent, false);
        }
        TextView tv_history_child_time = convertView.findViewById(R.id.child_text_time);
        TextView tv_history_child_mark = convertView.findViewById(R.id.child_text_mark);
        TextView tv_history_child_money = convertView.findViewById(R.id.child_text_money);
        TextView tv_history_child_type = convertView.findViewById(R.id.child_text_type);
        tv_history_child_mark.setText(child.getMark());
        tv_history_child_money.setText(child.getMoney());
        tv_history_child_time.setText(child.getTime());
        tv_history_child_type.setText(child.getType());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
