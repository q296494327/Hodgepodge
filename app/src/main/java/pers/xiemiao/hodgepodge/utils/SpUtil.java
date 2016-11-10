package pers.xiemiao.hodgepodge.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
    private static SharedPreferences sp;

    /**
     * 写入boolean变量至sp中
     *
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值 boolean
     */
    public static void putBoolean(Context ctx, String key, boolean value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 读取boolean标示从sp中
     *
     * @param ctx      上下文环境
     * @param key      存储节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    /**
     * 写入boolean变量至sp中
     *
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值string
     */
    public static void putString(Context ctx, String key, String value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    /**
     * 读取boolean标示从sp中
     *
     * @param ctx      上下文环境
     * @param key      存储节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static String getString(Context ctx, String key, String defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }


    /**
     * 写入boolean变量至sp中
     *
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值string
     */
    public static void putInt(Context ctx, String key, int value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).commit();
    }

    /**
     * 读取boolean标示从sp中
     *
     * @param ctx      上下文环境
     * @param key      存储节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static int getInt(Context ctx, String key, int defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }


    /**
     * 从sp中移除指定节点
     *
     * @param ctx 上下文环境
     * @param key 需要移除节点的名称
     */
    public static void remove(Context ctx, String key) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }


    /**
     * 存字符串数组到sp里
     *
     * @param ctx    上下文
     * @param key    节点名
     * @param strArr 字符串数组
     */
    public static void putStringArr(Context ctx, String key, String[] strArr) {
        String titles = "";
        //先将字符串数组遍历成字符串
        for (int i = 0; i < strArr.length; i++) {
            if (i == strArr.length - 1) {
                titles += strArr[i];
            } else {
                titles += strArr[i] + ",";
            }
        }
        putString(ctx, key, titles);
    }

    /**
     * 从sp里取出字符串数组
     *
     * @param ctx       上下文
     * @param key       节点名
     * @param defValues 默认的字符串数组
     */
    public static String[] getStringArr(Context ctx, String key, String[] defValues) {
        //取出sp存储的字符串
        String titles = getString(ctx, key, "");
        if (titles.equals("")) {
            return defValues;
        } else {
            //根据","分割字符串成数组
            String[] strings = titles.split(",");
            return strings;
        }
    }


}
