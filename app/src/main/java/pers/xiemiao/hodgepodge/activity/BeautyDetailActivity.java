package pers.xiemiao.hodgepodge.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobads.InterstitialAd;
import com.baidu.mobads.InterstitialAdListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import lib.lhh.fiv.library.FrescoZoomImageView;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.bean.BaiduBeautyBean;
import pers.xiemiao.hodgepodge.bean.MessageEvent;
import pers.xiemiao.hodgepodge.conf.Constants;
import pers.xiemiao.hodgepodge.utils.DensityUtils;
import pers.xiemiao.hodgepodge.utils.ScreenUtil;
import pers.xiemiao.hodgepodge.views.ScalePageTransformer;

/**
 * User: xiemiao
 * Date: 2016-10-17
 * Time: 21:25
 * Desc: 美女图片详情的activity
 */
public class BeautyDetailActivity extends AppCompatActivity implements ViewPager
        .OnPageChangeListener {


    private int position;
    private ViewPager mViewPager;
    private TextView mTvCount;
    private List<BaiduBeautyBean.BaiduBeautyData> mDatalist;
    private InterstitialAd interAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_detail);
        //获取传递过来的位置和数据
        position = getIntent().getIntExtra("position", 0);
        mDatalist = (List<BaiduBeautyBean.BaiduBeautyData>)
                getIntent().getSerializableExtra("datalist");
        //初始化控件
        mTvCount = (TextView) findViewById(R.id.tv_count);
        mViewPager = (ViewPager) findViewById(R.id.pic_viewpager);
        mViewPager.setPageTransformer(true, new ScalePageTransformer());//动画
        mViewPager.setOnPageChangeListener(this);//页面改变监听
        initInterAd();//初始化百度插屏广告
        //设置适配器
        MyAdapter myAdapter = new MyAdapter(mDatalist);
        mViewPager.setAdapter(myAdapter);
        //在适配器创建完之后,获取存着的历史记录,设置为当前条目,达到存档的效果
        mViewPager.setCurrentItem(position);//设置当前条目为传递过来的位置
        //设置底部翻页文本显示
        mTvCount.setText((position + 1) + "/" + mDatalist.size());
    }


    /**
     * 百度插屏广告
     */
    private void initInterAd() {
        String adPlaceId = Constants.VIWEPAGER_PLACE_ID; // 重要：请填上您的广告位ID，代码位错误会导致无法请求到广告
        interAd = new InterstitialAd(this, adPlaceId);
        interAd.setListener(new InterstitialAdListener() {

            @Override
            public void onAdClick(InterstitialAd arg0) {
                Log.i("InterstitialAd", "onAdClick");
            }

            @Override
            public void onAdDismissed() {
                Log.i("InterstitialAd", "onAdDismissed");
                interAd.loadAd();
            }

            @Override
            public void onAdFailed(String arg0) {
                Log.i("InterstitialAd", "onAdFailed");
            }

            @Override
            public void onAdPresent() {
                Log.i("InterstitialAd", "onAdPresent");
            }

            @Override
            public void onAdReady() {
                Log.i("InterstitialAd", "onAdReady");
                interAd.loadAd();
            }

        });
    }


    /**
     * 图片viewpager适配器
     */
    class MyAdapter extends PagerAdapter {

        private List<BaiduBeautyBean.BaiduBeautyData> mDatalist;

        public MyAdapter(List<BaiduBeautyBean.BaiduBeautyData> dataList) {
            mDatalist = dataList;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mDatalist.size();
            View view = View.inflate(BeautyDetailActivity.this, R.layout.viewpager_beauty, null);
            FrescoZoomImageView fivPic = (FrescoZoomImageView) view.findViewById(R.id.fiv_pagerpic);
            //获取屏幕的宽高
            int width = ScreenUtil.getScreenWidth(BeautyDetailActivity.this);
            int height = ScreenUtil.getScreenHeight(BeautyDetailActivity.this);
            //给fivpic设置宽高参数,让它充满全屏
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout
                    .LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.width = width;
            params.height = height - DensityUtils.dp2px(BeautyDetailActivity.this, 30);
            fivPic.setLayoutParams(params);
            String url = mDatalist.get(position).download_url;
            //创建控制器去下载图片
            DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true).setUri(url).build();
            fivPic.setController(mDraweeController);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /*-------------------友盟统计---------------------*/
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BeautyDetailActivity"); //统计页面
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BeautyDetailActivity");
        MobclickAgent.onPause(this);
    }

    /*-------------------viewpager页面选择状态监听---------------------*/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        position = position % mDatalist.size();
        //页面改变时,动态设置底部页数
        mTvCount.setText(position + 1 + "/" + mDatalist.size());

        //使用eventbus发消息给recycleview去滚动到相应位置
        MessageEvent event = new MessageEvent();
        event.position = position;
        EventBus.getDefault().post(event);
        //设置插屏广告的时机
        if (position == 0 || position % 10 == 0 || position == mDatalist.size() - 1) {
            if (interAd.isAdReady()) {
                interAd.showAd(BeautyDetailActivity.this);
            } else {
                interAd.loadAd();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
