package pers.xiemiao.hodgepodge.adapter;

import java.util.List;

import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.base.SuperBaseAdapter;
import pers.xiemiao.hodgepodge.bean.NewsBean;
import pers.xiemiao.hodgepodge.holder.newsHolder.LeftNewsHolder;
import pers.xiemiao.hodgepodge.holder.newsHolder.RightNewsHolder;

/**
 * User: xiemiao
 * Date: 2016-10-14
 * Time: 01:23
 * Desc: 头条新闻适配器
 */
public class TopNewsAdapter extends SuperBaseAdapter<NewsBean.ResultEntity.NewsData> {
    public static final int LeftPicType = 0;
    public static final int RightPicType = 1;

    public TopNewsAdapter(List<NewsBean.ResultEntity.NewsData> dataSources) {
        super(dataSources);
    }

    /*-------------------增加listview显示类型---begin------------------*/

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 4 == 0) {
            return LeftPicType;
        } else {
            return RightPicType;
        }
    }

    /*-------------------增加listview显示类型---end------------------*/

    @Override
    protected BaseHolder getSpecialHolder(int position) {
        if (getItemViewType(position) == RightPicType) {
            return new RightNewsHolder();
        } else {
            return new LeftNewsHolder();
        }
    }
}
