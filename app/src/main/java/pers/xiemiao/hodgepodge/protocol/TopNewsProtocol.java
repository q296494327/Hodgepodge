package pers.xiemiao.hodgepodge.protocol;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;

import java.io.File;
import java.io.IOException;

import okhttp3.Response;
import pers.xiemiao.hodgepodge.bean.NewsBean;
import pers.xiemiao.hodgepodge.utils.FileUtils;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.StringUtils;

/**
 * User: xiemiao
 * Date: 2016-10-07
 * Time: 12:58
 * Desc: 头条新闻网络协议，抽取共性、json的缓存与读取
 */
public class TopNewsProtocol {

    public boolean isRefresh = false;//定义一个常量记录是否是下拉刷新
    public static String BASEURL = "http://v.juhe.cn/toutiao/index?";
    public static long CACHE_TIME = 5 * 60 * 1000;//缓存时间

    /**
     * 加载数据
     *
     * @param type 请求新闻的类型
     */
    public NewsBean loadData(String type) throws Exception {
        if (!isRefresh) {
            //1先去获取本地缓存
            NewsBean localBean = getDataFromLocal(type);
            if (localBean != null) {
                return localBean;
            }
        }
        LogUtils.sf("下拉刷新中，去请求网络最新数据");
        isRefresh = false;
        //2当本地缓存为空时，就去返回网络数据
        NewsBean netBean = getDataFromNet(type);
        if (netBean != null) {
            return netBean;
        }
        return null;
    }

    /**
     * 从本地缓存获取数据
     *
     * @param type 要请求的新闻类型
     */
    private NewsBean getDataFromLocal(String type) {
        //1指定存储路径以及存储名字
        File cacheFile = getCacheFile(type);
        //2判断文件是否存在,如果存在，就去看缓存是否过期
        if (cacheFile.exists()) {
            long lastModified = cacheFile.lastModified();//获取最后一次修改时间
            //3判断当前系统时间和最后一次修改时间有木有超过缓存期限
            if (System.currentTimeMillis() - lastModified < CACHE_TIME) {
                //4没有超过期限，就去读取缓存文件
                LogUtils.sf("读取缓存:" + cacheFile.getAbsoluteFile());
                String cacheJson = FileUtils.readFile(cacheFile);
                if (!StringUtils.isEmpty(cacheJson)) {
                    //5如果缓存json不是空的，就去解析json
                    return parseJson(cacheJson);
                }
            }
        }
        return null;
    }

    /**
     * 从网络获取数据
     *
     * @param type 所加载的类型
     * @throws IOException type=top&key=366e9efbfa8d82b6e95e7ad15c3569e6
     */
    private NewsBean getDataFromNet(String type) throws IOException {
        String url = BASEURL + "type=" + type + "&key=366e9efbfa8d82b6e95e7ad15c3569e6";
        GetBuilder params = OkHttpUtils.get().url(url);
        Response response = params.build().execute();
        String jsonString = response.body().string();
        if (!StringUtils.isEmpty(jsonString)) {
            LogUtils.sf("写缓存");
            //将json进行缓存
            FileUtils.writeFile(jsonString, getCacheFile(type).getAbsolutePath(), false);
            //解析json
            return parseJson(jsonString);
        }
        return null;
    }

    /**
     * 获取缓存文件
     */
    @NonNull
    private File getCacheFile(String type) {
        String cacheDir = FileUtils.getDir("json");//创建以sd卡或者缓存文件夹为路径，json为文件夹名称
        String name = "";
        name = BASEURL.replace("/", "").replace(":", "").replace("?", "") + "." + type;
        return new File(cacheDir, name);
    }

    /**
     * 通过获取泛型的类型,父类自己解析json
     */
    protected NewsBean parseJson(String jsonString) {
        return new Gson().fromJson(jsonString, NewsBean.class);
    }

}
