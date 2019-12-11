package com.cgj.accountbook;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cgj.accountbook.act.ActivityAbout;
import com.cgj.accountbook.act.ActivityAddAccount;
import com.cgj.accountbook.act.ActivitySetting;
import com.cgj.accountbook.bean.CheckUpdate;
import com.cgj.MyApplication;
import com.cgj.accountbook.bean.MyStringUtils;
import com.zinc.libpermission.utils.JPermissionUtil;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public View content;
    private static Boolean isExit = false;
    private boolean isInit;//“初始化”标记
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    private Map<String, String> map = new HashMap<String, String>();
    private Handler handler = new Handler();
    private String isSame = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //检查是否已经初始化过界面，初始化即跳过之后。
        if (isInit) {
            return;
        }
        //检查权限
        JPermissionUtil.requestAllPermission(this);
        //初始化界面
        initView();
        //检查更新 
        // TODO: 2019-12-11 暂时屏蔽掉更新，测试好 
        checkUpdate();
        //“初始化”标记 为 TRUE；
        isInit = true;
    }


    private void showUpdateDialog() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialog = layoutInflater.inflate(R.layout.dialog_update, (ViewGroup) findViewById(R.id.update_dialog));
        TextView ver = (TextView) dialog.findViewById(R.id.update_dialog_tv_ver);
        TextView log = (TextView) dialog.findViewById(R.id.update_dialog_tv_log);
        TextView size = (TextView) dialog
                .findViewById(R.id.update_dialog_tv_size);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发现新版本");
        ver.setText(map.get("versionShort"));
        log.setText(map.get("changelog"));
        size.setText(map.get("fsize"));
        builder.setView(dialog);
        builder.setPositiveButton("立即更新",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downLoadApk();
                    }
                });
        builder.setNegativeButton("下次来吧", null);
        builder.show();

    }

    private void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    protected void downLoadApk() {
        final ProgressDialog pd;// 进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载新版本");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            File file;
                            try {
                                file = CheckUpdate.getFileFromServer(
                                        map.get("install_url"), pd);
                                sleep(1000);
                                installApk(file);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    pd.dismiss();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "更新失败，请稍后重试！",
                            Toast.LENGTH_LONG).show();
                }
            }
        }.start();
    }

    /**
     * 运行一个线程去APP是否需要检查更新
     */
    private void checkUpdate() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    map = CheckUpdate.getNewVersion();
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            MyApplication application = (MyApplication) getApplicationContext();
                            isSame = MyStringUtils.showVersion(
                                    MainActivity.this, "getcode");
                            if (isSame.equals(map.get("version"))) {
                                application.setVerflag("已是最新版本");
                            } else {
                                showUpdateDialog();
                                application.setVerflag("发现新版本 "
                                        + map.get("versionShort"));
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void initView() {
        //绑定UI组件
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nv_main_navigation);
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        content = findViewById(R.id.frame_content);
        //使用系toolbar替换掉系统知道的actionbar
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        //断言 等于 if ab==null——>抛异常
        assert ab != null;
        //添加一个“三横线”的logo放在actionbar上，并可以“点击？”
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        // TODO: 2019-12-07 自定义方法
        setupDrawerContent(mNavigationView);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        initPreView();
    }

    private void initPreView() {
        //第一次启动
        String run = MyStringUtils.readSharedpre(MainActivity.this, 0);
        if (run.equals("0")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,
                    new Fragment_Home_None()).commit();
            toolbar.setTitle(getString(R.string.app_name));
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_content, new Fragment_Home()).commit();
            toolbar.setTitle(getString(R.string.nav_home));
            MenuItem mItem = mNavigationView.getMenu().getItem(0);
            mItem.setChecked(true);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView
                .setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //关掉侧滑栏
                        mDrawerLayout.closeDrawers();
                        //判断点击的项目
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                initPreView();
                                break;
                            //打开账单历史碎片
                            case R.id.nav_history:
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,
                                        new Fragment_History()).commit();
                                toolbar.setTitle(getString(R.string.nav_history));
                                break;
                            //打开财政预算碎片
                            case R.id.nav_yusuan:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.frame_content,
                                                new Fragment_Yusuan()).commit();
                                toolbar.setTitle(getString(R.string.nav_yusuan));
                                break;
                            //打开使用情况页面
                            case R.id.nav_fenpei:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.frame_content,
                                                new Fragment_Fenpei()).commit();
                                toolbar.setTitle(getString(R.string.nav_fenpei));
                                break;

                            //                            case R.id.nav_count:
                            //                                getSupportFragmentManager()
                            //                                        .beginTransaction()
                            //                                        .replace(R.id.frame_content,
                            //                                                new Fragment_Count()).commit();
                            //                                toolbar.setTitle(getString(R.string.nav_count));
                            //                                break;

                            //打开关于菜单，一个新的activity
                            case R.id.nav_about:
                                startActivity(new Intent(MainActivity.this,
                                        ActivityAbout.class));
                                break;
                            //点击退出
                            case R.id.nav_exit:
                                MainActivity.this.finish();
                                System.exit(0);
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        initPreView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.tool_add) {
            startActivityForResult(new Intent(MainActivity.this,
                    ActivityAddAccount.class), 1);
            return true;
        }
        if (item.getItemId() == R.id.tool_setting) {
            startActivityForResult(new Intent(MainActivity.this,
                    ActivitySetting.class), 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    public void open() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    private void exitBy2Click() {
        Timer tExit;
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            open();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            this.finish();
            System.exit(0);
        }
    }
}
