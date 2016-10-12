package pers.xiemiao.hodgepodge.protocol;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Response;
import pers.xiemiao.hodgepodge.conf.Constants;
import pers.xiemiao.hodgepodge.utils.FileUtils;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.StringUtils;

/**
 * User: xiemiao
 * Date: 2016-10-07
 * Time: 12:58
 * Desc: 基类网络协议，抽取共性、json的缓存与读取
 */
public abstract class BaseProtocol<T> {

    public boolean isRefresh = false;//定义一个常量记录是否是下拉刷新

    /**
     * 加载数据
     *
     * @param index 从第几条数据开始加载
     */
    public T loadData(int index) throws Exception {
        if (!isRefresh) {
            //1先去获取本地缓存
            T localBean = getDataFromLocal(index);
            LogUtils.sf("现在不是下拉刷新,先去看本地有没有缓存数据");
            if (localBean != null) {
                return localBean;
            }
        }
        isRefresh = false;
        //2当本地缓存为空时，就去返回网络数据
        T netBean = getDataFromNet(index);
        if (netBean != null) {
            return netBean;
        }
        return null;
    }

    /**
     * 从本地缓存获取数据
     *
     * @param index 要获取数据的index
     */
    private T getDataFromLocal(int index) {
        //1指定存储路径以及存储名字
        File cacheFile = getCacheFile(index);
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
     * @param page 所加载的第几条数据
     * @throws IOException
     */
    private T getDataFromNet(int page) throws IOException {
        String url = getInterfaceKey() +
                "?key=" + getAppKey();
        GetBuilder params = OkHttpUtils.get().url(url);
        Map<String, String> extraParams = getExtraParams();
        if (extraParams == null) {
            //如果额外的参数是空,那就直接添加index参数
            params.addParams("page", page + "");
            params.addParams("pagesize", "20");
        } else {
            //否则遍历额外的参数集合,添加额外的参数
            for (Map.Entry<String, String> extraParam : extraParams.entrySet()) {
                String key = extraParam.getKey();
                String value = extraParam.getValue();
                params.addParams(key, value);
            }
        }
        Response response = params.build().execute();
        String jsonString = response.body().string();
        if (!StringUtils.isEmpty(jsonString)) {
            LogUtils.sf("写缓存");
            //将json进行缓存
            FileUtils.writeFile(jsonString, getCacheFile(page).getAbsolutePath(), false);
            //解析json,由子类去实现
            return parseJson(jsonString);
        }
        return null;
    }

    /**
     * 协议模块的网络请求APPKEY子类必须实现
     */
    protected abstract String getAppKey();


    /**
     * 获取额外的参数,子类可复写去添加参数
     */
    public Map<String, String> getExtraParams() {
        return null;
    }

    /**
     * 获取缓存文件
     */
    @NonNull
    private File getCacheFile(int index) {
        String cacheDir = FileUtils.getDir("json");//创建以sd卡或者缓存文件夹为路径，json为文件夹名称
        String name = "";
        //这里要进行判断如果是详情页,命名方式要用包名
        Map<String, String> extraParams = getExtraParams();
        if (extraParams == null) {
            //以接口key和index为文件名
            name = getInterfaceKey().replace("/", "").replace(":", "") + "." + index;
        } else {
            //以接口key和type为文件名
            name = getInterfaceKey().replace("/", "").replace(":", "") + extraParams.get
                    ("type");
        }
        return new File(cacheDir, name);
    }

    /**
     * 通过获取泛型的类型,父类自己解析json
     */
    protected T parseJson(String jsonString) {
        //1通过它的Class,获取它的泛型,强转成参数化泛型,如果BaseProtocol是接口就用getGenericInterfaceclass
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        //2再用参数化泛型,获取真实的泛型数组
        Type[] types = genericSuperclass.getActualTypeArguments();
        //3由于只有一个泛型,这里直接0角标获取到子类的实际泛型
        Type type = types[0];
        //4直接把类型丢到Gson去解析
        return new Gson().fromJson(jsonString, type);
    }


    /**
     * 网络请求接口Key，子类必须实现具体接口key
     */
    protected abstract String getInterfaceKey();

    //    /**
    //     * 解析json，因为不知道json的bean文件，所以由子类去必须实现解析
    //     *
    //     * @param jsonString 所要解析的json
    //     */
    //    protected abstract T parseJson(String jsonString);

}
