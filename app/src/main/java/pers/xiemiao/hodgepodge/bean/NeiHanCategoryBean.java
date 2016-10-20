package pers.xiemiao.hodgepodge.bean;

import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-20
 * Time: 16:00
 * Desc: 内涵漫画分类列表bean
 */
public class NeiHanCategoryBean {

    public String showapi_res_error;
    public ShowapiResBodyEntity showapi_res_body;
    public int showapi_res_code;

    public static class ShowapiResBodyEntity {
        public String currentPage;
        public int ret_code;
        public PagebeanEntity pagebean;

        public static class PagebeanEntity {
            public String allPages;
            public String maxResult;
            public List<NeiHanCategoryData> contentlist;

            public static class NeiHanCategoryData {
                /**
                 * link : http://www.hanhande.com/xe/7007659.shtml
                 * id : /xe/7007659.shtml
                 * title : 内涵漫画：情人节礼物
                 */
                public String link;
                public String id;
                public String title;
            }
        }
    }
}
