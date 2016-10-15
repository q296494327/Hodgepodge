package pers.xiemiao.hodgepodge.holder.newsHolder;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import lib.lhh.fiv.library.FrescoImageView;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.activity.NewsDetailActivity;
import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.bean.NewsBean;
import pers.xiemiao.hodgepodge.utils.DensityUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-07
 * Time: 17:59
 * Desc: 首页轮播图holder
 */
public class TopNewsPicturesHolder extends BaseHolder<List<NewsBean.ResultEntity.NewsData>>
        implements ViewPager
        .OnPageChangeListener {

    private ViewPager mPicturePager;
    private LinearLayout mContainerIndicator;//指针容器（小圆点）
    private ImageView mIndicator;
    private Handler mHandler;
    private TextView mTvMiniTitle;

    @Override
    protected View initHolderView() {
        //初始化布局
        View view = View.inflate(UIUtils.getContext(), R.layout.item_topnews_picture, null);
        mPicturePager = (ViewPager) view.findViewById(R.id.item_news_picture_pager);
        mContainerIndicator = (LinearLayout) view.findViewById(R.id
                .item_news_picture_container_indicator);
        mTvMiniTitle = (TextView) view.findViewById(R.id.tv_miniTitle);
        return view;
    }

    @Override
    protected void refreshHolderView(List<NewsBean.ResultEntity.NewsData> data) {
        //添加小圆点
        for (int i = 0; i < mData.size(); i++) {
            mIndicator = new ImageView(UIUtils.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = DensityUtils.dp2px(UIUtils.getContext(), 6);
            mIndicator.setLayoutParams(params);
            mIndicator.setImageResource(R.mipmap.indicator_normal);
            mContainerIndicator.addView(mIndicator);
        }
        //一开始就将第一个点设置为被选择状态
        ImageView child = (ImageView) mContainerIndicator.getChildAt(0);
        child.setImageResource(R.mipmap.indicator_selected);

        //创建适配器
        PicturesPagerAdapter mAdapter = new PicturesPagerAdapter();
        mPicturePager.setAdapter(mAdapter);
        mPicturePager.setOnPageChangeListener(this);
        mPicturePager.setCurrentItem(mData.size() * 1000000);//为了可以向左滑,设置当前条目为中间

        //无限轮播
        mHandler = UIUtils.getHandler();
        final MyTask task = new MyTask();
        task.start();

        //设置viewpager触摸监听，停止轮播
        mPicturePager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        task.stop();
                        break;
                    case MotionEvent.ACTION_UP:
                        task.start();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 轮播任务
     */
    class MyTask implements Runnable {
        void start() {
            mHandler.postDelayed(this, 3000);
        }

        void stop() {
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            int currentItem = mPicturePager.getCurrentItem();
            currentItem++;
            mPicturePager.setCurrentItem(currentItem);
            start();
        }
    }

    class PicturesPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;//无限轮播
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mData.size();//因为count无限大了,所以这里要取余
            NewsBean.ResultEntity.NewsData newsData = mData.get(position);
            FrescoImageView frescoImageView = new FrescoImageView(UIUtils.getContext());
            frescoImageView.setImageURI(newsData.thumbnail_pic_s03);//设置网络链接
            frescoImageView.setScaleType(SimpleDraweeView.ScaleType.FIT_XY);
            //设置图片点击事件,跳转到新闻详情
            final int finalPosition = position;
            frescoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UIUtils.getContext(), NewsDetailActivity.class);
                    intent.putExtra("url", mData.get(finalPosition).url);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    UIUtils.getContext().startActivity(intent);
                }
            });
            container.addView(frescoImageView);
            return frescoImageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /*================viewpager页面改变监听=================*/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        position = position % mData.size();
        for (int i = 0; i < mContainerIndicator.getChildCount(); i++) {
            ImageView childView = (ImageView) mContainerIndicator.getChildAt(i);
            if (i == position) {
                childView.setImageResource(R.mipmap.indicator_selected);
            } else {
                childView.setImageResource(R.mipmap.indicator_normal);
            }
        }
        mTvMiniTitle.setText(mData.get(position).title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
