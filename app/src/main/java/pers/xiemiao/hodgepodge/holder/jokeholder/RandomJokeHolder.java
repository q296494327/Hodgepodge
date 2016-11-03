package pers.xiemiao.hodgepodge.holder.jokeholder;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.io.File;
import java.util.Random;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.socialization.QuickCommentBar;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.bean.RandomJokeBean;
import pers.xiemiao.hodgepodge.conf.Constants;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.utils.DensityUtils;
import pers.xiemiao.hodgepodge.utils.DrawableUtils;
import pers.xiemiao.hodgepodge.utils.FileUtils;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.StringUtils;
import pers.xiemiao.hodgepodge.utils.TimeUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;
import pers.xiemiao.hodgepodge.views.CircleImageView;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 22:49
 * Desc: 随机笑话的item  holder
 */
public class RandomJokeHolder extends BaseHolder<RandomJokeBean.RandomJokeData> implements
        View.OnClickListener {

    public TextView mTvContent;
    public CircleImageView mIvHead;
    public TextView mTvTime;
    public ImageButton mIbAudio;
    public AnimationDrawable mAnimAudio;
    public SpeechSynthesizer mTts;
    public RelativeLayout mItemRoot;
    private QuickCommentBar mQcBar;
    private OnekeyShare mOks;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_newest_joke, null);
        mQcBar = (QuickCommentBar) view.findViewById(R.id.qcBar);
        mItemRoot = (RelativeLayout) view.findViewById(R.id.rl_item_root);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);
        mIvHead = (CircleImageView) view.findViewById(R.id.iv_head);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        mIbAudio = (ImageButton) view.findViewById(R.id.ib_audio);
        mIbAudio.setBackgroundResource(R.drawable.anim_audio);//设置播放按钮背景
        mAnimAudio = (AnimationDrawable) mIbAudio.getBackground();//取得动画背景

        showItemRandomBg();//展示条目随机背景
        initHeadIcon();
        //设置按钮点击事件
        mIbAudio.setOnClickListener(this);
        mIvHead.setOnClickListener(this);
        return view;
    }


    @Override
    protected void refreshHolderView(RandomJokeBean.RandomJokeData data) {
        //每次刷新视图的时候,都将播放图标的动画复原
        mAnimAudio.selectDrawable(0);
        mAnimAudio.stop();

        mTvContent.setText(data.content);
        long unixtime = Long.parseLong(data.unixtime);
        mTvTime.setText("更新时间: " + TimeUtils.getTime(unixtime * 1000));

        //底部评论栏的初始化设置
        initOnekeyShare(data);//初始化一键分享
        //关闭sso授权
        mOks.disableSSOWhenAuthorize();
        mQcBar.setTopic(data.hashId, data.content.trim().substring(0, 10) + "…",
                TimeUtils.getTime(unixtime * 1000), "佚名");
        mQcBar.getBackButton().setVisibility(View.GONE);
        mQcBar.setOnekeyShare(mOks);
    }

    /**
     * 初始化一键分享
     */
    private void initOnekeyShare(final RandomJokeBean.RandomJokeData data) {
        ShareSDK.initSDK(UIUtils.getContext());
        mOks = new OnekeyShare();
        //关闭sso授权
        mOks.disableSSOWhenAuthorize();

        //将笑话转换成图片设置为分享
        final String path = FileUtils.getDir("jokepic") + data.hashId + ".jpg";
        File file = new File(path);
        if (!file.exists()) {
            ThreadPoolFactory.getNormalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    StringUtils.stringToImage(path, data.content, 10);
                    UIUtils.postSafeTask(new Runnable() {
                        @Override
                        public void run() {
                            mOks.setImagePath(path);
                        }
                    });
                }
            });
        } else {
            mOks.setImagePath(path);
        }
        mOks.setSite(UIUtils.getContext().getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        mOks.setSiteUrl(Constants.URLS.WEIDOWNLOAD);
    }

    /**
     * 初始化头图标
     */
    private void initHeadIcon() {
        int[] headIcons = {R.mipmap.m1, R.mipmap.m2, R.mipmap.m3, R.mipmap.m4, R.mipmap.m5, R
                .mipmap.m6, R.mipmap.m7, R.mipmap.m8, R.mipmap.m9, R.mipmap.m10, R.mipmap.m11, R
                .mipmap.m12, R.mipmap.m13, R.mipmap.m14, R.mipmap.m15, R.mipmap.m16, R.mipmap.m17, R
                .mipmap.m18, R.mipmap.m19, R.mipmap.m20, R.mipmap.m21, R.mipmap.m22, R.mipmap.m23, R
                .mipmap.m24, R.mipmap.m25, R.mipmap.m26, R.mipmap.m27, R.mipmap.m28, R.mipmap.m29, R
                .mipmap.m30};
        Random random = new Random();
        mIvHead.setImageResource(headIcons[random.nextInt(headIcons.length)]);
    }

    /**
     * 展示item随机背景颜色
     */
    private void showItemRandomBg() {
        //4生成一个随机默认背景颜色
        Random random = new Random();
        int red = 30 + random.nextInt(190);
        int green = 30 + random.nextInt(190);
        int blue = 30 + random.nextInt(190);
        int normalBg = Color.rgb(red, green, blue);
        //5通过颜色生成shpe
        GradientDrawable gradientDrawable = DrawableUtils.getGradientDrawable(normalBg,
                DensityUtils.dp2px(UIUtils.getContext(), 10));
        //6给控件设置shape背景
        mItemRoot.setBackgroundDrawable(gradientDrawable);
    }

    /*================父类点击事件=================*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head://点击头像

                break;
            case R.id.ib_audio://点击声音
                mAnimAudio.start();
                read(mData.content);
                break;
        }
    }


    /*================讯飞语音相关  begin=================*/

    /**
     * 阅读语音
     */
    public void read(String msg) {
        // 1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(UIUtils.getContext(), null);
        // 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        String speaker = SpUtil.getString(UIUtils.getContext(), "speaker", "xiaoyan");//从sp取语言
        mTts.setParameter(SpeechConstant.VOICE_NAME, speaker);// 设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "60");// 设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
        // 设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        // 保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        // 如果不需要保存合成音频，注释该行代码
        //        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        // 3.开始合成
        mTts.startSpeaking(msg, new SynthesizerListener() {

            @Override
            public void onCompleted(SpeechError speechError) {
                //读取完毕后,关闭语音动画
                mAnimAudio.selectDrawable(0);//使动画显示在第一帧
                mAnimAudio.stop();
            }

            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {
            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });

    }

    /*================讯飞语音相关  end=================*/

}
