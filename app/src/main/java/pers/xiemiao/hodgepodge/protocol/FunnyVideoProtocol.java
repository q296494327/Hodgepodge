package pers.xiemiao.hodgepodge.protocol;

import pers.xiemiao.hodgepodge.bean.FunnyVideoBean;

/**
 * User: xiemiao
 * Date: 2016-10-15
 * Time: 15:27
 * Desc: 搞笑视频bean
 */
public class FunnyVideoProtocol extends BaseBaiSIProtocol<FunnyVideoBean> {
    @Override
    protected String getInterfaceKey() {
        return "&type=41";//类型是视频
    }
}
