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
     * 广告位ID
     */
    //        public static final String LISTVIEW_PLACE_ID = "2058628";//广告位ID
    //        public static final String VIWEPAGER_PLACE_ID = "2403633";//广告位ID
    public static final String LISTVIEW_PLACE_ID = "";//广告位ID
    public static final String VIWEPAGER_PLACE_ID = "3036061";//广告位ID

    /**
     * 笑话标题数组KEY
     */
    public static final String JOKE_TITLES = "JOKE_TITLES";
    public static final String JOKE_OTHER_TITLES = "JOKE_OTHER_TITLES";
    /**
     * 三更有鬼标题数组KEY
     */
    public static final String GHOST_TITLES = "GHOST_TITLES";
    public static final String GHOST_OTHER_TITLES = "GHOST_OTHER_TITLES";
    /**
     * 萌妹标题数组KEY
     */
    public static final String GRIL_TITLES = "GRIL_TITLES";
    public static final String GRIL_OTHER_TITLES = "GRIL_OTHER_TITLES";
    /**
     * 卡通标题数组KEY
     */
    public static final String CARTOON_TITLES = "CARTOON_TITLES";
    public static final String CARTOON_OTHER_TITLES = "CARTOON_OTHER_TITLES";

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
        public static final String GIRLCATEGORYURL = "http://route.showapi" +
                ".com/959-1?showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //美女图片组图URL
        public static final String GIRLDETAILURL = "http://route.showapi" +
                ".com/959-2?showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //卡通漫画分类URL
        public static final String CARTOONCATEGORYURL = "http://route.showapi.com/958-1?" +
                "showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //卡通漫画详细URL
        public static final String CARTOONDETAILURL = "http://route.showapi.com/958-2?" +
                "showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //内涵漫画分类URL
        public static final String NEIHANCATEGORYURL = "http://route.showapi.com/978-2?" +
                "showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //内涵漫画详情URL
        public static final String NEIHANDETAILURL = "http://route.showapi.com/978-1?" +
                "showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //星座今日运势URL
        public static final String STARURL = "http://route.showapi.com/872-1?" +
                "showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";

        //美女列表URL
        public static final String BEAUTYCATEGORYURL = "http://www.tngou.net/tnfs/api/list?";
        //美女列表URL
        public static final String BEAUTYDETAILURL = "http://www.tngou.net/tnfs/api/show?";
        //图片前缀
        public static final String BEAUTYBEFORE = "http://tnfs.tngou.net/img";
        public static final String BEAUTYBEFORESIZE = "http://tnfs.tngou.net/image";
        //百度美女图片
        public static final String BAIDUIMAGEURL = "http://image.baidu" +
                ".com/channel/listjson?ie=utf8&rn=20";

        //鬼故事列表URL
        public static final String GHOSTCATEGORYURL = "http://route.showapi.com/955-1?" +
                "showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //鬼故事详情URL
        public static final String GHOSTDETAILURL = "http://route.showapi.com/955-2?" +
                "showapi_appid=25623&showapi_sign=4e2d463aa20d47e588d325a13512571f";
        //应用微下载链接
        public static final String WEIDOWNLOAD = "http://a.app.qq.com/o/simple.jsp?pkgname=pers" +
                ".xiemiao.hodgepodge";
    }
}
