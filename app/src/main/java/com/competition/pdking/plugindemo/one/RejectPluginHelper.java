package com.competition.pdking.plugindemo.one;

import android.app.ActivityManager;
import android.content.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * @author liupeidong
 * Created on 2019/10/9 10:10
 */
public class RejectPluginHelper {

    //加载插件apk
    public static void loadPlugin(DexClassLoader dexClassLoader, Context applicationContext) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {

        //获取PathClassLoader
        PathClassLoader pathClassLoader = (PathClassLoader) applicationContext.getClassLoader();

        //分别获取宿主和插件的PathList
        Object suzhuPathList = getPathList(pathClassLoader);
        Object chajianPathList = getPathList(dexClassLoader);

        //获得PathList的element并合并
        Object newElements = mergeElements(getElements(suzhuPathList),
                getElements(chajianPathList));

        //重新设置给宿主的dexElement
        Field field = suzhuPathList.getClass().getDeclaredField("dexElements");
        field.setAccessible(true);
        field.set(suzhuPathList, newElements);
    }

    private static Object mergeElements(Object elements1, Object elements2) {
        int len1 = Array.getLength(elements1);
        int len2 = Array.getLength(elements2);
        Object newArr = Array.newInstance(elements1.getClass().getComponentType(), len1 + len2);
        for (int i = 0; i < len1; i++) {
            Array.set(newArr, i, Array.get(elements1, i));
        }
        for (int i = len1; i < len1 + len2; i++) {
            Array.set(newArr, i, Array.get(elements2, i - len1));
        }
        return newArr;
    }

    // 获取DexPathList 中的dexElements
    private static Object getElements(Object suzhuPathList) throws NoSuchFieldException,
            IllegalAccessException {
        Class cl = suzhuPathList.getClass();
        Field field = cl.getDeclaredField("dexElements");
        field.setAccessible(true);
        return field.get(suzhuPathList);
    }

    // 获取DexPathList
    private static Object getPathList(Object loader) throws ClassNotFoundException,
            NoSuchFieldException, IllegalAccessException {
        Class cl = Class.forName("dalvik.system.BaseDexClassLoader");
        Field field = cl.getDeclaredField("pathList");
        field.setAccessible(true);
        return field.get(loader);
    }

}
