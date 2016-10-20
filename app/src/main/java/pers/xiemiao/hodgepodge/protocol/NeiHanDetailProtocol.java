package pers.xiemiao.hodgepodge.protocol;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Response;
import pers.xiemiao.hodgepodge.bean.NeiHanDetailBean;
import pers.xiemiao.hodgepodge.conf.Constants;
import pers.xiemiao.hodgepodge.utils.FileUtils;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.StringUtils;

/**
 * User: xiemiao
 * Date: 2016-10-07
 * Time: 12:58
 * Desc: 美女分类网络协议
 */
public class NeiHanDetailProtocol {

    public boolean isRefresh = false;//定义一个常量记录是否是下拉刷新

    /**
     * 加载数据
     *
     * @param params 数据加载类型
     */
    public NeiHanDetailBean loadData(String params) throws Exception {
        if (!isRefresh) {
            //1先去获取本地缓存
            NeiHanDetailBean localBean = getDataFromLocal(params);
            LogUtils.sf("现在不是下拉刷新,先去看本地有没有缓存数据");
            if (localBean != null) {
                return localBean;
            }
        }
        isRefresh = false;
        //2当本地缓存为空时，就去返回网络数据
        NeiHanDetailBean netBean = getDataFromNet(params);
        if (netBean != null) {
            return netBean;
        }
        return null;
    }

    /**
     * 从本地缓存获取数据
     *
     * @param params 数据加载类型
     */
    private NeiHanDetailBean getDataFromLocal(String params) {
        //1指定存储路径以及存储名字
        File cacheFile = getCacheFile(params);
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
     * @param params 数据类型
     * @throws IOException
     */
    private NeiHanDetailBean getDataFromNet(String params) throws IOException {
        String url = Constants.URLS.NEIHANDETAILURL + "&id=" + params;
        Response response = OkHttpUtils.get().url(url).build().execute();
        String jsonString = response.body().string();
        if (!StringUtils.isEmpty(jsonString)) {
            LogUtils.sf("写缓存");
            //将json进行缓存
            FileUtils.writeFile(jsonString, getCacheFile(params).getAbsolutePath(), false);
            //解析json,由子类去实现
            return parseJson(jsonString);
        }
        return null;
    }

    /**
     * 获取缓存文件
     */
    @NonNull
    private File getCacheFile(String params) {
        String cacheDir = FileUtils.getDir("json");//创建以sd卡或者缓存文件夹为路径，json为文件夹名称
        String name = params.replace("/", "");
        return new File(cacheDir, name);
    }

    /**
     * 通过获取泛型的类型,父类自己解析json
     */
    protected NeiHanDetailBean parseJson(String jsonString) {
        return new Gson().fromJson(jsonString, NeiHanDetailBean.class);
    }


}
