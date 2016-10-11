package pers.xiemiao.hodgepodge.holder;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.bean.NewestJokeBean;
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


    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_newest_joke, null);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);
        mIvHead = (CircleImageView) view.findViewById(R.id.iv_head);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        mIbAudio = (ImageButton) view.findViewById(R.id.ib_audio);
        mAnimAudio = (AnimationDrawable) mIbAudio.getBackground();
        //设置按钮点击事件
        mIvHead.setOnClickListener(this);
        mIbAudio.setOnClickListener(this);
        return view;
    }

    @Override
    protected void refreshHolderView(NewestJokeBean.ResultEntity.JokeDate data) {
        mTvContent.setText(data.content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head://点击头像
                showPopupWindow();
                break;
            case R.id.ib_audio://点击声音
                read(mData.content);
                mAnimAudio.start();
                break;
        }
    }

    /**
     * 点击头像
     */

    private void showPopupWindow() {

    }

    /*================讯飞语音相关  begin=================*/

    /**
     * 阅读语音
     */
    public void read(String msg) {
        // 1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer
                .createSynthesizer(UIUtils.getContext(), null);
        // 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "nannan");// 设置发音人
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
