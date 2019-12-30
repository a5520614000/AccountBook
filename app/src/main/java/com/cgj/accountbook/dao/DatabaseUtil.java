package com.cgj.accountbook.dao;

import com.cgj.accountbook.bean.LimitsDatabase;
import com.cgj.accountbook.bean.MyStringUtils;
import com.cgj.accountbook.bean.SrzcsDatabase;
import com.cgj.accountbook.util.LogUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onono
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class DatabaseUtil {

    private static final int VERSION = 1;
    private static final String DBNAME = "myAccountTest.db";
    private static final String TAG = "DatabaseUtil_Exception";
    private static DatabaseUtil mDatabaseUtil;
    private static DbManager db;
    public static final int SRZCS_INCOME = 0;
    public static final int SRZCS_OUTPUT = 1;
    public static final int SRZCS_OTHER = 2;

    private DatabaseUtil() {
    }

    public synchronized static DatabaseUtil getInstance() {
        if (mDatabaseUtil == null) {
            mDatabaseUtil = new DatabaseUtil();
            db = mDatabaseUtil.getDbManager();
        }
        return mDatabaseUtil;
    }

    private DbManager getDbManager() {
        if (db == null) {
            initDbManager();
        }
        return db;
    }

    private void initDbManager() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName(DBNAME)
                .setDbVersion(VERSION)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) throws DbException {
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) throws DbException {
                        // TODO: 2019-12-25 升级数据库操作
                    }
                });
        try {
            db = x.getDb(daoConfig);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public int save(Object obj) {
        try {
            db.save(obj);
            return 1;
        } catch (DbException e) {
            LogUtil.logw(TAG, TAG + ":保存失败");
            return -1;
        }
    }

    /**
     * 查找：entityType表内type列是否含有condition的值
     *
     * @param entityType 表的类
     * @param type       需要查找的列名
     * @param condition  需要查找的type列内的值
     * @return true 找到，false 其他
     */
    public boolean isNameExist(Class<?> entityType, String type, String condition) {
        List<?> all = null;
        try {
            all = db.selector(entityType).where(type, "=", condition).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (all != null && all.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 修改：更新limits表的limit值（更新预算）
     *
     * @param condition 用于查找需要修改的行，（type的值）
     * @param limit     预算
     * @param use       金额
     * @return 1成功 0失败
     */
    public int updateDatetoLimitsLimit(String condition, String limit, String use) {
        float l = MyStringUtils.getString2Float(limit);
        float u = MyStringUtils.getString2Float(use);
        List<?> type = null;
        try {
            type = db.selector(LimitsDatabase.class).where("_type", "=", condition).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (type != null) {
            if (l != 0) {
                LimitsDatabase obj = (LimitsDatabase) type.get(0);
                int pro = (int) ((u / l) * 100);
                obj.setProgress(Integer.toString(pro));
                obj.setLimit(limit);
                try {
                    db.update(obj, "_limit", "_progress");
                    return 1;
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        } else {
            LogUtil.logi(TAG, TAG + ":更新limit表的limit失败");
        }
        return 0;
    }

    /**
     * 修改：更新limits表的use值（更新使用的钱）
     *
     * @param condition 用于查找需要修改的行，（type的值）
     * @param used      金额
     */
    public void updateDatetoLimitsUsed(String condition, float used) {
        List<?> type = null;
        try {
            type = db.selector(LimitsDatabase.class).where("_type", "=", condition).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (type != null) {
            LimitsDatabase obj = (LimitsDatabase) type.get(0);
            String used1 = MyStringUtils.get2dotFloat(used);
            //            obj.setType(condition);
            obj.setUsed(used1);
            try {
                db.update(obj, "_used");
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else {
            LogUtil.logi(TAG, TAG + ":更新limit表的used失败");
        }

    }

    /**
     * 获得srzcr表的month月a列的值
     *
     * @param a     哪一列
     * @param month 哪个月
     * @return
     */
    public String getSRGL(int a, String month) {
        String s = "0";
        List<?> list = null;
        try {
            list = db.selector(SrzcsDatabase.class).where("_month", "=", month).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (list != null) {
            SrzcsDatabase o = (SrzcsDatabase) list.get(0);
            switch (a) {
                case SRZCS_INCOME:
                    s = o.getSr();
                    break;
                case SRZCS_OUTPUT:
                    s = o.getZc();
                    break;
                case SRZCS_OTHER:
                    s = o.getOther();
                    break;
                default:
                    break;
            }
            return s;
        } else {
        }
        if (s == null) {
            return "0";
        } else {
            return s;
        }

    }

    /**
     * 更新srzcr表
     *
     * @param month 月份
     * @param t     哪一列
     * @param value 值
     */
    public void updateGLSR(String month, int t, String value) {

        List<SrzcsDatabase> all = null;
        try {
            all = db.selector(SrzcsDatabase.class).where("_month", "=", month).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (all != null) {
            SrzcsDatabase obj = all.get(0);
            switch (t) {
                case SRZCS_INCOME:
                    obj.setSr(value);
                    try {
                        db.update(obj, "_sr");
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    break;
                case SRZCS_OUTPUT:
                    obj.setZc(value);
                    try {
                        db.update(obj, "_zc ");
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    break;
                case SRZCS_OTHER:
                    obj.setOther(value);
                    try {
                        db.update(obj, "_other");
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 获取limits表的数据
     *
     * @return arraylist, 内含hashmap内“String，String”
     */
    public ArrayList<HashMap<String, String>> getPartLimitsDatas() {
        //查找limits表下所有内容
        List<LimitsDatabase> all = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            all = db.findAll(LimitsDatabase.class);

            if (all != null) {
                for (int i = 0; i < all.size(); i++) {
                    HashMap<String, String> stringStringHashMap = new HashMap<>();
                    String type = all.get(i).getType();
                    String color = all.get(i).getColor();
                    String used = all.get(i).getUsed();
                    String progress = all.get(i).getProgress();
                    String limit = all.get(i).getLimit();
                    int id = all.get(i).getId();
                    stringStringHashMap.put("id", id + "");
                    stringStringHashMap.put("limit", limit);
                    stringStringHashMap.put("progress", progress);
                    stringStringHashMap.put("used", used);
                    stringStringHashMap.put("type", type);
                    stringStringHashMap.put("color", color);
                    list.add(stringStringHashMap);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 初始化limits表
     */
    public void initLimitsDatabase() {
        for (int i = 0; i < MyStringUtils.templimitsname.length; i++) {
            LimitsDatabase limitsDatabase = new LimitsDatabase();
            limitsDatabase.setType(MyStringUtils.templimitsname[i]);
            limitsDatabase.setProgress("0");
            limitsDatabase.setUsed("0");
            limitsDatabase.setColor(MyStringUtils.templimitscolor[i]);
            limitsDatabase.setLimit("0");
            try {
                db.save(limitsDatabase);
            } catch (DbException e) {
                LogUtil.logw(TAG, "M:initLimitsDatabse：" + e);
            }
        }
    }

    /**
     * 获取首页展示时的数据
     *
     * @return
     */
    public ArrayList<Map<String, Object>> getHomeData() {
        int count = 0;//当月总消费
        int pro = 0;//当月消费占比
        Map<String, Object> map;
        //当月总消费计算
        ArrayList<Map<String, Object>> datas = new ArrayList<>();
        ArrayList<HashMap<String, String>> partLimitsDatas = getPartLimitsDatas();
        for (int i = 0; i < partLimitsDatas.size(); i++) {
            String u = partLimitsDatas.get(i).get("used");
            if (u != null) {
                int pro1 = (int) ((Float.parseFloat(u) / count) * 100);
                map = new HashMap<>();
                float used = Float.parseFloat(u);
                count += used;
                map.put("used", u + "");
                map.put("color", partLimitsDatas.get(i).get("color"));
                map.put("type", partLimitsDatas.get(i).get("type"));
                map.put("pro", String.valueOf(pro1));
                datas.add(map);

            }
        }
        LogUtil.logi(TAG, "：总花费:" + count);
        return datas;
    }

    /**
     * 获取进度或限额
     *
     * @param a
     * @param type
     * @return
     */
    public String getProORLimit(String a, String type) {
        String s = "0";
        LimitsDatabase first = null;
        try {
            first = db.selector(LimitsDatabase.class).where("_type", "=", type).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        switch (a) {
            case "used":
                s = first.getUsed();
                break;
            case "limit":
                s = first.getLimit();
                break;
            case "pro":
                s = first.getProgress();
                break;
            default:
                break;
        }
        if (s == null) {
            return "0";
        } else {
            return s;
        }

    }

    /**
     * 查询特定表中，type=condition的数据总数
     *
     * @param entityType 表的类
     * @param type       列名
     * @param condition  条件
     * @return int总数
     */
    public int getCount(Class entityType, String type, String condition) {
        int count = 0;
        List all = null;
        try {
            all = db.selector(entityType).where(type, "=", condition).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (all != null) {
            return all.size();
        } else {
            return 0;
        }
    }

    /**
     * 查询特定表中，type=condition的对象
     *
     * @param entityType 表的类
     * @param type       列名
     * @param condition  条件
     * @return List符合条件的对象
     */
    public List getData(Class entityType, String type, String condition) {
        List all = null;
        try {
            all = db.selector(entityType).where(type, "=", condition).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (all != null) {
            return all;
        } else {
            return null;
        }
    }

    public List findAll(Class entityType) {
        List all = null;
        try {
            all = db.findAll(entityType);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (all != null) {
            return all;
        } else {
            return null;
        }
    }
}
