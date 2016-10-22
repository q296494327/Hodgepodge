package pers.xiemiao.hodgepodge.bean;

/**
 * User: xiemiao
 * Date: 2016-10-22
 * Time: 12:13
 * Desc:
 */
public class StarBean {

    public String showapi_res_error;
    public ShowapiResBodyEntity showapi_res_body;
    public int showapi_res_code;

    public static class ShowapiResBodyEntity {
        public String star;
        public TodayData day;
        public int ret_code;

        public static class TodayData {
            /**
             * money_star : 4
             * love_txt : 和情人一同出游或是甜蜜约会的机会都会增加，可以让自己轻松一下。
             * lucky_num : 1
             * money_txt : 有意外之财，例如：彩票中奖。
             * love_star : 3
             * summary_star : 4
             * lucky_time : 下午3:00--4:00
             * lucky_color : 玛瑙蓝
             * work_txt : 没有什么压力，精神格外清爽，适合与亲友聚会，享受温暖的亲情和友情。
             * lucky_direction : 正北方
             * time : 20161022
             * grxz : 水瓶座
             * general_txt :
             * 太过严肃会显得你很冷漠，做一个调皮的表情反而能给心仪对象留下好印象；学生族留意校园里的公告栏，有机会获取到一份不错的兼职；对金钱的嗅觉很敏感，可望在街上拾得意外之财。
             * day_notice : 今日好运相伴。
             * work_star : 4
             */
            public int money_star;
            public String love_txt;
            public String lucky_num;
            public String money_txt;
            public int love_star;
            public int summary_star;
            public String lucky_time;
            public String lucky_color;
            public String work_txt;
            public String lucky_direction;
            public String time;
            public String grxz;
            public String general_txt;
            public String day_notice;
            public int work_star;
        }
    }
}
