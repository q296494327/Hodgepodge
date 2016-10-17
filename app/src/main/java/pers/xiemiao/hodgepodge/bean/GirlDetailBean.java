package pers.xiemiao.hodgepodge.bean;

import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-17
 * Time: 21:50
 * Desc: 详细图集bean
 */
public class GirlDetailBean {
    public String showapi_res_error;
    public ShowapiResBodyEntity showapi_res_body;
    public int showapi_res_code;

    public static class ShowapiResBodyEntity {
        public int ret_code;
        public List<String> imgList;
    }
}
