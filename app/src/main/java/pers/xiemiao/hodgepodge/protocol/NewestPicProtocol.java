package pers.xiemiao.hodgepodge.protocol;

import pers.xiemiao.hodgepodge.bean.NewestPicBean;
import pers.xiemiao.hodgepodge.conf.Constants;

/**
 * User: xiemiao
 * Date: 2016-10-12
 * Time: 15:48
 * Desc: 最新图片网络协议
 */
public class NewestPicProtocol extends BaseProtocol<NewestPicBean> {

    @Override
    protected String getAppKey() {
        return Constants.URLS.JOKEAPPKEY;
    }

    @Override
    protected String getInterfaceKey() {
        return Constants.URLS.JOKECOMMONURL + "img/text.from";
    }
}
