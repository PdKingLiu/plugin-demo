package com.competition.pdking.plugindemo.one;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.competition.pdking.plugindemo.one.ams.HookHelper;
import com.competition.pdking.plugindemo.two.hookplugindemo.framework.PluginManager;
import com.competition.pdking.plugindemo.two.hookplugindemo.framework.ReflectUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;

/**
 * @author liupeidong
 * Created on 2019/10/9 11:31
 */
public class App extends Application {

    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = base;
//        initPlugin();
    }

    private PluginManager mPluginManager;


    private void initPlugin() {
        ReflectUtil.init();
        mPluginManager = PluginManager.getInstance(context);
        mPluginManager.hookInstrumentation();
//        mPluginManager.hookCurrentActivityInstrumentation(this);
    }

    public static Context getContext() {
        return context;
    }
    private void hook() throws IllegalAccessException, NoSuchFieldException,
            ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        String cachePath = getCacheDir().getAbsolutePath();
        String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/chajian" +
                ".apk";
        File file = new File(apkPath);
        Log.d("Lpp", "hook: " + file.exists());
        DexClassLoader dexClassLoader = new DexClassLoader(apkPath, cachePath, cachePath,
                getClassLoader());
        RejectPluginHelper.loadPlugin(dexClassLoader, getApplicationContext());
        HookHelper.hookActivityManager(this);
        HookHelper.hookActivityThread();
    }
}
