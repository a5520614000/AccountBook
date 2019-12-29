package com.cgj.accountbook.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cgj.accountbook.MainActivity;
import com.cgj.accountbook.R;
import com.cgj.accountbook.bean.AccountData;
import com.cgj.accountbook.bean.AccountDatabase;
import com.cgj.accountbook.dao.DatabaseUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * @author onono
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class MyDB extends Activity {
    String str = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydbtest);
        final TextView tv = findViewById(R.id.textView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DbManager dbManager = DatabaseUtil.getInstance();
                ////                try {
                ////                    AccountDatabase accountDatabase = new AccountDatabase();
                ////                    accountDatabase.setType("今天好幸福");
                ////                    tv.setText(accountDatabase.toString());
                ////                    dbManager.save(accountDatabase);
                ////                } catch (DbException e) {
                ////                    e.printStackTrace();
                ////                }
                Toast.makeText(getApplicationContext(), "你好", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {


            private List<AccountDatabase> mAll;

            @Override
            public void onClick(View v) {
//                DbManager db = DatabaseUtil.getInstance();
//                str = "";
//                try {
//                    mAll = db.findAll(AccountDatabase.class);
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
//                for (int i = 0; i < mAll.size(); i++) {
//                    str += mAll.get(i).toString()+"======";
//                    tv.setText(str);
//                }
            }
        });
    }
}
