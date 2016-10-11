package pers.xiemiao.hodgepodge.conf;


import pers.xiemiao.hodgepodge.utils.LogUtils;

/**
 * User: xiemiao
 * Date: 2016-10-04
 * Time: 21:37
 * Desc: 常量工具类
 */
public class Constants {

    /**
     * 允许输出的lOG等级
     */
    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;
    /**
     * json的缓存期限
     */
    public static final long CACHE_TIME = 30 * 60 * 1000;//30分钟

    /**
     * 请求链接相关
     */
    public static final class URLS {
        public static final String BASEJOKEURL = "http://japi.juhe.cn/joke/";
    }
}
