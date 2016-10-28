package pers.xiemiao.hodgepodge.protocol;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Response;
import pers.xiemiao.hodgepodge.bean.BaiduBeautyBean;
import pers.xiemiao.hodgepodge.conf.Constants;
import pers.xiemiao.hodgepodge.utils.FileUtils;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.PinyinUtil;
import pers.xiemiao.hodgepodge.utils.StringUtils;

/**
 * User: xiemiao
 * Date: 2016-10-07
 * Time: 12:58
 * Desc: 百度图片网络协议
 */
public class BaiduImageProtocol {

    public boolean isRefresh = false;//定义一个常量记录是否是下拉刷新

    /**
     * 加载数据
     *
     * @param tag1 大的类型
     * @param tag2 小分类
     */
    public BaiduBeautyBean loadData(String tag1, String tag2, int page) throws Exception {
        if (!isRefresh) {
            //1先去获取本地缓存
            BaiduBeautyBean localBean = getDataFromLocal(tag1, tag2, page);
            LogUtils.sf("现在不是下拉刷新,先去看本地有没有缓存数据");
            if (localBean != null) {
                return localBean;
            }
        }
        isRefresh = false;
        //2当本地缓存为空时，就去返回网络数据
        BaiduBeautyBean netBean = getDataFromNet(tag1, tag2, page);
        if (netBean != null) {
            return netBean;
        }
        return null;
    }

    /**
     * 从本地缓存获取数据
     *
     * @param tag1
     * @param tag2 数据加载类型
     * @param page 第几页
     */
    private BaiduBeautyBean getDataFromLocal(String tag1, String tag2, int page) {
        //1指定存储路径以及存储名字
        File cacheFile = getCacheFile(tag1, tag2, page);
        //2判断文件是否存在,如果存在，就去看缓存是否过期
        if (cacheFile.exists()) {
            long lastModified = cacheFile.lastModified();//获取最后一次修改时间
            //3判断当前系统时间和最后一次修改时间有木有超过缓存期限
            if (System.currentTimeMillis() - lastModified < Constants.CACHE_TIME) {
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
     * @param tag1 数据类型
     * @param page 第几页
     * @throws IOException
     */
    private BaiduBeautyBean getDataFromNet(String tag1, String tag2, int page) throws IOException {
        String url = Constants.URLS.BAIDUIMAGEURL + "&tag1=" + tag1 + "&tag2=" + tag2 + "&pn=" +
                page;
        Response response = OkHttpUtils.get().url(url).build().execute();
        String jsonString = response.body().string();
        if (!StringUtils.isEmpty(jsonString)) {
            LogUtils.sf("写缓存");
            //将json进行缓存
            FileUtils.writeFile(jsonString, getCacheFile(tag1, tag2, page).getAbsolutePath(),
                    false);
            //解析json,由子类去实现
            return parseJson(jsonString);
        }
        return null;
    }

    /**
     * 获取缓存文件
     */
    @NonNull
    private File getCacheFile(String tag1, String tag2, int page) {
        String cacheDir = FileUtils.getDir("json");//创建以sd卡或者缓存文件夹为路径，json为文件夹名称
        String name = PinyinUtil.getPinyin(tag1, tag2) + "." + page;
        return new File(cacheDir, name);
    }

    /**
     * 通过获取泛型的类型,父类自己解析json
     */
    protected BaiduBeautyBean parseJson(String jsonString) {
        return new Gson().fromJson(jsonString, BaiduBeautyBean.class);
    }


}
