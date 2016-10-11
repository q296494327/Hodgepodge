package pers.xiemiao.hodgepodge.protocol;

import pers.xiemiao.hodgepodge.bean.NewestJokeBean;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 21:27
 * Desc: 最新笑话网络协议
 */
public class NewestJokeProtocol extends BaseProtocol<NewestJokeBean>{

    @Override
    protected String getInterfaceKey() {
        return "content/text.from";//笑话内容
    }
}
