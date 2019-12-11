package com.cgj.accountbook.util;

import android.util.Log;

/**
 * @author onono
 * @version 1
 * @des You can use this class to manege your toast
 * @updateAuthor $
 * @updateDes
 */
public class LogUtil {
    private static boolean LOGSWITCH = true;

    public static void logi(String tag, Object msg) {
        if (LOGSWITCH) {
            Log.i(tag, msg.toString());
        }
    }

    public static void logw(String tag, Object msg) {
        if (LOGSWITCH) {
            Log.w(tag, msg.toString());
        }
    }

    public static void logv(String tag, Object msg) {
        if (LOGSWITCH) {
            Log.v(tag, msg.toString());
        }
    }

    public static void logd(String tag, Object msg) {
        if (LOGSWITCH) {
            Log.d(tag, msg.toString());
        }
    }

    public static void loge(String tag, Object msg) {
        if (LOGSWITCH) {
            Log.e(tag, msg.toString());
        }
    }
}
