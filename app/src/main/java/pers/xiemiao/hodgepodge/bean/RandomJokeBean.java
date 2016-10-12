package pers.xiemiao.hodgepodge.bean;

import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-12
 * Time: 14:45
 * Desc:
 */
public class RandomJokeBean {

    public List<RandomJokeData> result;
    public String reason;
    public int error_code;

    public static class RandomJokeData {
        /**
         * unixtime : 1432456934
         * hashId : B82811731009229411320A0FB965F8CB
         * content : 半夜里，丈夫突然把妻子摇醒，兴奋地说：刚才我做梦捡到了一个装有五千元的提包！ 妻子唔了一声，又睡着了。
         * 过了一会，丈夫被妻子的抽泣声惊醒了，问她为什么哭，妻子说：我梦见你捡的那个提包被人偷走了。
         */
        public String unixtime;
        public String hashId;
        public String content;
    }
}
