package pers.xiemiao.hodgepodge.holder;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.util.Random;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.bean.NewestJokeBean;
import pers.xiemiao.hodgepodge.utils.DensityUtils;
import pers.xiemiao.hodgepodge.utils.DrawableUtils;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.UIUtils;
import pers.xiemiao.hodgepodge.views.CircleImageView;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 22:49
 * Desc: 最新笑话的item  holder
 */
public class NewestJokeHolder extends BaseHolder<NewestJokeBean.ResultEntity.JokeDate> implements
        View.OnClickListener {

    private TextView mTvContent;
    private LinearLayout mLlHead;
    private CircleImageView mIvHead;
    private TextView mTvTime;
    private ImageButton mIbAudio;
    private AnimationDrawable mAnimAudio;
    private SpeechSynthesizer mTts;
    private RelativeLayout mItemRoot;


    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_newest_joke, null);
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
        mIvHead.setOnClickListener(this);
        mIbAudio.setOnClickListener(this);
        return view;
    }


    @Override
    protected void refreshHolderView(NewestJokeBean.ResultEntity.JokeDate data) {
        mTvContent.setText(data.content);
        mTvTime.setText("更新时间: " + data.updatetime);
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
                read(mData.content);
                mAnimAudio.start();
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
