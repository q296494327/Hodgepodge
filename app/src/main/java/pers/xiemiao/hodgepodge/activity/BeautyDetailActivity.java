package pers.xiemiao.hodgepodge.activity;

import android.os.Bundle;
import android.os.Environment;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;

import lib.lhh.fiv.library.FrescoZoomImageView;
import okhttp3.Call;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.conf.Constants;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.GirlDetailProtocol;
import pers.xiemiao.hodgepodge.utils.DensityUtils;
import pers.xiemiao.hodgepodge.utils.ScreenUtil;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;
import pers.xiemiao.hodgepodge.views.ScalePageTransformer;

/**
 * User: xiemiao
 * Date: 2016-10-17
 * Time: 21:25
 * Desc: 美女图片详情的activity
 */
public class BeautyDetailActivity extends AppCompatActivity implements ViewPager
        .OnPageChangeListener {


    private String mImgurl;
    private ViewPager mViewPager;
    private GirlDetailProtocol mProtocol;
    private List<String> mImgList;
    private TextView mTvCount;
    private InterstitialAd interAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_detail);
        mImgurl = getIntent().getStringExtra("imgurl");
        mTvCount = (TextView) findViewById(R.id.tv_count);
        mViewPager = (ViewPager) findViewById(R.id.pic_viewpager);
        mViewPager.setPageTransformer(true, new ScalePageTransformer());
        mViewPager.setOnPageChangeListener(this);
        initInterAd();//初始化百度插屏广告
        initData();
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
            }

        });
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
                            //在适配器创建完之后,获取存着的历史记录,设置为当前条目,达到存档的效果
                            int currentItem = SpUtil.getInt(BeautyDetailActivity.this,
                                    "currentItem" + mImgurl, 0);
                            mViewPager.setCurrentItem(currentItem);
                            //设置底部翻页文本显示
                            if (mImgList != null) {
                                mTvCount.setText((currentItem + 1) + "/" + mImgList.size());
                            }
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
            if (mImgList != null) {
                return mImgList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(BeautyDetailActivity.this, R.layout.viewpager_beauty, null);
            TextView tvSave = (TextView) view.findViewById(R.id.tv_save);
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
            final String img = mImgList.get(position);
            DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true).setUri(img).build();
            fivPic.setController(mDraweeController);
            tvSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //请求下载
                    ThreadPoolFactory.getDownloadThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            String desFileDir = Environment.getExternalStorageDirectory()
                                    .getAbsolutePath() + "/" + "meinv";
                            String fileName = img.replace(":", "").replace("?", "").replace("/",
                                    "");
                            OkHttpUtils.get().url(img).build().execute(new DowmloadCurPicCallback
                                    (desFileDir, fileName));
                        }
                    });
                }
            });

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
        //页面改变时,动态设置底部页数
        mTvCount.setText(position + 1 + "/" + mImgList.size());

        //将页面选择状况存到sp里
        if (position == mImgList.size() - 1) {
            //如果是最后一页,就将当前item存为0
            SpUtil.putInt(BeautyDetailActivity.this, "currentItem" + mImgurl, 0);
        } else {
            SpUtil.putInt(BeautyDetailActivity.this, "currentItem" + mImgurl, position);
        }
        //在页面的指定位置设置插屏广告
        if (mImgList.size() > 20) {
            if (position % 8 == 0 || position == mImgList.size() - 1) {
                if (interAd.isAdReady()) {
                    interAd.showAd(BeautyDetailActivity.this);
                } else {
                    interAd.loadAd();
                }
            }
        } else {
            if (position % 5 == 0 || position == mImgList.size() - 1) {
                if (interAd.isAdReady()) {
                    interAd.showAd(BeautyDetailActivity.this);
                } else {
                    interAd.loadAd();
                }
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 下载当前图片
     */
    class DowmloadCurPicCallback extends FileCallBack {

        public DowmloadCurPicCallback(String destFileDir, String destFileName) {
            super(destFileDir, destFileName);
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtils.showToast("保存失败,请检查网络或SD卡挂载状态");
        }

        @Override
        public void onResponse(File response, int id) {
            ToastUtils.showToast("图片已保存至SD卡根目录的\"meinv\"文件夹下");
        }
    }

}
