package pers.xiemiao.hodgepodge.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobads.InterstitialAd;
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
import pers.xiemiao.hodgepodge.bean.BeautyDetailBean;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.BeautyDetailProtocol;
import pers.xiemiao.hodgepodge.utils.DensityUtils;
import pers.xiemiao.hodgepodge.utils.ScreenUtil;
import pers.xiemiao.hodgepodge.utils.ToastUtils;

/**
 * User: xiemiao
 * Date: 2016-10-17
 * Time: 21:25
 * Desc: 美女图片详情的activity
 */
public class BeautyDetailActivity extends AppCompatActivity {


    private String mUrl;
    private ViewPager mViewPager;
    private BeautyDetailProtocol mProtocol;
    private List<BeautyDetailBean.ImageData> mImgdataList;
    private List<String> mImgList;//封装链接地址
    private TextView mTvCount;
    private InterstitialAd interAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_beauty);
        mUrl = getIntent().getStringExtra("url");
        initView();
    }

    private void initView() {
        TextView tvSave = (TextView) findViewById(R.id.tv_save);
        FrescoZoomImageView fivPic = (FrescoZoomImageView) findViewById(R.id.fiv_pagerpic);
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
                .setAutoPlayAnimations(true).setUri(mUrl).build();
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
                        String fileName = mUrl.replace(":", "").replace("?", "").replace("/",
                                "");
                        OkHttpUtils.get().url(mUrl).build().execute(new DowmloadCurPicCallback
                                (desFileDir, fileName));
                    }
                });
            }
        });
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
