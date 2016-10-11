package pers.xiemiao.hodgepodge.bean;

import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 21:36
 * Desc: 最新笑话实体bean
 */
public class NewestJokeBean {

    public ResultEntity result;
    public String reason;
    public int error_code;

    public static class ResultEntity {

        public List<JokeDate> data;

        public static class JokeDate {
            /**
             * unixtime : 1476101630
             * updatetime : 2016-10-10 20:13:50
             * hashId : 39c4f8be434119f3801d11f9a3880e15
             * content : 小明上个月刚刚结束单身，交了一个漂亮女友，小刚很羡慕，问小明：“你们交往到哪一步了”，小明说：“banana”
             * 小刚鄙视的看着小明，欺负我不懂英语，我得回去查查，一查，“哇！香蕉（相交）”
             */
            public long unixtime;
            public String updatetime;
            public String hashId;
            public String content;
        }
    }
}
