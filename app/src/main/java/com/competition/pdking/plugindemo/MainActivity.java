package com.competition.pdking.plugindemo;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.competition.pdking.plugindemo.one.RejectPluginHelper;
import com.competition.pdking.plugindemo.one.ams.HookHelper;
import com.competition.pdking.plugindemo.two.hookplugindemo.framework.Constants;
import com.competition.pdking.plugindemo.two.hookplugindemo.framework.PluginManager;
import com.competition.pdking.plugindemo.two.hookplugindemo.framework.ReflectUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
            }
        }
        initPlugin();
    }

    private PluginManager mPluginManager;

    private void initPlugin() {
        ReflectUtil.init();
        mPluginManager = PluginManager.getInstance(getApplicationContext());
        mPluginManager.hookInstrumentation();
//        mPluginManager.hookCurrentActivityInstrumentation(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_target:
//                goTarget();
                Log.d("Lpp", "MainActivity: " + this);
                Log.d("Lpp", "MainActivity getApplicationContext: " + this.getApplicationContext());
                if (mPluginManager.loadPlugin(Constants.PLUGIN_PATH)) {
                    Intent intent = new Intent();
                    intent.setClassName("com.plugin.demo.plugindemo2", "com.plugin.demo" +
                            ".plugindemo2.TargetActivity");
                    getApplicationContext().startActivity(intent);
                }
                break;
            case R.id.hook:
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }


    private void goTarget() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.plugin.demo.plugindemo2", "com.plugin.demo" +
                ".plugindemo2.TargetActivity"));
        getApplicationContext().startActivity(intent);
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
