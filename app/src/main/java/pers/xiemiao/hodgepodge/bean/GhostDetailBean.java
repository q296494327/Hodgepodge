package pers.xiemiao.hodgepodge.bean;

/**
 * User: xiemiao
 * Date: 2016-10-13
 * Time: 22:53
 * Desc: 鬼故事详情bean
 */
public class GhostDetailBean {
    public String showapi_res_error;
    public GhostDetailData showapi_res_body;
    public int showapi_res_code;

    public static class GhostDetailData {
        /**
         * allPages : 1
         * text : 我蹲下来摸了摸狗的头，然后认真地点点头，眼角润湿了。
         * currentPage : 1
         * ret_code : 0
         */
        public int allPages;
        public String text;
        public String currentPage;
        public int ret_code;
    }
}
