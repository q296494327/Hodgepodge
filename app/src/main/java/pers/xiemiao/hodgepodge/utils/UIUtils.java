package pers.xiemiao.hodgepodge.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Process;

import pers.xiemiao.hodgepodge.base.BaseApplication;


/**
 * User: xiemiao
 * Date: 2016-10-04
 * Time: 21:30
 * Desc: 创建一些UI相关的工具类
 */
public class UIUtils {
    /**
     * 获取上下文
     */
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    /**
     * 获取资源对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 根据资源ID获取string.xml下的字符串
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 根据资源ID获取string.xml下的字符串数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 根据资源ID获取colors.xml下的颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }


    /**
     * 获取应用包名
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 获取消息管理器
     */
    public static Handler getHandler(){
        return BaseApplication.getHandler();
    }

    /**
     * 获取主线程ID
     */
    public static long getMainThreadId(){
        return BaseApplication.getMainThreadId();
    }

    /**
     * 执行安全任务，自行判断是否在主线程
     */
    public static void postSafeTask(Runnable task){
        //调用此方法时，先获取当前线程ID
        int myCurThreadId = Process.myTid();
        //与主线程ID进行比较
        if(myCurThreadId==getMainThreadId()){
            //如果是线主程，就直接执行务任
            task.run();
        }else {
            //如果是子线程，就用handler去post任务
            getHandler().post(task);
        }
    }
}
