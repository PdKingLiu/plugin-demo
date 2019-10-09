package com.competition.pdking.plugindemo.one.ams;

import android.content.ComponentName;
import android.content.Intent;

import com.competition.pdking.plugindemo.one.App;
import com.competition.pdking.plugindemo.one.FakeActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author liupeidong
 * Created on 2019/10/9 11:08
 */
public class IActivityManagerProxy implements InvocationHandler {

    private Object obj;

    public IActivityManagerProxy(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("startActivity")) {
            //获得传入的Intent
            Intent targetIntent = null;
            int index;

            for (index = 0; index < args.length; index++) {
                if (args[index] instanceof Intent) {
                    targetIntent = (Intent) args[index];
                    break;
                }
            }

            //创建假Intent
            Intent fakeIntent = new Intent();
            String packageName = App.getContext().getPackageName();
            ComponentName componentName = new ComponentName(packageName,
                    FakeActivity.class.getName());
            fakeIntent.setComponent(componentName);
            //把真的先存里面
            fakeIntent.putExtra("targetIntent", targetIntent);

            //设置到参数里面
            args[index] = fakeIntent;
            return method.invoke(obj, args);
        }
        return method.invoke(obj, args);
    }
}
