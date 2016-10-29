package pers.xiemiao.hodgepodge.bean;

import java.io.Serializable;
import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-28
 * Time: 21:06
 * Desc: 百度美女bean
 */
public class BaiduBeautyBean {
    public String tag1;
    public List<BaiduBeautyData> data;
    public int start_index;
    public int totalNum;
    public int return_number;
    public String tag2;

    public static class BaiduBeautyData implements Serializable {
        public String date;
        public int image_height;
        public String download_url;
        public String id;
        public int image_width;
        public String colum;
        public String desc;
        public String photo_id;
        public String abs;
        public int pn;
    }
}
