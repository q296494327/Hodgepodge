package pers.xiemiao.hodgepodge.bean;

import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-12
 * Time: 22:57
 * Desc: 随机趣图bean
 */
public class RandomPicBean {
    public List<RandomPicData> result;
    public String reason;
    public int error_code;

    public static class RandomPicData {
        /**
         * unixtime : 1434336149
         * hashId : 949BCC195DDBB1511984B711DFEC1F67
         * content : 去哪都改不了
         * url : http://juheimg.oss-cn-hangzhou.aliyuncs
         * .com/joke/201506/15/949BCC195DDBB1511984B711DFEC1F67.jpg
         */
        public String unixtime;
        public String hashId;
        public String content;
        public String url;
    }
}
