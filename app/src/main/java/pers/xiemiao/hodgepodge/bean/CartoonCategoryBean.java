package pers.xiemiao.hodgepodge.bean;

import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-18
 * Time: 19:10
 * Desc: 卡通漫画分类列表bean
 */
public class CartoonCategoryBean {

    public String showapi_res_error;
    public ShowapiResBodyEntity showapi_res_body;
    public int showapi_res_code;

    public static class ShowapiResBodyEntity {
        public int ret_code;
        public PagebeanEntity pagebean;

        public static class PagebeanEntity {
            public String maxResult;
            public boolean hasMorePage;
            public int currentPage;
            public List<CartoonCategoryData> contentlist;

            public static class CartoonCategoryData {
                /**
                 * thumbnailList : ["http://cdn.heibaimanhua.com/slt/tu.php?src=http://img
                 * .heibaimanhua.com/wp-content/uploads/2016/09/19/20160919_57df3d44a9544.jpg_x",
                 * "http://cdn.heibaimanhua.com/slt/tu.php?src=http://img.heibaimanhua
                 * .com/wp-content/uploads/2016/09/19/20160919_57df3d46a7186.jpg_x","http://cdn
                 * .heibaimanhua.com/slt/tu.php?src=http://img.heibaimanhua
                 * .com/wp-content/uploads/2016/09/19/20160919_57df3d4a06a7f.jpg_x"]
                 * link : http://heibaimanhua.com/weimanhua/kbmh/112339.html
                 * id : /weimanhua/kbmh/112339.html
                 * time : 4周前 (09-19)
                 * title : 恐怖漫画《杀人游戏》-黑白漫话
                 */
                public List<String> thumbnailList;
                public String link;
                public String id;
                public String time;
                public String title;
            }
        }
    }
}
