package pers.xiemiao.hodgepodge.bean;

/**
 * User: xiemiao
 * Date: 2016-10-20
 * Time: 16:02
 * Desc: 内涵漫画详情bean
 */
public class NeiHanDetailBean {

    public String showapi_res_error;
    public NeiHanDetailData showapi_res_body;
    public int showapi_res_code;

    public static class NeiHanDetailData {
        /**
         * img : http://www.hanhande.com/upload/160104/4182594_174646_1.jpg
         * title : 内涵漫画：证据
         * ret_code : 0
         */
        public String img;
        public String title;
        public int ret_code;
    }
}
