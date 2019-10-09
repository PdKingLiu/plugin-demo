package com.competition.pdking.plugindemo.one;

import android.app.Application;
import android.content.Context;

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
    }

    public static Context getContext() {
        return context;
    }
}
