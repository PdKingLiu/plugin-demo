package com.competition.pdking.plugindemo.one.ams;

import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author liupeidong
 * Created on 2019/10/9 10:55
 */
public class HookHelper {

    public static void hookActivityManager() throws ClassNotFoundException, NoSuchFieldException,
            IllegalAccessException {

        //得到ActivityManagerNative的class对象
        Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
        //得到他的gDefault字段，他是Singleton类型
        Field field = activityManagerNativeClass.getDeclaredField("gDefault");
        field.setAccessible(true);
        //静态直接传null即可
        Object gDefaultObj = field.get(null);

        //得到Singleton class
        Class singletonCLass = Class.forName("android.util.Singleton");
        //得到他的成员mInstance 他是IActivityManager
        Field mInstanceField = singletonCLass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        //得到gDefault的 mInstance
        Object activityManagerObj = mInstanceField.get(gDefaultObj);

        //给IActivityManager设置代理
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{Class.forName("android.app.IActivityManager")},
                new IActivityManagerProxy(activityManagerObj));

        //将代理对象设置给gDefault
        mInstanceField.set(gDefaultObj, proxy);
        Log.d("Lpp", "已经替换掉了Intent");
    }

    public static void hookActivityThread() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, NoSuchFieldException {

        //先得到ActivityThread对象，他有一个返回自己本身的方法
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        Object activityThreadObj = currentActivityThreadMethod.invoke(null);

        //得到他的成员 mH
        Field mHField = activityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        Object handleObj = mHField.get(activityThreadObj);

        //给mH设置mCallback
        Field callBackField = Handler.class.getDeclaredField("mCallback");
        callBackField.setAccessible(true);
        callBackField.set(handleObj, new ChangeCallBack((android.os.Handler) handleObj));
        Log.d("Lpp", "替换回Intent");
    }

}
