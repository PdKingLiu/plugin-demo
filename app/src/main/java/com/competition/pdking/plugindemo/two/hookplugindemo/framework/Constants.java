package com.competition.pdking.plugindemo.two.hookplugindemo.framework;

import android.os.Environment;

/**
 * Created by vimerzhao
 * Date: 2018/9/30
 * Description:
 */
public class Constants {
    public final static String KEY_IS_PLUGIN = "key_is_plugin";
    public final static String KEY_PACKAGE =  "key_package";
    public final static String KEY_ACTIVITY = "key_activity";

    public final static String FIELD_RESOURCES = "mResources";

    public static final String STUB_ACTIVITY = "com.competition.pdking.plugindemo.FakeActivity";
    public static final String STUB_PACKAGE = "com.competition.pdking.plugindemo";

    public static final String PLUGIN_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/app-debug.apk";

    public final static boolean DEBUG = true;
}
