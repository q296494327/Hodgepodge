package pers.xiemiao.hodgepodge.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

import java.util.List;

import lib.lhh.fiv.library.FrescoZoomImageView;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.GirlDetailProtocol;
import pers.xiemiao.hodgepodge.utils.DensityUtils;
import pers.xiemiao.hodgepodge.utils.ScreenUtil;
import pers.xiemiao.hodgepodge.utils.UIUtils;
import pers.xiemiao.hodgepodge.views.ScalePageTransformer;

/**
 * User: xiemiao
 * Date: 2016-10-17
 * Time: 21:25
 * Desc: 美女图片详情的activity
 */
public class BeautyDetailActivity extends AppCompatActivity {


    private String mImgurl;
    private ViewPager mViewPager;
    private GirlDetailProtocol mProtocol;
    private List<String> mImgList;
    private TextView mTvCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_detail);
        mImgurl = getIntent().getStringExtra("imgurl");
        mTvCount = (TextView) findViewById(R.id.tv_count);
        mViewPager = (ViewPager) findViewById(R.id.pic_viewpager);
        mViewPager.setPageTransformer(true, new ScalePageTransformer());
        initData();
    }

    /**
     * 初始化网络图片的数据
     */
    private void initData() {
        //创建线程任务,执行网络请求图片集
        ThreadPoolFactory.getNormalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mProtocol = new GirlDetailProtocol();
                    mImgList = mProtocol.loadData(mImgurl).showapi_res_body.imgList;
                    //在获取到图片集合后,到主线程去给viewpager去设置适配器
                    UIUtils.postSafeTask(new Runnable() {
                        @Override
                        public void run() {
                            MyAdapter myAdapter = new MyAdapter(mImgList);
                            mViewPager.setAdapter(myAdapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 图片viewpager适配器
     */
    class MyAdapter extends PagerAdapter {

        private List<String> mImgList;

        public MyAdapter(List<String> imgList) {
            mImgList = imgList;
        }

        @Override
        public int getCount() {
            return mImgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(BeautyDetailActivity.this, R.layout.viewpager_beauty, null);
            //设置下方的页数数据
            mTvCount.setText(position + "/" + mImgList.size());
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
            //创建控制器去下载图片
            DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true).setUri(mImgList.get(position)).build();
            fivPic.setController(mDraweeController);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
