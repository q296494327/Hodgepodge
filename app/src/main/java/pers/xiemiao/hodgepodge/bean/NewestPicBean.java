package pers.xiemiao.hodgepodge.bean;

import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-12
 * Time: 15:50
 * Desc: 最新趣图bean
 */
public class NewestPicBean {

    public ResultEntity result;
    public String reason;
    public int error_code;

    public static class ResultEntity {

        public List<NewestPicData> data;

        public static class NewestPicData {
            /**
             * unixtime : 1474167938
             * updatetime : 2016-09-18 11:05:38
             * hashId : CB59DF7283EFE444850A1898F0D9A26F
             * content : 结果还是被抓了
             * url : http://juheimg.oss-cn-hangzhou.aliyuncs
             * .com/joke/201609/18/CB59DF7283EFE444850A1898F0D9A26F.jpg
             */
            public int unixtime;
            public String updatetime;
            public String hashId;
            public String content;
            public String url;
        }
    }
}
