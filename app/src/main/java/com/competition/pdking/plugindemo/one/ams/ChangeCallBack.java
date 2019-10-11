package com.competition.pdking.plugindemo.one.ams;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.Field;

/**
 * @author liupeidong
 * Created on 2019/10/9 13:05
 */
public class ChangeCallBack implements Handler.Callback {

    private Handler handler;

    public ChangeCallBack(Handler handler) {
        this.handler = handler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 100:
                try {
                    handleLaunchActivity(msg);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
        }
        handler.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) throws NoSuchFieldException,
            IllegalAccessException {
        Object obj = msg.obj;

        Field intent = obj.getClass().getDeclaredField("intent");
        intent.setAccessible(true);
        Intent fakeIntent = (Intent) intent.get(obj);
        Intent targetIntent = fakeIntent.getParcelableExtra("targetIntent");
//        fakeIntent.setComponent(targetIntent.getComponent());
        intent.set(obj, targetIntent);
    }
}
