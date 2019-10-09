package com.competition.pdking.plugindemo.one;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.competition.pdking.plugindemo.R;
import com.competition.pdking.plugindemo.one.ams.HookHelper;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_target:
                goTarget();
                break;
            case R.id.hook:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            hook();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    private void goTarget() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.competition.pdking.projecttest", "com" +
                ".competition.pdking.projecttest.TargetActivity"));
        startActivity(intent);
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
        HookHelper.hookActivityManager();
        HookHelper.hookActivityThread();
    }


}
