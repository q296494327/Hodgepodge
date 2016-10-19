package pers.xiemiao.hodgepodge.bean;

import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-18
 * Time: 19:12
 * Desc: 卡通漫画详情bean
 */
public class CartoonDetailBean {

    public String showapi_res_error;
    public ShowapiResBodyEntity showapi_res_body;
    public int showapi_res_code;

    public static class ShowapiResBodyEntity {
        public CartoonDetailData item;
        public int ret_code;

        public static class CartoonDetailData {
            public String time;
            public String title;
            public List<String> imgList;
        }
    }
}
