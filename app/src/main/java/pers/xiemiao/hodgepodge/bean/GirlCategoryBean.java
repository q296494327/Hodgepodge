package pers.xiemiao.hodgepodge.bean;

import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-17
 * Time: 14:11
 * Desc: 美女分类bean
 */
public class GirlCategoryBean {
    public String showapi_res_error;
    public ShowapiResBodyEntity showapi_res_body;
    public int showapi_res_code;

    public static class ShowapiResBodyEntity {
        public int currentPage;
        public int ret_code;
        public PagebeanEntity pagebean;

        public static class PagebeanEntity {
            public String maxResult;
            public List<GirlCategoryData> contentlist;

            public static class GirlCategoryData {
                /**
                 * img : http://mimg.xmeise.com/thumb/m/allimg/161017/1-16101G21648,c_fill,h_360,
                 * w_240.jpg
                 * link : /av/qt/59571.html
                 * id : /qt/59571.html
                 * title : 90后女优つぼみ三点式比基尼湿身美臀诱惑个人艺术写真
                 */
                public String img;
                public String link;
                public String id;
                public String title;
                public String count;
            }
        }
    }
}
