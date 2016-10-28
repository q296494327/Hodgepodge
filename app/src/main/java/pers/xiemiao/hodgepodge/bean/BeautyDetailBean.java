package pers.xiemiao.hodgepodge.bean;

import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-28
 * Time: 12:36
 * Desc: 美女详情bean
 */
public class BeautyDetailBean {

    /**
     * img : /ext/161027/5b2b08f2e757d3de84fb4c1fa8e92f23.jpg
     * rcount : 0
     * size : 6
     * count : 26
     * id : 993
     * time : 1477575688000
     * fcount : 0
     * list : [{"src":"/ext/161027/5b2b08f2e757d3de84fb4c1fa8e92f23.jpg","id":15216,"gallery":993}]
     * title : 极品大胸美女腿模连体泳装大白腿美胸爆乳性感
     * galleryclass : 1
     * url : http://www.tngou.net/tnfs/show/993
     * status : true
     */
    public String img;
    public int rcount;
    public int size;
    public int count;
    public int id;
    public long time;
    public int fcount;
    public List<ImageData> list;
    public String title;
    public int galleryclass;
    public String url;
    public boolean status;

    public static class ImageData {
        /**
         * src : /ext/161027/5b2b08f2e757d3de84fb4c1fa8e92f23.jpg
         * id : 15216
         * gallery : 993
         */
        public String src;
        public int id;
        public int gallery;
    }
}
