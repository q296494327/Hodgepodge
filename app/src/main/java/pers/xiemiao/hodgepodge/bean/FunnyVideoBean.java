package pers.xiemiao.hodgepodge.bean;

import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-15
 * Time: 14:52
 * Desc: 搞笑视频bean
 */
public class FunnyVideoBean {

    public String showapi_res_error;
    public ShowapiResBodyEntity showapi_res_body;
    public int showapi_res_code;

    public static class ShowapiResBodyEntity {
        public int ret_code;
        public PagebeanEntity pagebean;

        public static class PagebeanEntity {
            public int allPages;
            public int maxResult;
            public int currentPage;
            public List<FunnyVideoData> contentlist;
            public int allNum;

            public static class FunnyVideoData {
                /**
                 * weixin_url : http://m.budejie.com/detail-21063169.html/
                 * love : 936
                 * create_time : 2016-10-10 21:42:02
                 * videotime : 0
                 * type : 41
                 * voicetime : 0
                 * video_uri : http://mvideo.spriteapp
                 * .cn/video/2016/1003/7c8a9c26-8915-11e6-bbaa-90b11c479401_wpc.mp4
                 * profile_image : http://wimg.spriteapp
                 * .cn/profile/large/2016/07/05/577b7ff6e4250_mini.jpg
                 * voicelength : 0
                 * voiceuri :
                 * width : 0
                 * name : 百思葫芦娃VV六娃
                 * hate : 124
                 * text :
                 * 什么叫妇道，美女说的太好了，兄弟们顶起来
                 * <p/>
                 * id : 21063169
                 * height : 0
                 */
                public String weixin_url;
                public String love;
                public String create_time;
                public String videotime;
                public String type;
                public String voicetime;
                public String video_uri;
                public String profile_image;
                public String voicelength;
                public String voiceuri;
                public String width;
                public String name;
                public String hate;
                public String text;
                public String id;
                public String height;
            }
        }
    }
}
