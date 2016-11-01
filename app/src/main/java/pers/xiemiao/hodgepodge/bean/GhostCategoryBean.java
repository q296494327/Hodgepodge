package pers.xiemiao.hodgepodge.bean;

import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-13
 * Time: 22:53
 * Desc: 鬼故事分类bean
 */
public class GhostCategoryBean {
    public String showapi_res_error;
    public ShowapiResBodyEntity showapi_res_body;
    public int showapi_res_code;

    public static class ShowapiResBodyEntity {
        public int ret_code;
        public PagebeanEntity pagebean;

        public static class PagebeanEntity {
            public String allPages;
            public String maxResult;
            public int currentPage;
            public List<GhostCategoryData> contentlist;

            public static class GhostCategoryData {
                /**
                 * img : http://www.guidaye.com/images/img/186.jpg
                 * link : http://www.guidaye.com/dp/19169.html
                 * id : /dp/19169.html
                 * title : 不可说
                 * desc :
                 * “阿玲，我喜欢林飞。”莫菲菲带着心事看着邓玲，忐忑不安。那名叫邓玲的女子听到此话，目光暗淡下来，强打着笑容说：“喜欢那就去追吧。”“真的吗？他一定会成为我男朋友的。”莫菲菲欢呼雀跃的牵着邓玲的手，蹦着...
                 */
                public String img;
                public String link;
                public String id;
                public String title;
                public String desc;
            }
        }
    }
}
