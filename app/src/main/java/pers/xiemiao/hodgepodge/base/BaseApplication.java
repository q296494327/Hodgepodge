package pers.xiemiao.hodgepodge.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * User: xiemiao
 * Date: 2016-10-04
 * Time: 21:22
 * Desc: 基类应用入口，可以在此处初始化一些全局所要用到的东西
 */
public class BaseApplication extends Application {


    private static Context mContext;
    private static Thread mMainThread;
    private static int mMainThreadId;
    private static Handler mHandler;

    @Override
    public void onCreate() {
        //禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        //初始化okhttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        //获取上下文
        mContext = getApplicationContext();
        //获取主线程
        mMainThread = Thread.currentThread();
        //获取主线程ID
        mMainThreadId = Process.myTid();
        //获取消息对象
        mHandler = new Handler();
        //初始化Fresco
        Fresco.initialize(this);
        //初始化讯飞语音
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=57fc6d18");
        super.onCreate();
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }
}
