package pers.xiemiao.hodgepodge.protocol;

import java.util.HashMap;
import java.util.Map;

import pers.xiemiao.hodgepodge.bean.RandomJokeBean;
import pers.xiemiao.hodgepodge.conf.Constants;

/**
 * User: xiemiao
 * Date: 2016-10-12
 * Time: 13:16
 * Desc: 随机笑话网络协议
 */
public class RandomJokeJokeProtocol extends BaseJokeProtocol<RandomJokeBean> {
    @Override
    protected String getAppKey() {
        return Constants.URLS.JOKEAPPKEY;
    }

    @Override
    protected String getInterfaceKey() {
        return Constants.URLS.JOKERANDOMURL + "randJoke.php";//随机笑话内容
    }

    @Override//额外的笑话类型参数(趣图或者笑话)
    public Map<String, String> getExtraParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", " ");
        return params;
    }
}
