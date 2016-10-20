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
        //百思不得姐URL
        public static final String BAISIURL = "http://route.showapi" +
                ".com/255-1?showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //美女图片分类URL
        public static final String GIRLCATEGORYURL = "https://route.showapi" +
                ".com/959-1?showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //美女图片组图URL
        public static final String GIRLDETAILURL = "https://route.showapi" +
                ".com/959-2?showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //卡通漫画分类URL
        public static final String CARTOONCATEGORYURL = "http://route.showapi.com/958-1?" +
                "showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //卡通漫画详细URL
        public static final String CARTOONDETAILURL = "http://route.showapi.com/958-2?" +
                "showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //内涵漫画分类URL
        public static final String NEIHANCATEGORYURL = "https://route.showapi.com/978-2?" +
                "showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //内涵漫画详情URL
        public static final String NEIHANDETAILURL = "https://route.showapi.com/978-1?" +
                "showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";

    }
}
