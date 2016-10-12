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
        //笑话相关的APPKEY
        public static final String JOKEAPPKEY = "6682314439831e05070d10c5e6e36b23";
        //常规的笑话URL
        public static final String JOKECOMMONURL = "http://japi.juhe.cn/joke/";
        //随机的笑话URL
        public static final String JOKERANDOMURL = "http://v.juhe.cn/joke/";
    }
}
