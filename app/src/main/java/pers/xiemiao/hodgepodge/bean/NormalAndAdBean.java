package pers.xiemiao.hodgepodge.bean;

import com.baidu.mobad.feeds.NativeResponse;

/**
 * User: xiemiao
 * Date: 2016-10-25
 * Time: 10:58
 * Desc:
 */
public class NormalAndAdBean {
    public NativeResponse ad;
    public FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity.FunnyVideoData vData;//视频
    public RandomJokeBean.RandomJokeData rjData;//随机笑话
    public NewestJokeBean.ResultEntity.JokeDate njData;//最新笑话
    public boolean isAd;
}
