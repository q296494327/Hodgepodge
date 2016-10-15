package pers.xiemiao.hodgepodge.bean;

import java.util.List;

import pers.xiemiao.hodgepodge.utils.TimeUtils;

/**
 * User: xiemiao
 * Date: 2016-10-13
 * Time: 22:53
 * Desc: 新闻bean
 */
public class NewsBean {

    public ResultEntity result;
    public String reason;
    public int error_code;

    public static class ResultEntity {
        public String stat;
        public List<NewsData> data;

        public static class NewsData implements Comparable<NewsData> {
            /**
             * author_name : 新浪军事
             * date : 2016-10-13 19:03
             * realtype : 军事
             * thumbnail_pic_s : http://03.imgmini.eastday
             * .com/mobile/20161013/20161013190327_d8708ee6f9f93bf139a375bab34ed475_1_mwpm_03200403.jpg
             * uniquekey : 161013190327794
             * thumbnail_pic_s03 : http://03.imgmini.eastday
             * .com/mobile/20161013/20161013190327_d8708ee6f9f93bf139a375bab34ed475_1_mwpl_05500201.jpg
             * thumbnail_pic_s02 : http://03.imgmini.eastday
             * .com/mobile/20161013/20161013190327_d8708ee6f9f93bf139a375bab34ed475_1_mwpl_05500201.jpg
             * title : 美军舰四天两遭中国导弹打击 惊恐之下急忙反击
             * type : 头条
             * url : http://mini.eastday.com/mobile/161013190327794.html?qid=juheshuju
             */
            public String author_name;
            public String date;
            public String realtype;
            public String thumbnail_pic_s;
            public String uniquekey;
            public String thumbnail_pic_s03;
            public String thumbnail_pic_s02;
            public String title;
            public String type;
            public String url;

            //其他类型新闻相关
            public String category;

            @Override
            public int compareTo(NewsData another) {
                //按更新时间进行排序,先将时间转换成long类型
                long me = TimeUtils.strTime2Unixtime(date, "yyyy-MM-dd HH:mm");
                long it = TimeUtils.strTime2Unixtime(another.date, "yyyy-MM-dd HH:mm");
                return (int) (it - me);
            }
        }
    }
}
