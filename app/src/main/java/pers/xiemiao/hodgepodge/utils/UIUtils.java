package pers.xiemiao.hodgepodge.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Process;
import android.provider.MediaStore;

import java.util.Hashtable;

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
    public static Handler getHandler() {
        return BaseApplication.getHandler();
    }

    /**
     * 获取主线程ID
     */
    public static long getMainThreadId() {
        return BaseApplication.getMainThreadId();
    }

    /**
     * 执行安全任务，自行判断是否在主线程
     */
    public static void postSafeTask(Runnable task) {
        //调用此方法时，先获取当前线程ID
        int myCurThreadId = Process.myTid();
        //与主线程ID进行比较
        if (myCurThreadId == getMainThreadId()) {
            //如果是线主程，就直接执行务任
            task.run();
        } else {
            //如果是子线程，就用handler去post任务
            getHandler().post(task);
        }
    }

    /**
     * 获取图片缩略图
     */
    public static Bitmap createVideoThumbnail(String filePath, int kind) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://")) {
                retriever.setDataSource(filePath, new Hashtable<String, String>());
            } else {
                retriever.setDataSource(filePath);
            }
            bitmap = retriever.getFrameAtTime(1000);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
                ex.printStackTrace();
            }
        }

        if (bitmap == null)
            return null;

        if (kind == MediaStore.Images.Thumbnails.MINI_KIND) {
            // Scale down the bitmap if it's too large.
            //            int width = bitmap.getWidth();
            //            int height = bitmap.getHeight();
            //            int max = Math.max(width, height);
            //            if (max > 512) {
            //                float scale = 512f / max;
            //                int w = Math.round(scale * width);
            //                int h = Math.round(scale * height);
            //                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            //            }
            int w = ScreenUtil.getScreenWidth(getContext());
            int h = DensityUtils.dp2px(getContext(),250);
            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        } else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                    96,
                    96,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

}
