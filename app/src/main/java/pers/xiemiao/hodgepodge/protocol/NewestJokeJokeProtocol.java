package pers.xiemiao.hodgepodge.protocol;

import pers.xiemiao.hodgepodge.bean.NewestJokeBean;
import pers.xiemiao.hodgepodge.conf.Constants;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 21:27
 * Desc: 最新笑话网络协议
 */
public class NewestJokeJokeProtocol extends BaseJokeProtocol<NewestJokeBean> {

    @Override
    protected String getAppKey() {
        return Constants.URLS.JOKEAPPKEY;
    }

    @Override
    protected String getInterfaceKey() {
        return Constants.URLS.JOKECOMMONURL + "content/text.from";//笑话内容
    }
}
