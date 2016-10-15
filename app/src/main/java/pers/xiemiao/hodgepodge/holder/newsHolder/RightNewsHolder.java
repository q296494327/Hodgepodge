package pers.xiemiao.hodgepodge.holder.newsHolder;

import android.view.View;
import android.widget.TextView;

import lib.lhh.fiv.library.FrescoImageView;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.bean.NewsBean;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.StringUtils;
import pers.xiemiao.hodgepodge.utils.TimeUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-14
 * Time: 00:29
 * Desc: 头条新闻holder
 */
public class RightNewsHolder extends BaseHolder<NewsBean.ResultEntity.NewsData> {
    private TextView mTvTitle;
    private TextView mTvAuthor;
    private TextView mTvDate;
    private FrescoImageView mFivNewsPic;
    private TextView mTvType;
    private TextView mTvTimeAgo;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_right_news, null);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvAuthor = (TextView) view.findViewById(R.id.tv_author);
        mTvType = (TextView) view.findViewById(R.id.tv_type);
        mTvDate = (TextView) view.findViewById(R.id.tv_date);
        mTvTimeAgo = (TextView) view.findViewById(R.id.tv_timeago);
        mFivNewsPic = (FrescoImageView) view.findViewById(R.id.fiv_newspic);
        return view;
    }

    @Override
    protected void refreshHolderView(NewsBean.ResultEntity.NewsData data) {
        mTvAuthor.setText(data.author_name);
        mTvTitle.setText(data.title);
        mTvDate.setText(data.date);
        mTvType.setText(data.realtype);
        //转换间隔时间
        if (StringUtils.isEquals(data.category, "社会")) {
            mTvTimeAgo.setText("");
        } else {
            String timeAgo = parseSpaceTime(data);
            mTvTimeAgo.setText(timeAgo);
        }
        mFivNewsPic.setImageURI(data.thumbnail_pic_s03);

        //刷新视图的时候根据sp里存的状态,去改变已读颜色
        String readState = SpUtil.getString(UIUtils.getContext(), "readState", "");
        String url = data.url.replace(":", "").replace("/", "").replace("?", "");
        if (readState.contains(url)) {
            mTvTitle.setTextColor(0XFF787171);
            mTvAuthor.setTextColor(0XFF787171);
            mTvDate.setTextColor(0XFF787171);
            mTvTimeAgo.setTextColor(0XFF787171);
        } else {
            mTvTitle.setTextColor(0XFF000000);
            mTvAuthor.setTextColor(0XFF000000);
            mTvDate.setTextColor(0XFF000000);
            mTvTimeAgo.setTextColor(0XFF000000);
        }
    }

    private String parseSpaceTime(NewsBean.ResultEntity.NewsData data) {
        //将字符串时间转换成时间戳
        long unixtime = TimeUtils.strTime2Unixtime(data.date, "yyyy-MM-dd HH:mm");
        //计算unixtime与当前系统时间的差距
        long currentTimeMillis = System.currentTimeMillis();
        long spaceTime = currentTimeMillis - unixtime;
        String timeAgo = "";
        if (spaceTime < 3600000) {
            timeAgo = spaceTime / 60000 + "分钟前";
        } else {
            timeAgo = spaceTime / 3600000 + "小时前";
        }
        return timeAgo;
    }
}
